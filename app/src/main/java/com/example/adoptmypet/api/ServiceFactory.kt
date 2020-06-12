package com.example.adoptmypet.api

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceFactory {
    private const val BASE_API_URL = "https://10.0.2.2:5001/"
    private val okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient()
//    private val okHttpClient = OkHttpClient()

    val service: Service =
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(okHttpClient.build())
            .build()
            .create(Service::class.java)
}