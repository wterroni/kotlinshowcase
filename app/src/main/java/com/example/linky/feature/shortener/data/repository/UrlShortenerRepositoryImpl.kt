package com.example.linky.feature.shortener.data.repository

import com.example.linky.feature.shortener.data.remote.UrlShortenerService
import com.example.linky.feature.shortener.domain.model.ShortUrl
import com.example.linky.feature.shortener.domain.model.ShortenUrlRequest
import com.example.linky.feature.shortener.domain.repository.UrlShortenerRepository
import io.ktor.client.plugins.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Implementação do repositório de encurtamento de URLs
 */
class UrlShortenerRepositoryImpl(
    private val service: UrlShortenerService
) : UrlShortenerRepository {
    
    // Lista em memória das URLs recentemente encurtadas
    private val _recentUrls = MutableStateFlow<List<ShortUrl>>(emptyList())
    
    override suspend fun shortenUrl(request: ShortenUrlRequest): Result<ShortUrl> {
        return try {
            val response = service.shortenUrl(request)
            val shortUrl = ShortUrl(
                originalUrl = request.url,
                alias = response.alias,
                shortUrl = response._links.short
            )
            
            // Adiciona a nova URL ao início da lista
            val updatedList = listOf(shortUrl) + _recentUrls.value
            _recentUrls.emit(updatedList)
            
            Result.success(shortUrl)
        } catch (e: ClientRequestException) {
            Result.failure(Exception("Erro ao encurtar URL: ${e.message}"))
        } catch (e: Exception) {
            Result.failure(Exception("Erro inesperado: ${e.message}"))
        }
    }
    
    override suspend fun getOriginalUrl(alias: String): Result<String> {
        return try {
            val response = service.getOriginalUrl(alias)
            Result.success(response.url)
        } catch (e: ClientRequestException) {
            if (e.response.status.value == 404) {
                Result.failure(Exception("URL não encontrada"))
            } else {
                Result.failure(Exception("Erro ao buscar URL: ${e.message}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro inesperado: ${e.message}"))
        }
    }
    
    override fun getRecentUrls(): Flow<List<ShortUrl>> {
        return _recentUrls.asStateFlow()
    }
}
