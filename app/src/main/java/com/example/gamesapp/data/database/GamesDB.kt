package com.example.gamesapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gamesapp.data.database.dao.GamesDao
import com.example.gamesapp.data.database.entities.GamesEntity

@Database(entities = [GamesEntity::class], version = 1)
abstract class GamesDB: RoomDatabase() {

    abstract val gamesDao: GamesDao
}