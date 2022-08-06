package com.example.gamesapp.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gamesapp.model.Games
import com.example.gamesapp.services.RawgApiService
import javax.inject.Inject

class GamePagingSource  @Inject constructor(
    private val apiService: RawgApiService
    ) : PagingSource<Int, Games>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Games> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = apiService.getListOfGames(page = nextPageNumber)
            return LoadResult.Page(
                data = response.body()!!.result,
                prevKey = null, // Only paging forward.
                nextKey = nextPageNumber + 1
            )
        } catch (e: Exception) {
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
        }
        return LoadResult.Error(
            throwable = IllegalStateException("Error loading games")
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Games>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}