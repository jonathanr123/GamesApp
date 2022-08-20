package com.example.gamesapp.di

import com.example.gamesapp.BuildConfig
import com.example.gamesapp.data.services.RawgApiService
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {
    private const val BASE_URL = "https://api.rawg.io/api/"

    @Singleton
    @Provides
    fun getOkHttpClient(): OkHttpClient {

        val loggingInterceptor = HttpLoggingInterceptor()

        if(BuildConfig.DEBUG){
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }else{
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(getOkHttpClient())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(): RawgApiService = getRetrofit().create(RawgApiService::class.java)

}