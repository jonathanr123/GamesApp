package com.example.gamesapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gamesapp.domain.model.GamesItem

@Entity(tableName = "games_table")
data class GamesEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int? = 0,
    @ColumnInfo(name = "name") val name: String? = "",
    @ColumnInfo(name = "imageURL") val imageURL: String? = "",
) {
    fun toDomain(): GamesItem {
        return GamesItem(id = id, name = name, imageURL = imageURL)
    }
}