package com.example.linky.feature.shortener.domain.usecase

import com.example.linky.feature.shortener.domain.model.ShortUrl
import com.example.linky.feature.shortener.domain.model.ShortenUrlRequest
import com.example.linky.feature.shortener.domain.repository.UrlShortenerRepository

/**
 * Caso de uso para encurtar uma URL
 */
class ShortenUrlUseCase(private val repository: UrlShortenerRepository) {
    
    /**
     * Encurta uma URL
     * @param url A URL a ser encurtada
     * @return Resultado contendo a URL encurtada ou um erro
     */
    suspend operator fun invoke(url: String): Result<ShortUrl> {
        // Validação básica da URL
        if (url.isBlank()) {
            return Result.failure(IllegalArgumentException("URL não pode estar vazia"))
        }
        
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            // Adiciona https:// se não tiver um protocolo
            val formattedUrl = "https://$url"
            return repository.shortenUrl(ShortenUrlRequest(formattedUrl))
        }
        
        return repository.shortenUrl(ShortenUrlRequest(url))
    }
}
