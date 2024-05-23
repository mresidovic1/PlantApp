package com.example.plantapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiAdapter {
    val retrofit : Api = Retrofit.Builder()
        .baseUrl("https://trefle.io/api/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Api::class.java)
}