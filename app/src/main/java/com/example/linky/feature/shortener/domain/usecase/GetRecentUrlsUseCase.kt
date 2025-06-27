package com.example.linky.feature.shortener.domain.usecase

import com.example.linky.feature.shortener.domain.model.ShortUrl
import com.example.linky.feature.shortener.domain.repository.UrlShortenerRepository
import kotlinx.coroutines.flow.Flow

/**
 * Caso de uso para obter as URLs recentemente encurtadas
 */
class GetRecentUrlsUseCase(private val repository: UrlShortenerRepository) {
    
    /**
     * Obtém o fluxo de URLs recentemente encurtadas
     * @return Um fluxo de lista de URLs encurtadas
     */
    operator fun invoke(): Flow<List<ShortUrl>> {
        return repository.getRecentUrls()
    }
}
