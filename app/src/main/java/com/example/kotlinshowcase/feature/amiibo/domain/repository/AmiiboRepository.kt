package com.example.kotlinshowcase.feature.amiibo.domain.repository

import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo

interface AmiiboRepository {
    suspend fun getAmiibos(): List<Amiibo>
    suspend fun searchAmiibos(query: String): List<Amiibo>
}
