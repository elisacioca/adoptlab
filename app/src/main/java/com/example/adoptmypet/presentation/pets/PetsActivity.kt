package com.example.adoptmypet.presentation.pets

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.adoptmypet.R
import com.example.adoptmypet.models.Pet
import com.example.adoptmypet.presentation.PetDetailsActivity
import com.example.adoptmypet.presentation.dialogs.ErasePetDialog
import com.example.adoptmypet.presentation.WelcomeActivity
import com.example.adoptmypet.presentation.adoptions.AdoptionsActivity
import com.example.adoptmypet.presentation.dialogs.ErrorDialog
import com.example.adoptmypet.utils.service
import kotlinx.android.synthetic.main.acitivity_pets.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetsActivity : AppCompatActivity(),
    PetsAdapter.PetItemInterface, ErasePetDialog.ErasePetDialogListener{

    private lateinit var adapter: PetsAdapter
    private val viewModel by lazy {
        ViewModelProvider(this).get(PetsViewModel::class.java)
    }

    val listOfPets = mutableListOf<Pet>()
    var username: String? = ""
    var pet: Pet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.acitivity_pets)
        setupAdapter()
        observeEvents()

        getUsername()
        viewModel.getListOfPets(username ?: "")
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
                viewModel.getListOfPets(username ?: "")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUsername() {
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username", "")
    }

    private fun observeEvents() {
        viewModel.listOfPets.observe(this, Observer {
            if (it.isNullOrEmpty().not()) {
                listOfPets.clear()
                listOfPets.addAll(it)
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun setupAdapter() {
        adapter =
            PetsAdapter(
                listOfPets,
                this
            )
        rvPets.adapter = adapter
    }

    override fun onEraseClicked(item: Pet) {
        val dialog: ErasePetDialog = ErasePetDialog()
        dialog.show(supportFragmentManager, "Erase")
    }

    override fun onSeeCandidatesClicked(item: Pet) {
        pet = item
        val intent = Intent(this, AdoptionsActivity::class.java)
        intent.putExtra("petId", item.petId)
        startActivity(intent)
    }

    override fun erasePet() {
        if(pet != null) {
            service.deletePet(pet!!.petId!!)
                .enqueue(object : Callback<Unit> {
                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        val dialog: ErrorDialog = ErrorDialog
                        dialog.show(supportFragmentManager, "Error")
                    }

                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        response.body().let {

                        }
                    }
                })
            finish();
            startActivity(getIntent());
        }
    }
}