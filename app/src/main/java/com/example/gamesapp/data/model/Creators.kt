package com.example.gamesapp.data.model

import com.google.gson.annotations.SerializedName

data class Creators (
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("image")
    val imageURL: String = "",
    @SerializedName("image_background")
    val imageBackgroundURL: String = "",
    @SerializedName("games_count")
    val gameCount: Int = 0,
    @SerializedName("description")
    val description:String = "",
    @SerializedName("review_count")
    val reviewsCount:Int = 0,
    @SerializedName("rating")
    val rating:String = "",
    @SerializedName("rating_top")
    val ratingTop:Int = 0
)