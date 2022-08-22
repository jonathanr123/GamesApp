package com.example.gamesapp.data.model

import com.example.gamesapp.data.database.entities.GamesEntity
import com.example.gamesapp.domain.model.GamesItem
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Games(
    val id: Int? = 0,
    val name: String? = "",
    val metacritic: Int? = 0,
    val released: String? = "",
    @SerializedName("background_image")
    val imageURL: String? = "",
    val rating: Float? = 0.0f,
    val platforms: List<Platforms>? = listOf(),
    val genres: List<Genres>? = listOf(),
    @SerializedName("short_screenshots")
    val shortScreenshots: List<ShortScreenshot>? = listOf(),
) : Serializable {
    fun toDatabase(): GamesEntity {
        return GamesEntity(id = id, name = name, imageURL = imageURL)
    }
    fun toDomain(): GamesItem {
        return GamesItem(id = id, name = name, imageURL = imageURL)
    }
}

data class Platforms(
    val platform: Platform? = Platform(),
) : Serializable

data class Platform(
    val id: Int? = 0,
    val name:String? = ""
) : Serializable

data class ShortScreenshot(
    val id: Int? = 0,
    val image: String? = ""
) : Serializable

/**
 * @property id integer (ID)
 * @property name string (Name) non-empty
 * @property description string (Description) non-empty
 * @property metacritic integer (Metacritic)
 * @property released string <date> (Released)
 * @property imageURL string <uri> (Background image)
 * @property websiteURL string <uri> (Website) non-empty
 * @property rating float (Rating)
 * @property developers array of objects (Developers) non-empty
 *
 */
data class GameSingle(
    val id: Int? = 0,
    val name: String? = "",
    @SerializedName("description_raw")
    val description: String? = "",
    val metacritic: Int? = 0,
    val released: String? = "",
    @SerializedName("background_image")
    val imageURL: String? = "",
    @SerializedName("website")
    val websiteURL: String? = "",
    val rating: Float? = 0.0f,
    val developers: List<Developer>? = listOf(),
)

data class Developer(
    val id:Int? = 0,
    val name:String? = ""
)


/**
 * Get links to the stores that sell the game.
 */
data class GameStoreFull(
    val id: Int,
    val gameID: Int,
    val storeID: Int,
    val url: String
)
