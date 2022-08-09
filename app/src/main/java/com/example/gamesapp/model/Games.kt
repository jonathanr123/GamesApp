package com.example.gamesapp.model

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
) : Serializable

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
 * @property backgroundImageURL string <uri> (Background image)
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


data class GamePersonList(
    val id: Int,
    val name: String,
    val slug: String,
    val imageURL: String,
    val imageBackground: String,
    val gameCount: Int
)

/**
 * @property boolean (Hidden) Default: false Set image as hidden or visible.
 */
data class ScreenShot(
    val id: Int,
    val imageURL: String,
    val hidden: Boolean,
    val width: Int,
    val height: Int
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

/**
 * Get a list of game achievements.
 */
data class Achievement(
    val id: Int,
    val name: String,
    val description: String,
    val imageURL: String,
    val percent: String
)

/**
 * Get a list of most recent posts from the game's subreddit.
 */
data class RecentPosts(
    val id: Int,
    val name: String,
    val text: String,
    val imageURL: String,
    val URL: String,
    val username: String,
    val usernameURL: String,
    val created: String
)

/**
 * Get streams on Twitch associated with the game
 */
data class TwitchStreams(
    val id: Int,
    val externalID: Int,
    val name: String,
    val description: String,
    val created: String,
    val published: String,
    val thumbnailURL: String,
    val viewCount: Int,
    val language: String
)

/**
 * Get videos from YouTube associated with the game.
 *
 * channel_id
 */
data class YoutubeChannels(
    val id: Int,
    val externalID: String,
    val channelID: String,
    val channelTitle: String,
    val name: String,
    val description: String,
    val created: String,
    val viewCount: Int,
    val commentCount: Int,
    val likeCount: Int,
    val dislikeCount: Int,
    val favoriteCount: Int,
    val thumbnails: Objects
)
