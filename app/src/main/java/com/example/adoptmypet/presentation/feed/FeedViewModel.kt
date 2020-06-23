package com.example.adoptmypet.presentation.feed

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.adoptmypet.models.Location
import com.example.adoptmypet.models.Pet
import com.example.adoptmypet.utils.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedViewModel : ViewModel() {
    val listOfPets = MutableLiveData<List<Pet>>()
    val location = MutableLiveData<Location>()

    fun getListOfPets(affection: Int, freedom: Int, factorRisk: Int, animalType: Int) {
        location.value?.locationId?.let {
            service.getPetsByStatistics(affection, freedom, factorRisk, animalType, it)
                .enqueue(object : Callback<List<Pet>> {
                    override fun onFailure(call: Call<List<Pet>>, t: Throwable) {
                        Log.e("FeedViewModel", t.localizedMessage ?: "")
                    }

                    override fun onResponse(call: Call<List<Pet>>, response: Response<List<Pet>>) {
                        response.body().let {
                            listOfPets.value = it
                        }
                    }
                })
        }
    }
}