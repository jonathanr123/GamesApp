package com.example.gamesapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesapp.data.model.Creators
import com.example.gamesapp.data.model.GameSingle
import com.example.gamesapp.data.model.Games
import com.example.gamesapp.data.repository.RawgRepository
import com.example.gamesapp.utils.RawgApiResult
import com.example.gamesapp.utils.RawgData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class GameDetailViewModel @Inject constructor(private val repository: RawgRepository) : ViewModel() {

    private val _gameSelected: MutableLiveData<Games?> = MutableLiveData()
    val gameSelected: LiveData<Games?> = _gameSelected

    private val _gameDetail: MutableLiveData<RawgApiResult<GameSingle>> =
        MutableLiveData(RawgApiResult.loading())
    val gameDetail: LiveData<RawgApiResult<GameSingle>> = _gameDetail

    private val _creators: MutableLiveData<RawgApiResult<RawgData<List<Creators>>>> =
        MutableLiveData(RawgApiResult.loading())
    val creators: LiveData<RawgApiResult<RawgData<List<Creators>>>> = _creators

    private val _isFavorite: MutableLiveData<Boolean> = MutableLiveData()
    val isFavorite: LiveData<Boolean> = _isFavorite

    // set game selected to live data
    fun setGameSelected(game: Games? = null) {
        _gameSelected.value = game
    }

    // Fetch Game by Id from the repository and update live data with the result
    fun fetchGameById(id: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getGameById(id)
                .catch { throwable ->
                    _gameDetail.postValue(RawgApiResult.errorThrowable(Exception(throwable.cause))) }
                .collect { response ->
                    when (response) {
                        is RawgApiResult.Success ->
                            _gameDetail.postValue(RawgApiResult.success(response.data))

                        is RawgApiResult.Failure ->
                            _gameDetail.postValue(RawgApiResult.failure(response.statusCode))

                        is RawgApiResult.ErrorThrowable ->
                            _gameDetail.postValue(RawgApiResult.errorThrowable(response.errorThrowable))

                        is RawgApiResult.Loading ->
                            _gameDetail.postValue(RawgApiResult.loading())
                    }
                }
        }
    }

    // Fetch creators (development team) from the repository and update live data with the result
    fun fetchCreators(id: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getListOfGameCreators(id)
                .catch { throwable ->
                    _creators.postValue(RawgApiResult.errorThrowable(Exception(throwable.cause))) }
                .collect { response ->
                    when (response) {
                        is RawgApiResult.Success ->
                            _creators.postValue(RawgApiResult.success(response.data))

                        is RawgApiResult.Failure ->
                            _creators.postValue(RawgApiResult.failure(response.statusCode))

                        is RawgApiResult.ErrorThrowable ->
                            _creators.postValue(RawgApiResult.errorThrowable(response.errorThrowable))

                        is RawgApiResult.Loading ->
                            _creators.postValue(RawgApiResult.loading())
                    }
                }
        }
    }

    // Insert favorite game into the repository (database)
    fun insertFavoriteGame() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.postGameToDB(gameSelected.value as Games)
        }
    }

    // Delete favorite game from the repository (database)
    fun deleteFavoriteGame() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteGameFromDB(gameSelected.value as Games)
        }
    }

    // Fetch favorite games from the repository (database) and update LiveData with the result
    fun isFavoriteGame() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllGamesFromDB()
                .catch { _isFavorite.postValue(false) }
                .collect { response ->
                    // Verify if the game selected is favorite list in the DB
                    if (response.contains(gameSelected.value?.toDomain())) {
                        _isFavorite.postValue(true)
                    } else {
                        _isFavorite.postValue(false)
                    }
                }
        }
    }
}