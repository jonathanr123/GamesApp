package com.example.gamesapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gamesapp.core.DataStoreManager
import com.example.gamesapp.data.model.Games
import com.example.gamesapp.data.repository.RawgRepository
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
class HomeViewModel @Inject constructor(private val repository: RawgRepository, private val dataStore: DataStoreManager) : ViewModel() {

    // LiveData to observe the state of the response from the repository and update the UI
    private val _gamesPopular: MutableLiveData<RawgApiResult<RawgData<List<Games>>>> =
        MutableLiveData(RawgApiResult.loading())
    val gamesPopular: LiveData<RawgApiResult<RawgData<List<Games>>>> = _gamesPopular

    private val _gamesTrending: MutableLiveData<RawgApiResult<RawgData<List<Games>>>> =
        MutableLiveData(RawgApiResult.loading())
    val gamesTrending: LiveData<RawgApiResult<RawgData<List<Games>>>> = _gamesTrending

    private val _gamesLastYear: MutableLiveData<RawgApiResult<RawgData<List<Games>>>> =
        MutableLiveData(RawgApiResult.loading())
    val gamesLastYear: LiveData<RawgApiResult<RawgData<List<Games>>>> = _gamesLastYear

    private val _gamesTagSpecific: MutableLiveData<RawgApiResult<RawgData<List<Games>>>> =
        MutableLiveData(RawgApiResult.loading())
    val gamesTagSpecific: LiveData<RawgApiResult<RawgData<List<Games>>>> = _gamesTagSpecific

    private val _gamesPublisherSpecific: MutableLiveData<RawgApiResult<RawgData<List<Games>>>> =
        MutableLiveData(RawgApiResult.loading())
    val gamesPublisherSpecific: LiveData<RawgApiResult<RawgData<List<Games>>>> = _gamesPublisherSpecific

    private val _username: MutableLiveData<String> = MutableLiveData("")
    val username: LiveData<String> = _username

    init {
        fetchGamesPopular()
        fetchGamesTrending()
        fetchGamesLastYear()
        fetchGamesTagSpecific()
        fetchGamesPublisherSpecific()
        getUserName()
    }

    // Fetch games popular from the repository and update live data with the result
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

    // Fetch games trending from the repository and update live data with the result
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

    // Fetch games of the last year from the repository and update live data with the result
    private fun fetchGamesLastYear() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getListOfGameLastYear()
                .catch { throwable ->
                    _gamesLastYear.postValue(RawgApiResult.errorThrowable(Exception(throwable.cause))) }
                .collect { response ->
                    when (response) {
                        is RawgApiResult.Success ->
                        {delay(2000)
                            _gamesLastYear.postValue(RawgApiResult.success(response.data))}

                        is RawgApiResult.Failure ->
                            _gamesLastYear.postValue(RawgApiResult.failure(response.statusCode))

                        is RawgApiResult.ErrorThrowable ->
                            _gamesLastYear.postValue(RawgApiResult.errorThrowable(response.errorThrowable))

                        is RawgApiResult.Loading ->
                            _gamesLastYear.postValue(RawgApiResult.loading())
                    }
                }
        }
    }

    // Fetch games of the tag specific from the repository and update live data with the result
    private fun fetchGamesTagSpecific() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getListOfGameTagSpecific()
                .catch { throwable ->
                    _gamesTagSpecific.postValue(RawgApiResult.errorThrowable(Exception(throwable.cause))) }
                .collect { response ->
                    when (response) {
                        is RawgApiResult.Success ->
                        {delay(2000)
                            _gamesTagSpecific.postValue(RawgApiResult.success(response.data))}

                        is RawgApiResult.Failure ->
                            _gamesTagSpecific.postValue(RawgApiResult.failure(response.statusCode))

                        is RawgApiResult.ErrorThrowable ->
                            _gamesTagSpecific.postValue(RawgApiResult.errorThrowable(response.errorThrowable))

                        is RawgApiResult.Loading ->
                            _gamesTagSpecific.postValue(RawgApiResult.loading())
                    }
                }
        }
    }

    // Fetch games published by specific publisher from the repository and update live data with the result
    private fun fetchGamesPublisherSpecific() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getListOfGamePublisherSpecific()
                .catch { throwable ->
                    _gamesPublisherSpecific.postValue(RawgApiResult.errorThrowable(Exception(throwable.cause))) }
                .collect { response ->
                    when (response) {
                        is RawgApiResult.Success ->
                        {delay(2000)
                            _gamesPublisherSpecific.postValue(RawgApiResult.success(response.data))}

                        is RawgApiResult.Failure ->
                            _gamesPublisherSpecific.postValue(RawgApiResult.failure(response.statusCode))

                        is RawgApiResult.ErrorThrowable ->
                            _gamesPublisherSpecific.postValue(RawgApiResult.errorThrowable(response.errorThrowable))

                        is RawgApiResult.Loading ->
                            _gamesPublisherSpecific.postValue(RawgApiResult.loading())
                    }
                }
        }
    }

    // Save the username in the Datastore and call getUserName() function
    fun saveUsername(username: String){
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.saveDS("name", username)
            getUserName()
        }
    }

    // Get the username and update live data with the result
    private fun getUserName(){
        viewModelScope.launch(Dispatchers.IO) {
            val name = dataStore.getDS("name", "")
            _username.postValue(name)
        }
    }

    fun refreshLayout(){
        fetchGamesPopular()
        fetchGamesTrending()
        fetchGamesLastYear()
        fetchGamesTagSpecific()
        fetchGamesPublisherSpecific()
    }

}