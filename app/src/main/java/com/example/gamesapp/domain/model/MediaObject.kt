package com.example.gamesapp.domain.model

data class MediaObject (
    var id: Int? = 0,
    var title: String? = "",
    var url: String? = "",
    var coverUrl: String? = "",
    var description: String? = "",
    var genres: String? = "",
)