package com.example.adoptmypet.presentation.questionnaire

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.adoptmypet.api.ServiceFactory
import com.example.adoptmypet.models.Question
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionnaireViewModel : ViewModel() {
    var listOfQuestions = MutableLiveData<List<Question>>()


    fun getQuestions(userRole: Boolean) {
        ServiceFactory.service.getQuestionsByRole(userRole)
            .enqueue(object : Callback<List<Question>> {
                override fun onFailure(call: Call<List<Question>>, t: Throwable) {
                    TODO("Not yet implemented")
                }

                override fun onResponse(
                    call: Call<List<Question>>,
                    response: Response<List<Question>>
                ) {
                    response.body().let {
                        if (it != null) {
                            listOfQuestions.postValue(it)
                        }
                    }
                }
            })
    }
}


