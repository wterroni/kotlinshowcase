package com.example.kotlinshowcase.feature.amiibo.repository

import com.example.kotlinshowcase.feature.amiibo.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.service.AmiiboService

class AmiiboRepository(
    private val service: AmiiboService
) {
    suspend fun getAmiibos(): List<Amiibo> {
        return service.getAmiibos()
    }
    
    suspend fun searchAmiibos(query: String): List<Amiibo> {
        return if (query.isBlank()) {
            emptyList()
        } else {
            service.searchAmiibos(query)
        }
    }
}
