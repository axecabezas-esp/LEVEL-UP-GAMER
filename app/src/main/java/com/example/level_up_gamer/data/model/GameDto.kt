package com.example.level_up_gamer.data.model

import com.google.gson.annotations.SerializedName

data class GameDto(
    @SerializedName("gameID") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("salePrice") val price: String, // CheapShark usa "salePrice"
    @SerializedName("normalPrice") val originalPrice: String,
    @SerializedName("thumb") val thumbnail: String, // La imagen se llama "thumb"
    @SerializedName("steamRatingPercent") val rating: String?
)