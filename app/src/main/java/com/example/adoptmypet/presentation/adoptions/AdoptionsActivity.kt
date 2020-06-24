package com.example.adoptmypet.presentation.adoptions

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.adoptmypet.R
import com.example.adoptmypet.models.Adoption
import com.example.adoptmypet.presentation.WelcomeActivity
import com.example.adoptmypet.presentation.adoption_details.AdoptionDetailsActivity
import com.example.adoptmypet.utils.gson
import kotlinx.android.synthetic.main.acitivity_pets.*
import kotlinx.android.synthetic.main.activity_adoption.*

class AdoptionsActivity : AppCompatActivity(),
    AdoptionsAdapter.AdoptionItemInterface {

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
        viewModel.getListOfAdoptions(petId ?: "")
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
                viewModel.getListOfAdoptions(petId?:"")
            }
        }
        return super.onOptionsItemSelected(item)
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
        rvAdoption.adapter = adapter
    }

    override fun onDetailsItemClicked(item: Adoption) {
        val intent = Intent(this, AdoptionDetailsActivity::class.java)
        val jsonPet = gson.toJson(item)
        intent.putExtra("adoption", jsonPet)
        startActivity(intent)
    }
}