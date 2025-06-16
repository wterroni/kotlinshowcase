package com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.domain.repository.AmiiboRepository
import com.example.kotlinshowcase.feature.amiibo.paging.AmiiboPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AmiiboListViewModel(
    private val repository: AmiiboRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private var currentAmiibos: Flow<PagingData<Amiibo>>? = null
    
    fun getAmiibos(query: String = ""): Flow<PagingData<Amiibo>> {
        val lastAmiibos = currentAmiibos
        if (query == _searchQuery.value && lastAmiibos != null) {
            return lastAmiibos
        }
        
        _searchQuery.value = query
        
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 40
            ),
            pagingSourceFactory = { AmiiboPagingSource(repository, query.ifEmpty { null }) }
        )
            .flow
            .cachedIn(viewModelScope)
            .also { currentAmiibos = it }
    }
    
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
    
    /**
     * Notifies the ViewModel that the error has been shown to the user
     * and can be cleared.
     */
    fun onErrorShown() {
        // This method is called when the error is shown to the user
        // and can be used to clear any error state if needed
    }
}
