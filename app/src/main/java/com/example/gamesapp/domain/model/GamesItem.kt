package com.example.gamesapp.domain.model

import com.example.gamesapp.data.model.Games
import com.example.gamesapp.data.model.Genres
import com.example.gamesapp.data.model.Platforms
import com.example.gamesapp.data.model.ShortScreenshot

data class GamesItem(
    val id: Int? = 0,
    val name: String? = "",
    val metacritic: Int? = 0,
    val released: String? = "",
    val imageURL: String? = "",
    val rating: Float? = 0.0f,
    val platforms: List<Platforms>? = listOf(),
    val genres: List<Genres>? = listOf(),
    val shortScreenshots: List<ShortScreenshot>? = listOf()
) {
    fun toModel(): Games {
        return Games(id, name, metacritic, released, imageURL, rating, platforms, genres, shortScreenshots)
    }
}