package com.example.gamesapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gamesapp.data.model.Games
import com.example.gamesapp.data.repository.RawgRepository
import com.example.gamesapp.utils.RawgApiResult
import com.example.gamesapp.utils.RawgData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: RawgRepository) : ViewModel() {

    // LiveData to observe the state of the response from the repository and update the UI
    private val _gamesSearch: MutableLiveData<RawgApiResult<RawgData<List<Games>>>> =
        MutableLiveData(RawgApiResult.loading())
    val gamesSearch: LiveData<RawgApiResult<RawgData<List<Games>>>> = _gamesSearch

    private val _allGames: MutableStateFlow<Flow<PagingData<Games>>> = MutableStateFlow(flowOf(PagingData.empty()))
    // flow for paging games list from repository
    val allGames: StateFlow<Flow<PagingData<Games>>> = _allGames

    init {
        fetchAllGames()
    }

    // Fetch games searched by name from repository and update livedata with result
    fun fetchGamesByName(nameSearch:String? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getGameByName(nameSearch)
                .catch { throwable ->
                    _gamesSearch.postValue(RawgApiResult.errorThrowable(Exception(throwable.cause))) }
                .collect { response ->
                    when (response) {
                        is RawgApiResult.Success ->
                        {
                            _gamesSearch.postValue(RawgApiResult.success(response.data))}

                        is RawgApiResult.Failure ->
                            _gamesSearch.postValue(RawgApiResult.failure(response.statusCode))

                        is RawgApiResult.ErrorThrowable ->
                            _gamesSearch.postValue(RawgApiResult.errorThrowable(response.errorThrowable))

                        is RawgApiResult.Loading ->
                            _gamesSearch.postValue(RawgApiResult.loading())
                    }
                }
        }
    }

    private fun fetchAllGames(){
        _allGames.value = Pager( PagingConfig(pageSize = 40) ) {
            repository.getAllGames()
        }.flow
            .cachedIn(viewModelScope)
    }

    fun refreshLayout(){
        fetchAllGames()
    }

}