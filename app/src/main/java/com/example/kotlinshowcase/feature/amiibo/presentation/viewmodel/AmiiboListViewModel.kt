package com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.domain.repository.AmiiboRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AmiiboListState {
    data object Loading : AmiiboListState()
    data class Success(val amiibos: List<Amiibo>) : AmiiboListState()
    data class Error(val message: String) : AmiiboListState()
}

class AmiiboListViewModel(
    private val repository: AmiiboRepository
) : ViewModel() {

    private val _state = MutableStateFlow<AmiiboListState>(AmiiboListState.Loading)
    val state: StateFlow<AmiiboListState> = _state.asStateFlow()

    private var currentAmiibos: List<Amiibo> = emptyList()

    init {
        loadAmiibos()
    }

    fun loadAmiibos() {
        viewModelScope.launch {
            _state.value = AmiiboListState.Loading
            try {
                currentAmiibos = repository.getAmiibos()
                _state.value = AmiiboListState.Success(currentAmiibos)
            } catch (e: Exception) {
                _state.value = AmiiboListState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun searchAmiibos(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _state.value = AmiiboListState.Success(currentAmiibos)
            } else {
                try {
                    val results = repository.searchAmiibos(query)
                    _state.value = AmiiboListState.Success(results)
                } catch (e: Exception) {
                    _state.value = AmiiboListState.Error(e.message ?: "Search error")
                }
            }
        }
    }
}
