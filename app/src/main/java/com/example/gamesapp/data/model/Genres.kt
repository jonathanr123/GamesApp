package com.example.gamesapp.data.model

import com.google.gson.annotations.SerializedName

data class Genres (
    val id: Int? = 0,
    val name: String? = "",
    @SerializedName("games_count")
    val gamesCount: Int? = 0,
    @SerializedName("image_background")
    val imageURL: String? = "",
    val description: String? = ""
)