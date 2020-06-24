package com.example.adoptmypet.presentation.adoption_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.adoptmypet.R
import com.example.adoptmypet.databinding.ActivityAdoptionDetailsBinding
import com.example.adoptmypet.models.Adoption
import com.example.adoptmypet.utils.gson
import com.example.adoptmypet.utils.service

class AdoptionDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdoptionDetailsBinding
    private lateinit var adoption: Adoption

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adoption_details)
        binding = DataBindingUtil.setContentView(this@AdoptionDetailsActivity, R.layout.activity_adoption_details)

        adoption = gson.fromJson<Adoption>(intent.getStringExtra("adoption"), Adoption::class.java)

        binding.adoption = adoption;
    }

    fun onDeleteItemClicked(view: View) {
        adoption.adoptionId?.let { service.deleteAdoption(it) }
    }
}
