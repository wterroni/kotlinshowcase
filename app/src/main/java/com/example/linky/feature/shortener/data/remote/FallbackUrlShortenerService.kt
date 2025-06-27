package com.example.linky.feature.shortener.data.remote

import android.util.Log
import com.example.linky.feature.shortener.domain.model.GetUrlResponse
import com.example.linky.feature.shortener.domain.model.ShortenUrlRequest
import com.example.linky.feature.shortener.domain.model.ShortenUrlResponse

/**
 * Service implementation that tries to use the real API first and falls back to the mock
 * if the real API is unavailable or returns an error.
 */
class FallbackUrlShortenerService(
    private val realService: UrlShortenerServiceImpl,
    private val mockService: MockUrlShortenerService
) : UrlShortenerService {
    
    companion object {
        private const val TAG = "FallbackUrlService"
    }
    
    override suspend fun shortenUrl(request: ShortenUrlRequest): ShortenUrlResponse {
        return try {
            // Try to use the real service first
            Log.d(TAG, "Attempting to use real API for shortening URL: ${request.url}")
            realService.shortenUrl(request)
        } catch (e: Exception) {
            // If an error occurs, log it and use the mock service
            Log.w(TAG, "Real API failed, falling back to mock: ${e.message}")
            mockService.shortenUrl(request)
        }
    }
    
    override suspend fun getOriginalUrl(alias: String): GetUrlResponse {
        return try {
            // Try to use the real service first
            Log.d(TAG, "Attempting to use real API for getting URL with alias: $alias")
            realService.getOriginalUrl(alias)
        } catch (e: Exception) {
            // If an error occurs, log it and use the mock service
            Log.w(TAG, "Real API failed, falling back to mock: ${e.message}")
            mockService.getOriginalUrl(alias)
        }
    }
}
