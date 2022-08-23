package com.example.gamesapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gamesapp.data.model.Genres
import com.example.gamesapp.data.model.Platforms
import com.example.gamesapp.data.model.ShortScreenshot
import com.example.gamesapp.domain.model.GamesItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@Entity(tableName = "games_table")
data class GamesEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int? = 0,
    @ColumnInfo(name = "name") val name: String? = "",
    @ColumnInfo(name = "metacritic") val metacritic: Int? = 0,
    @ColumnInfo(name = "released") val released: String? = "",
    @ColumnInfo(name = "imageURL") val imageURL: String? = "",
    @ColumnInfo(name = "rating") val rating: Float? = 0.0f,
    @ColumnInfo(name = "platforms")val platforms: String? = "",
    @ColumnInfo(name = "genres")val genres: String? = "",
    @ColumnInfo(name = "shortScreenshots")val shortScreenshots: String? = "",
) {
    fun toDomain(): GamesItem {
        val platformsJson: List<Platforms>? = Gson().fromJson(platforms, object: TypeToken<List<Platforms>>(){}.type)
        val genresJson: List<Genres>? = Gson().fromJson(genres, object: TypeToken<List<Genres>>(){}.type)
        val shortScreenshotsJson: List<ShortScreenshot>? = Gson().fromJson(shortScreenshots, object: TypeToken<List<ShortScreenshot>>(){}.type)
        return GamesItem(id, name, metacritic, released, imageURL, rating, platformsJson, genresJson, shortScreenshotsJson)
    }
}