package com.example.gamesapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesapp.data.repository.RawgRepository
import com.example.gamesapp.domain.model.GamesItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: RawgRepository) : ViewModel() {

    // LiveData to observe the state of the response from the repository and update the UI
    private val _favoriteGames: MutableLiveData<List<GamesItem>> = MutableLiveData()
    val favoriteGames: LiveData<List<GamesItem>> = _favoriteGames

    init {
        fetchFavoriteGames()
    }

    // Fetch favorite games from the repository (database) and update LiveData with the result
    fun fetchFavoriteGames() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllGamesFromDB()
                .catch { _favoriteGames.postValue(emptyList()) }
                .collect { response ->
                    _favoriteGames.postValue(response)
                }
        }
    }

}