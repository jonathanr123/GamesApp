package com.example.gamesapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesapp.model.Creators
import com.example.gamesapp.model.Games
import com.example.gamesapp.model.Genres
import com.example.gamesapp.repository.RawgRepository
import com.example.gamesapp.utils.RawgApiResult
import com.example.gamesapp.utils.RawgData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: RawgRepository) : ViewModel() {

    private val _creators: MutableLiveData<RawgApiResult<RawgData<List<Creators>>>> =
        MutableLiveData(RawgApiResult.loading())
    val creators: LiveData<RawgApiResult<RawgData<List<Creators>>>> = _creators

    private val _genres: MutableLiveData<RawgApiResult<RawgData<List<Genres>>>> =
        MutableLiveData(RawgApiResult.loading())
    val genres: LiveData<RawgApiResult<RawgData<List<Genres>>>> = _genres

    private val _gamesPopular: MutableLiveData<RawgApiResult<RawgData<List<Games>>>> =
        MutableLiveData(RawgApiResult.loading())
    val gamesPopular: LiveData<RawgApiResult<RawgData<List<Games>>>> = _gamesPopular

    private val _gamesTrending: MutableLiveData<RawgApiResult<RawgData<List<Games>>>> =
        MutableLiveData(RawgApiResult.loading())
    val gamesTrending: LiveData<RawgApiResult<RawgData<List<Games>>>> = _gamesTrending

    init {
        fetchCreators()
        fetchGenres()
        fetchGamesPopular()
        fetchGamesTrending()
    }

    private fun fetchCreators() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getListOfGameCreators()
                .catch { throwable ->
                    _creators.postValue(RawgApiResult.errorThrowable(Exception(throwable.cause))) }
                .collect { response ->
                    when (response) {
                        is RawgApiResult.Success ->
                        {delay(2000)
                            _creators.postValue(RawgApiResult.success(response.data))}

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

    private fun fetchGenres() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getListOfGenres()
                .catch { throwable ->
                    _genres.postValue(RawgApiResult.errorThrowable(Exception(throwable.cause))) }
                .collect { response ->
                    when (response) {
                        is RawgApiResult.Success ->
                        {delay(2000)
                            _genres.postValue(RawgApiResult.success(response.data))}

                        is RawgApiResult.Failure ->
                            _genres.postValue(RawgApiResult.failure(response.statusCode))

                        is RawgApiResult.ErrorThrowable ->
                            _genres.postValue(RawgApiResult.errorThrowable(response.errorThrowable))

                        is RawgApiResult.Loading ->
                            _genres.postValue(RawgApiResult.loading())
                    }
                }
        }
    }

    private fun fetchGamesPopular() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getListOfGamePopular()
                .catch { throwable ->
                    _gamesPopular.postValue(RawgApiResult.errorThrowable(Exception(throwable.cause))) }
                .collect { response ->
                    when (response) {
                        is RawgApiResult.Success ->
                        {delay(2000)
                            _gamesPopular.postValue(RawgApiResult.success(response.data))}

                        is RawgApiResult.Failure ->
                            _gamesPopular.postValue(RawgApiResult.failure(response.statusCode))

                        is RawgApiResult.ErrorThrowable ->
                            _gamesPopular.postValue(RawgApiResult.errorThrowable(response.errorThrowable))

                        is RawgApiResult.Loading ->
                            _gamesPopular.postValue(RawgApiResult.loading())
                    }
                }
        }
    }

    private fun fetchGamesTrending() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getListOfGameTrending()
                .catch { throwable ->
                    _gamesTrending.postValue(RawgApiResult.errorThrowable(Exception(throwable.cause))) }
                .collect { response ->
                    when (response) {
                        is RawgApiResult.Success ->
                        {delay(2000)
                            _gamesTrending.postValue(RawgApiResult.success(response.data))}

                        is RawgApiResult.Failure ->
                            _gamesTrending.postValue(RawgApiResult.failure(response.statusCode))

                        is RawgApiResult.ErrorThrowable ->
                            _gamesTrending.postValue(RawgApiResult.errorThrowable(response.errorThrowable))

                        is RawgApiResult.Loading ->
                            _gamesTrending.postValue(RawgApiResult.loading())
                    }
                }
        }
    }

}