package com.example.adoptmypet.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.adoptmypet.R
import com.example.adoptmypet.api.ServiceFactory
import com.example.adoptmypet.models.Location
import com.example.adoptmypet.models.Pet
import com.example.adoptmypet.presentation.dialogs.ErrorDialog
import com.example.adoptmypet.presentation.pets.PetsActivity
import com.example.adoptmypet.presentation.pets.PetsAdapter
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_pet_description.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream


class PetDescriptionActivity : AppCompatActivity() {
    val permissions = mutableListOf<String>()

    companion object {
        const val PICK_IMAGE = 1000
        private const val PERMISSION_CODE = 42
    }

    var affection: Int = 0
    var freedom: Int = 0
    var factorRisk: Int = 0
    var latitude = 0.0
    var longitude = 0.0
    var locationId: String? = null
    var photo: Uri? = null
    var petId: String? = null
    var questionnaire: String?  = ""
    var animalType: Int? = 0
    var age: String? = ""
    var username: String? = ""
    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_description)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        customizeSpinner()
        affection = intent.getIntExtra("affection", 0)
        freedom = intent.getIntExtra("freedom", 0)
        factorRisk = intent.getIntExtra("factorRisk", 0)
        questionnaire = intent.getStringExtra("questionnaire")
        animalType = intent.getIntExtra("animalType", 0)
        age = intent.getStringExtra("age")
        getUsername()
    }

    private fun getUsername() {
        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
        username = sharedPreference.getString("username", "")
    }

    private fun customizeSpinner() {
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            this,
            R.array.sex_array, R.layout.spinner_item
        )

        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        petSex.adapter = adapter
    }

    private fun selectImage(context: Context) {
        val options =
            arrayOf<CharSequence>("Alege din galerie", "Cancel")
        val builder =
            AlertDialog.Builder(context)
        builder.setTitle("Alege o poza cu animalutul tau")
        builder.setItems(options) { dialog, item ->
            if (options[item] == "Alege din galerie") {
                val pickPhoto = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                startActivityForResult(pickPhoto, 1)
            } else if (options[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    fun choosePhoto(view: View) {
        checkPermissionForImage()
        sample.visibility = View.GONE
    }

    fun onPetSubmit(view: View) {
        GlobalScope.launch {
            getLastLocation()
        }
    }

    private fun addPetToDatabase() {
        var pet: Pet = Pet(
            animalType = animalType,
            name = petName.text.toString(),
            age = age,
            sex = petSex.selectedItemPosition,
            color = petColor.text.toString(),
            description = petDescription.text.toString(),
            story = petStory.text.toString(),
            affection = affection,
            freedom = freedom,
            locationId = locationId,
            userId = username
        )
        ServiceFactory.service.addPet(pet)
            .enqueue(object : Callback<Pet> {
                override fun onFailure(call: Call<Pet>, t: Throwable) {
                    val dialog: ErrorDialog = ErrorDialog
                    dialog.show(supportFragmentManager, "Error")
                    TODO("Not yet implemented")
                }
                override fun onResponse(
                    call: Call<Pet>,
                    response: Response<Pet>
                ) {
                    response.body().let {
                        if (it != null) {
                            petId = it.petId
                            AddPhoto()
                        }
                    }
                }
            })
    }

    private fun addLocation() {
        var location: Location = Location(longitude = longitude, latitude = latitude)

        ServiceFactory.service.addLocation(location = location)
            .enqueue(object : Callback<Location> {
                override fun onFailure(
                    call: Call<Location>,
                    t: Throwable
                ) {
                    val dialog: ErrorDialog = ErrorDialog
                    dialog.show(supportFragmentManager, "Error")
                }

                override fun onResponse(
                    call: Call<Location>,
                    response: Response<Location>
                ) {
                    if (response.isSuccessful) {
                        response.body().let {
                            if (it != null) {
                                locationId = it.locationId
                                addPetToDatabase()
                            }
                        }
                    }
                }
            })
    }

    private fun AddPhoto() {
        var part: MultipartBody.Part? = null
        if (photo != null) {
            val imageStream = contentResolver.openInputStream(photo!!)
            val imageBitmap = BitmapFactory.decodeStream(imageStream)
            val location = Environment.getExternalStorageDirectory()

            val file = File.createTempFile(
                "photo", /* prefix */
                ".jpg", /* suffix */
                location
            )
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, FileOutputStream(file))
            file.let {
                val fileReqBody = it.asRequestBody("image/jpeg".toMediaTypeOrNull())
                part =
                    MultipartBody.Part.createFormData(
                        "photo",
                        "file",
                        fileReqBody
                    )

                val builder = MultipartBody.Builder().addPart(part!!)
                builder.setType(MultipartBody.FORM)
                builder.build()
            }

            val petInfo: RequestBody = petId!!.toRequestBody(okhttp3.MultipartBody.FORM)

            ServiceFactory.service.addPhotoMultipart(petInfo, part)
                .enqueue(object : Callback<Unit> {
                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        val dialog: ErrorDialog = ErrorDialog
                        dialog.show(supportFragmentManager, "Error")
                    }
                    override fun onResponse(
                        call: Call<Unit>,
                        response: Response<Unit>
                    ) {
                        if (response.isSuccessful) {
                            Log.e("succes", "succes!")
                            goToNextActivity()
                        }
                    }
                })
        }
    }

    private fun goToNextActivity() {
        val intent = Intent(this, PetsActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: android.location.Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        latitude = location.latitude
                        longitude = location.longitude
                        addLocation()
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private
    val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET
            ),
            PERMISSION_CODE
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun checkPermissionForImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(
                    permissions.toTypedArray(),
                    PICK_IMAGE
                ) // GIVE AN INTEGER VALUE FOR PERMISSION_CODE_READ LIKE 1001 } else {
            } else if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            ) {
                selectImage(this)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                1 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    photo = data.data
                    addPhotos.setImageURI(photo)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PICK_IMAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                selectImage(this)
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_LONG).show()
            }
        }
        if (requestCode == PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
    }


}
