package com.example.gamesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.gamesapp.repository.RawgRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val repository: RawgRepository) : ViewModel() {

    // flow for paging games list from repository
    val allGames = Pager( PagingConfig(pageSize = 40) ) {
        repository.getAllGames()
    }.flow
        .cachedIn(viewModelScope)

}