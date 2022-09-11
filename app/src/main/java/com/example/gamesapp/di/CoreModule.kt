package com.example.gamesapp.di

import android.content.Context
import com.example.gamesapp.core.ResourcesProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    //Injection of the singleton ResourcesProvider getting context as parameter
    @Singleton
    @Provides
    fun provideResourcesProvider(@ApplicationContext context: Context): ResourcesProvider =
        ResourcesProvider(context)

}