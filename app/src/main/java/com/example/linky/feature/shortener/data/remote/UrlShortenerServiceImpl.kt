package com.example.linky.feature.shortener.data.remote

import com.example.linky.feature.shortener.domain.model.GetUrlResponse
import com.example.linky.feature.shortener.domain.model.ShortenUrlRequest
import com.example.linky.feature.shortener.domain.model.ShortenUrlResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

/**
 * Implementação do serviço de encurtamento de URLs usando Ktor Client
 */
class UrlShortenerServiceImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String = "https://url-shortener-nu.herokuapp.com"
) : UrlShortenerService {
    
    override suspend fun shortenUrl(request: ShortenUrlRequest): ShortenUrlResponse {
        return httpClient.post {
            url("$baseUrl/api/alias")
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
    
    override suspend fun getOriginalUrl(alias: String): GetUrlResponse {
        return httpClient.get {
            url("$baseUrl/api/alias/$alias")
        }.body()
    }
}
