package com.example.gamesapp.model

import com.google.gson.annotations.SerializedName

data class Games (
    val id: Int = 0,
    val name: String = "",
    val urlImage: String = ""
)