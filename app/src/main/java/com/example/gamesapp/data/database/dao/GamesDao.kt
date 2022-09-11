package com.example.gamesapp.data.database.dao

import androidx.room.*
import com.example.gamesapp.data.database.entities.GamesEntity

@Dao
interface GamesDao {

    @Query("SELECT * FROM games_table")
    suspend fun getAllGames(): List<GamesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllGames(vararg games: GamesEntity)

    @Delete
    suspend fun deleteGame(game: GamesEntity)

}