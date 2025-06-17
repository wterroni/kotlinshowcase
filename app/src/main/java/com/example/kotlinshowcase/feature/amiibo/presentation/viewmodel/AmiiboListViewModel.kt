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
import kotlinx.coroutines.flow.map

class AmiiboListViewModel(
    private val repository: AmiiboRepository
) : ViewModel() {
    
    private val pager = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false,
            initialLoadSize = 10
        ),
        pagingSourceFactory = { AmiiboPagingSource(repository) }
    )

    fun getAmiibos(query: String = ""): Flow<PagingData<Amiibo>> {
        return if (query.isBlank()) {
            pager.flow.cachedIn(viewModelScope)
        } else {
            pager.flow.map { pagingData ->
                pagingData.filter { amiibo ->
                    amiibo.name.contains(query, ignoreCase = true) ||
                            amiibo.character.contains(query, ignoreCase = true) ||
                            amiibo.gameSeries.contains(query, ignoreCase = true)
                }
            }.cachedIn(viewModelScope)
        }
    }

    fun refresh() {

    }
}
