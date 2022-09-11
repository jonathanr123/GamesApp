package com.example.gamesapp.di

import android.content.Context
import androidx.room.Room
import com.example.gamesapp.data.database.GamesDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DATABASE_NAME = "games_db"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) = Room.databaseBuilder(context, GamesDB::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideGamesDao(db: GamesDB) = db.gamesDao

}