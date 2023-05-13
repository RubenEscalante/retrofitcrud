package com.udb.defensa.data.retrofit

import retrofit2.Retrofit
import com.google.gson.GsonBuilder
import com.udb.defensa.utils.BASE_URL
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val webService: WebService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(WebService::class.java)
    }
}