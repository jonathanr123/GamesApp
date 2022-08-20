package com.example.gamesapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.gamesapp.data.repository.RawgRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(private val repository: RawgRepository) : ViewModel() {
    // TODO: Implement the ViewModel
}