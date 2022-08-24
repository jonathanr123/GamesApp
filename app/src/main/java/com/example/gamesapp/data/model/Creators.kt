package com.example.gamesapp.data.model

import com.google.gson.annotations.SerializedName

data class Creators (
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("image")
    val imageURL: String? = "",
)