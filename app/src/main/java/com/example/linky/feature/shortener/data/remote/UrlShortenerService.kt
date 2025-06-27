package com.example.linky.feature.shortener.data.remote

import com.example.linky.feature.shortener.domain.model.GetUrlResponse
import com.example.linky.feature.shortener.domain.model.ShortenUrlRequest
import com.example.linky.feature.shortener.domain.model.ShortenUrlResponse

/**
 * Interface para o serviço de encurtamento de URLs
 */
interface UrlShortenerService {
    
    /**
     * Encurta uma URL
     * @param request A requisição contendo a URL a ser encurtada
     * @return A resposta da API com o alias e links
     */
    suspend fun shortenUrl(request: ShortenUrlRequest): ShortenUrlResponse
    
    /**
     * Recupera a URL original a partir de um alias
     * @param alias O alias da URL encurtada
     * @return A resposta contendo a URL original
     */
    suspend fun getOriginalUrl(alias: String): GetUrlResponse
}
