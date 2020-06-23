package com.example.adoptmypet.api

import com.example.adoptmypet.utils.BASE_API_URL
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object ServiceFactory {
    private val okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()

    val service: Service =
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(okHttpClient.build())
            .build()
            .create(Service::class.java)
}