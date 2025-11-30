package com.example.level_up_gamer.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GameRetrofitInstance {
    // Cambiamos a la URL base de CheapShark
    private const val BASE_URL = "https://www.cheapshark.com/api/1.0/"

    val api: GameApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GameApiService::class.java)
    }
}