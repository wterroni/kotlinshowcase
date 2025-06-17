package com.example.kotlinshowcase.feature.amiibo.data.remote

import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.domain.model.AmiiboResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

interface AmiiboService {
    suspend fun getAmiibos(): List<Amiibo>
    suspend fun searchAmiibos(name: String): List<Amiibo>
}

class AmiiboServiceImpl(
    private val client: HttpClient
) : AmiiboService {
    
    override suspend fun getAmiibos(): List<Amiibo> {
        return client.get("https://www.amiiboapi.com/api/amiibo/").body<AmiiboResponse>().amiibo
    }
    
    override suspend fun searchAmiibos(name: String): List<Amiibo> {
        if (name.isBlank()) {
            return emptyList()
        }
        return client.get("https://www.amiiboapi.com/api/amiibo/?name=${name.lowercase()}")
            .body<AmiiboResponse>().amiibo
    }
}
