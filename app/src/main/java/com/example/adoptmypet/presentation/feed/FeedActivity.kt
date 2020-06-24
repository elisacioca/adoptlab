package com.example.adoptmypet.presentation.feed

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.adoptmypet.R
import com.example.adoptmypet.api.ServiceFactory
import com.example.adoptmypet.models.Adoption
import com.example.adoptmypet.models.Location
import com.example.adoptmypet.models.Pet
import com.example.adoptmypet.presentation.dialogs.AdoptionDialog
import com.example.adoptmypet.presentation.PetDetailsActivity
import com.example.adoptmypet.presentation.dialogs.SuccessDialog
import com.example.adoptmypet.presentation.WelcomeActivity
import com.example.adoptmypet.presentation.dialogs.ErrorDialog
import com.example.adoptmypet.presentation.dialogs.FactorRiskDialog
import com.example.adoptmypet.utils.gson
import com.example.adoptmypet.utils.service
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_feed.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedActivity : AppCompatActivity(),
    FeedAdapter.FeedItemInterface, AdoptionDialog.AdoptionDialogListener, FactorRiskDialog.FactorRiskListener {

    companion object {
        private const val PERMISSION_CODE = 42
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(FeedViewModel::class.java)
    }
    private lateinit var adapter: FeedAdapter
    val permissions = mutableListOf<String>()
    val listOfPets = mutableListOf<Pet>()
    var latitude = 0.0
    var longitude = 0.0
    var locationId: String? = null
    var username: String? = ""
    var pet: Pet? = null
    var questionnaire: String? = ""
    lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_feed)

        verifyIfFactorRisk()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        questionnaire = intent.getStringExtra("questionnaire")
        setupAdapter()
        getUsername()
        getLastLocation()
        observeEvents()
    }

    private fun verifyIfFactorRisk() {
        val factorRisk = intent.getIntExtra("factorRisk", 0)
        if(factorRisk > 10)
        {
            val dialog: FactorRiskDialog = FactorRiskDialog()
            dialog.show(supportFragmentManager, "Oops")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.home -> {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            }
            R.id.refresh -> {
                refresh()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun refresh() {
        val affection = intent.getIntExtra("affection", 0)
        val freedom = intent.getIntExtra("freedom", 0)
        val factorRisk = intent.getIntExtra("factorRisk", 0)
        val animalType = intent.getIntExtra("animalType", 0)

        viewModel.getListOfPets(affection, freedom, factorRisk, animalType)
    }

    private fun getUsername() {
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username", "")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
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
                                viewModel.location.value = Location(
                                    locationId = locationId,
                                    latitude = latitude,
                                    longitude = longitude
                                )
                            }
                        }
                    }
                }
            })
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

    private fun getPets() {
        val affection = intent.getIntExtra("affection", 0)
        val freedom = intent.getIntExtra("freedom", 0)
        val factorRisk = intent.getIntExtra("factorRisk", 0)
        val animalType = intent.getIntExtra("animalType", 0)
        viewModel.getListOfPets(affection, freedom, factorRisk, animalType)
    }

    private fun observeEvents() {
        viewModel.listOfPets.observe(this, Observer {
            if (it.isNullOrEmpty().not()) {
                listOfPets.clear()
                listOfPets.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })
        viewModel.location.observe(this, Observer {
            if (it != null) {
                getPets()
            }
        })
    }

    private fun setupAdapter() {
        adapter =
            FeedAdapter(
                listOfPets,
                this
            )
        rvFeed.adapter = adapter
    }

    override fun onReadMoreClicked(item: Pet) {
        val intent = Intent(this, PetDetailsActivity::class.java)
        val jsonPet = gson.toJson(item)
        intent.putExtra("pet", jsonPet)
        intent.putExtra("questionnaire", questionnaire)
        startActivity(intent)
    }

    override fun onAdoptClicked(item: Pet) {
        pet = item
        val dialog: AdoptionDialog =
            AdoptionDialog()
        dialog.show(supportFragmentManager, "Adoption dialog")
    }


    override fun applyText(fullName: String, phoneNo: String) {
        if (pet != null) {
            var adoption = Adoption(
                userAdopterId = username,
                petId = pet!!.petId,
                telephoneNo = phoneNo,
                fullNameAdopter = fullName,
                questionnaireAdopter = questionnaire
            )
            service.addAdoption(adoption) .enqueue(object : Callback<Adoption> {
                override fun onFailure(call: Call<Adoption>, t: Throwable) {
                    val dialog: ErrorDialog = ErrorDialog
                    dialog.show(supportFragmentManager, "Error")
                }
                override fun onResponse(call: Call<Adoption>, response: Response<Adoption>) {
                    response.body().let {
                        val dialog: SuccessDialog = SuccessDialog()
                        dialog.show(supportFragmentManager, "Success")
                    }
                }
            })
        }
    }

    override fun getDetails() {
        val intent = Intent(this, PetDetailsActivity::class.java)
        val jsonPet = gson.toJson(pet)
        intent.putExtra("pet", jsonPet)
        startActivity(intent)
    }

    override fun backToHome() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
    }
}
