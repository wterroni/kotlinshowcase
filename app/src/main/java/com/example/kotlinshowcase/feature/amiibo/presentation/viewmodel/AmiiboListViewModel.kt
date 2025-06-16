package com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.domain.repository.AmiiboRepository
import com.example.kotlinshowcase.feature.amiibo.paging.AmiiboPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AmiiboListViewModel(
    private val repository: AmiiboRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _amiibos = MutableStateFlow<PagingData<Amiibo>>(PagingData.empty())
    val amiibos: StateFlow<PagingData<Amiibo>> = _amiibos.asStateFlow()

    init {
        loadAllAmiibos()
    }

    private fun loadAllAmiibos() {
        viewModelScope.launch {
            Pager(
                config = PagingConfig(
                    pageSize = 100,
                    enablePlaceholders = false,
                    initialLoadSize = 200
                ),
                pagingSourceFactory = { AmiiboPagingSource(repository) }
            ).flow
                .cachedIn(viewModelScope)
                .collect { pagingData ->
                    _amiibos.value = pagingData
                }
        }
    }

    fun getAmiibos(query: String = ""): Flow<PagingData<Amiibo>> {
        _searchQuery.value = query

        return if (query.isBlank()) {
            amiibos
        } else {
            amiibos.map { pagingData ->
                pagingData.filter { amiibo ->
                    amiibo.name.contains(query, ignoreCase = true) ||
                            amiibo.character.contains(query, ignoreCase = true) ||
                            amiibo.gameSeries.contains(query, ignoreCase = true)
                }
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
    
    fun refresh() {
        loadAllAmiibos()
    }
}
