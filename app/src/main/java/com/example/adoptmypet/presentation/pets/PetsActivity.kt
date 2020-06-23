package com.example.adoptmypet.presentation.pets

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.adoptmypet.R
import com.example.adoptmypet.models.Pet
import com.example.adoptmypet.presentation.PetDetailsActivity
import com.example.adoptmypet.utils.gson
import com.example.adoptmypet.utils.service
import kotlinx.android.synthetic.main.acitivity_pets.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetsActivity : AppCompatActivity(),
    PetsAdapter.PetItemInterface{

    private lateinit var adapter: PetsAdapter
    private val viewModel by lazy {
        ViewModelProvider(this).get(PetsViewModel::class.java)
    }

    val listOfPets = mutableListOf<Pet>()
    var username: String? = ""

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
        viewModel.getListOfPets(username?:"")
    }

    private fun getUsername() {
        val sharedPreference =  getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
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
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Esti sigur ca vrei sa stergi?")
        alertDialogBuilder.setMessage("Daca nu ai reusit sa gasesti stapan pentru animalul tau si ti-ai pierdut speranta incearca sa ne mai oferi o sansa. Poate chiar maine va aparea persoana potrivita.")

        alertDialogBuilder.setPositiveButton("Sunt sigur!") { dialog, which ->
            service.deletePet(item.petId!!)
                .enqueue(object : Callback<Unit> {
                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        Log.e("", t.localizedMessage ?: "")
                    }
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        response.body().let {
                        }
                    }
                })
            finish();
            startActivity(getIntent());
        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->
        }

        alertDialogBuilder.show()
    }

    override fun onSeeCandidatesClicked(item: Pet) {
        val intent = Intent(this, PetDetailsActivity::class.java)
        intent.putExtra("petId", item.petId)
        startActivity(intent)
    }
}