package com.example.gamesapp.utils

sealed class RawgApiResult<out T> {
    data class Success<T>(val data: T?) : RawgApiResult<T>()
    data class Failure(val statusCode: Int?) : RawgApiResult<Nothing>()
    data class ErrorThrowable<T>(val errorThrowable: Throwable?) : RawgApiResult<T>()
    data class Loading<T>(val data: T? = null) : RawgApiResult<T>()

    companion object {
        fun <T> success(data: T?): RawgApiResult<T> = Success(data)
        fun failure(statusCode: Int?): RawgApiResult<Nothing> = Failure(statusCode)
        fun <T> errorThrowable(errorThrowable: Throwable?): RawgApiResult<T> = ErrorThrowable(errorThrowable)
        fun <T> loading(): RawgApiResult<T> = Loading()
    }
}