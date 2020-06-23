package com.example.adoptmypet.presentation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.adoptmypet.R
import com.example.adoptmypet.databinding.ActivityPetDetailsBinding
import com.example.adoptmypet.models.Adoption
import com.example.adoptmypet.models.Pet
import com.example.adoptmypet.presentation.dialogs.AdoptionDialog
import com.example.adoptmypet.presentation.dialogs.AdoptionDialogNoDetails
import com.example.adoptmypet.presentation.dialogs.SuccessDialog
import com.example.adoptmypet.utils.GlideUtil
import com.example.adoptmypet.utils.gson
import com.example.adoptmypet.utils.service
import kotlinx.android.synthetic.main.activity_pet_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetDetailsActivity : AppCompatActivity(), AdoptionDialogNoDetails.AdoptionDialogListener {

    private lateinit var binding: ActivityPetDetailsBinding
    private lateinit var pet: Pet
    private var username: String? = ""
    private var questionnaire: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@PetDetailsActivity, R.layout.activity_pet_details)

        questionnaire = intent.getStringExtra("questionnaire")
        pet = gson.fromJson<Pet>(intent.getStringExtra("pet"), Pet::class.java)
        GlideUtil.loadImage(view = photo, id = pet.petId)

        getUsername()

        binding.pet = pet;
    }

    private fun getUsername() {
        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username", "")
    }

    fun onCancelClicked(view: View) {
        this.finish()
    }

    fun onAdoptClicked(view: View) {
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
            service.addAdoption(adoption)
                .enqueue(object : Callback<Adoption> {
                override fun onFailure(call: Call<Adoption>, t: Throwable) {
                    Log.e("FeedViewModel", t.localizedMessage ?: "")
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
}
