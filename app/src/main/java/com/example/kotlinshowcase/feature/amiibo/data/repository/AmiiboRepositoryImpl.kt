package com.example.kotlinshowcase.feature.amiibo.data.repository

import com.example.kotlinshowcase.feature.amiibo.data.remote.AmiiboService
import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.domain.model.AmiiboResponse
import com.example.kotlinshowcase.feature.amiibo.domain.repository.AmiiboRepository

class AmiiboRepositoryImpl(
    private val service: AmiiboService
) : AmiiboRepository {
    
    override suspend fun getAmiibos(): List<Amiibo> {
        return service.getAmiibos()
    }
    
    override suspend fun searchAmiibos(query: String): List<Amiibo> {
        return if (query.isBlank()) {
            emptyList()
        } else {
            service.searchAmiibos(query)
        }
    }
}
