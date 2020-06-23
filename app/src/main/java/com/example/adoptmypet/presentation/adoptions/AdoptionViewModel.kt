package com.example.adoptmypet.presentation.adoptions

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.adoptmypet.models.Adoption
import com.example.adoptmypet.models.Pet
import com.example.adoptmypet.utils.service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdoptionViewModel : ViewModel() {
    val listOfAdoptions = MutableLiveData<List<Adoption>>()

    fun getListOfAdoptions(petId: String) {
            service.getAdoptionsByStatistics(petId)
                .enqueue(object : Callback<List<Adoption>> {
                    override fun onFailure(call: Call<List<Adoption>>, t: Throwable) {
                        Log.e("FeedViewModel", t.localizedMessage ?: "")
                    }
                    override fun onResponse(call: Call<List<Adoption>>, response: Response<List<Adoption>>) {
                        response.body().let {
                            listOfAdoptions.value = it
                        }
                    }
                })
    }
}