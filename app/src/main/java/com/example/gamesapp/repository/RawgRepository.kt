package com.example.gamesapp.repository

import com.example.gamesapp.model.Creators
import com.example.gamesapp.model.Games
import com.example.gamesapp.model.Genres
import com.example.gamesapp.services.RawgApiService
import com.example.gamesapp.utils.RawgApiResult
import com.example.gamesapp.utils.RawgData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RawgRepository @Inject constructor(private val apiService: RawgApiService) {

    suspend fun getListOfGameCreators(): Flow<RawgApiResult<RawgData<List<Creators>>>> = flow {
        val response = apiService.getListOfGameCreators()
        when (response.isSuccessful) {
            true -> emit(RawgApiResult.Success(response.body()))
            false -> emit(RawgApiResult.Failure(response.code()))
        }
    }.catch { exception -> emit(RawgApiResult.ErrorThrowable(exception.cause)) }

    suspend fun getListOfGenres(): Flow<RawgApiResult<RawgData<List<Genres>>>> = flow {
        val response = apiService.getListOfGenres(ordering = "id")
        when (response.isSuccessful) {
            true -> emit(RawgApiResult.Success(response.body()))
            false -> emit(RawgApiResult.Failure(response.code()))
        }
    }.catch { exception -> emit(RawgApiResult.ErrorThrowable(exception.cause)) }

    suspend fun getListOfGamePopular(): Flow<RawgApiResult<RawgData<List<Games>>>> = flow {
        val response = apiService.getListOfGames(dates = "2021-01-01,2021-12-31", ordering = "-added")
        when (response.isSuccessful) {
            true -> emit(RawgApiResult.Success(response.body()))
            false -> emit(RawgApiResult.Failure(response.code()))
        }
    }.catch { exception -> emit(RawgApiResult.ErrorThrowable(exception.cause)) }

    suspend fun getListOfGameTrending(): Flow<RawgApiResult<RawgData<List<Games>>>> = flow {
        val response = apiService.getListOfGames(dates = "2022-07-10,2023-08-10", ordering = "-added")
        when (response.isSuccessful) {
            true -> emit(RawgApiResult.Success(response.body()))
            false -> emit(RawgApiResult.Failure(response.code()))
        }
    }.catch { exception -> emit(RawgApiResult.ErrorThrowable(exception.cause)) }

}