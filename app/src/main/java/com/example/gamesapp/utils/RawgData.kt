package com.example.gamesapp.utils

import com.google.gson.annotations.SerializedName

data class RawgData<T>(
    @SerializedName("count")
    val count: Int,
    @SerializedName("next")
    val next: String,
    @SerializedName("previous")
    var prev: String,
    @SerializedName("results")
    var result: T
)