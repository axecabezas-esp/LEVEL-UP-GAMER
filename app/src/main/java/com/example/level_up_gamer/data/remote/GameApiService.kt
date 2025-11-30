package com.example.level_up_gamer.data.remote

import com.example.level_up_gamer.data.model.GameDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GameApiService {
    @GET("deals")
    suspend fun getAllGames(
        @Query("storeID") storeId: String = "1", // Steam por defecto
        @Query("upperPrice") upperPrice: Int = 15 // Menos de $15 por defecto
    ): List<GameDto>
}