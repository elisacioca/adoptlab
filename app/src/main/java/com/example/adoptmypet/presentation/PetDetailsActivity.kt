package com.example.adoptmypet.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.adoptmypet.R
import com.example.adoptmypet.databinding.ActivityPetDetailsBinding
import com.example.adoptmypet.models.Pet
import com.example.adoptmypet.utils.GlideUtil
import com.example.adoptmypet.utils.gson
import kotlinx.android.synthetic.main.activity_pet_details.*

class PetDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPetDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@PetDetailsActivity, R.layout.activity_pet_details)

        val pet = gson.fromJson<Pet>(intent.getStringExtra("pet"), Pet::class.java)
        GlideUtil.loadImage(view = photo, id = pet.petId)

        binding.pet = pet;
    }
}
