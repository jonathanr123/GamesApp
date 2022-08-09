package com.example.gamesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesapp.model.Creators
import com.example.gamesapp.model.GameSingle
import com.example.gamesapp.model.Games
import com.example.gamesapp.repository.RawgRepository
import com.example.gamesapp.utils.RawgApiResult
import com.example.gamesapp.utils.RawgData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
}