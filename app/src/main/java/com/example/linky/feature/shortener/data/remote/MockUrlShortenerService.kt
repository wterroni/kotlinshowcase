package com.example.linky.feature.shortener.data.remote

import com.example.linky.feature.shortener.domain.model.GetUrlResponse
import com.example.linky.feature.shortener.domain.model.ShortenUrlRequest
import com.example.linky.feature.shortener.domain.model.ShortenUrlResponse
import com.example.linky.feature.shortener.domain.model.UrlLinks
import kotlinx.coroutines.delay
import java.util.*

/**
 * Mock implementation of the URL shortener service for when the real API is unavailable
 */
class MockUrlShortenerService : UrlShortenerService {
    
    companion object {
        private const val BASE_URL = "https://sou.nu"
        private const val ALIAS_LENGTH = 6
        private const val SHORTEN_DELAY_MS = 500L
        private const val GET_URL_DELAY_MS = 300L
    }
    
    private val urlMap = mutableMapOf<String, String>()
    
    override suspend fun shortenUrl(request: ShortenUrlRequest): ShortenUrlResponse {
        // Simulate network delay
        delay(SHORTEN_DELAY_MS)
        
        // Generate a random alias
        val alias = generateRandomAlias()
        
        // Store the URL mapping
        urlMap[alias] = request.url
        
        // Create the response
        return ShortenUrlResponse(
            alias = alias,
            _links = UrlLinks(
                self = "$BASE_URL/api/alias/$alias",
                short = "$BASE_URL/$alias"
            )
        )
    }
    
    override suspend fun getOriginalUrl(alias: String): GetUrlResponse {
        // Simulate network delay
        delay(GET_URL_DELAY_MS)
        
        // Get the original URL or throw an exception if not found
        val originalUrl = urlMap[alias] ?: throw NoSuchElementException("URL not found for alias: $alias")
        
        return GetUrlResponse(url = originalUrl)
    }
    
    /**
     * Generates a random alias with the specified length
     */
    private fun generateRandomAlias(): String {
        return UUID.randomUUID().toString().substring(0, ALIAS_LENGTH)
    }
}
