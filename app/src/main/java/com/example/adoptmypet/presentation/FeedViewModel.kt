package com.example.adoptmypet.presentation.feed

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.adoptmypet.models.Pet
import com.example.adoptmypet.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedViewModel : ViewModel() {
    val listOfPets = MutableLiveData<List<Pet>>()

    fun getListOfPets() {
        service.getPetsByStatistics(100, 100, 100).enqueue(object : Callback<List<Pet>> {
            override fun onFailure(call: Call<List<Pet>>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call<List<Pet>>, response: Response<List<Pet>>) {
                response.body().let {
                    listOfPets.value = it
                }
            }
        })
    }
}