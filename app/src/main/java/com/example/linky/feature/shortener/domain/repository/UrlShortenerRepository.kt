package com.example.linky.feature.shortener.domain.repository

import com.example.linky.feature.shortener.domain.model.ShortUrl
import com.example.linky.feature.shortener.domain.model.ShortenUrlRequest
import com.example.linky.feature.shortener.domain.model.ShortenUrlResponse
import kotlinx.coroutines.flow.Flow

/**
 * Interface para o repositório de encurtamento de URLs
 */
interface UrlShortenerRepository {
    
    /**
     * Encurta uma URL
     * @param request A requisição contendo a URL a ser encurtada
     * @return A resposta da API com o alias e links
     */
    suspend fun shortenUrl(request: ShortenUrlRequest): Result<ShortUrl>
    
    /**
     * Recupera a URL original a partir de um alias
     * @param alias O alias da URL encurtada
     * @return A URL original
     */
    suspend fun getOriginalUrl(alias: String): Result<String>
    
    /**
     * Obtém o fluxo de URLs recentemente encurtadas
     * @return Um fluxo de lista de URLs encurtadas
     */
    fun getRecentUrls(): Flow<List<ShortUrl>>
}
