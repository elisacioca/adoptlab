package com.example.adoptmypet.presentation.adoptions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.adoptmypet.R
import com.example.adoptmypet.models.Adoption
import com.example.adoptmypet.models.Pet
import com.example.adoptmypet.presentation.PetDetailsActivity
import com.example.adoptmypet.presentation.adoption_details.AdoptionDetailsActivity
import com.example.adoptmypet.utils.gson
import com.example.adoptmypet.utils.service
import kotlinx.android.synthetic.main.acitivity_pets.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdoptionsActivity : AppCompatActivity(),
    AdoptionsAdapter.AdoptionItemInterface{

    private lateinit var adapter: AdoptionsAdapter
    private val viewModel by lazy {
        ViewModelProvider(this).get(AdoptionViewModel::class.java)
    }

    val listOfAdoptions = mutableListOf<Adoption>()
    var petId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_adoption)
        setupAdapter()
        observeEvents()

        petId = intent.getStringExtra("petId")
        viewModel.getListOfAdoptions(petId?:"")
    }

    private fun observeEvents() {
        viewModel.listOfAdoptions.observe(this, Observer {
            if (it.isNullOrEmpty().not()) {
                listOfAdoptions.clear()
                listOfAdoptions.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun setupAdapter() {
        adapter =
            AdoptionsAdapter(
                listOfAdoptions,
                this
            )
        rvPets.adapter = adapter
    }

    override fun onDetailsItemClicked(item: Adoption) {
        val intent = Intent(this, AdoptionDetailsActivity::class.java)
        val jsonPet = gson.toJson(item)
        intent.putExtra("adoption", jsonPet)
        startActivity(intent)
    }
}