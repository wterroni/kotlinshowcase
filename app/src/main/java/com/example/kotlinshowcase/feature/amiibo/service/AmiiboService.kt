package com.example.kotlinshowcase.feature.amiibo.service

import com.example.kotlinshowcase.feature.amiibo.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.model.AmiiboResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class AmiiboService(
    private val client: HttpClient
) {
    suspend fun getAmiibos(): List<Amiibo> {
        return client.get("https://www.amiiboapi.com/api/amiibo/").body<AmiiboResponse>().amiibo
    }
    
    suspend fun searchAmiibos(name: String): List<Amiibo> {
        return client.get("https://www.amiiboapi.com/api/amiibo/?name=${name.lowercase()}")
            .body<AmiiboResponse>().amiibo
    }
}
