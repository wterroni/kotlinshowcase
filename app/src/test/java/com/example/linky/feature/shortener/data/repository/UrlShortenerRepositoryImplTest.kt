package com.example.linky.feature.shortener.data.repository

import app.cash.turbine.test
import com.example.linky.feature.shortener.data.remote.UrlShortenerService
import com.example.linky.feature.shortener.domain.model.GetUrlResponse
import com.example.linky.feature.shortener.domain.model.ShortenUrlRequest
import com.example.linky.feature.shortener.domain.model.ShortenUrlResponse
import com.example.linky.feature.shortener.domain.model.UrlLinks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class UrlShortenerRepositoryImplTest {
    
    private lateinit var service: UrlShortenerService
    private lateinit var repository: UrlShortenerRepositoryImpl
    
    @Before
    fun setup() {
        service = mockk()
        repository = UrlShortenerRepositoryImpl(service)
    }
    
    @Test
    fun `shortenUrl should return success result when service succeeds`() = runTest {
        // Given
        val request = ShortenUrlRequest("https://example.com")
        val response = ShortenUrlResponse(
            alias = "abc123",
            _links = UrlLinks(
                self = "https://example.com",
                short = "https://sou.nu/abc123"
            )
        )
        
        coEvery { service.shortenUrl(request) } returns response
        
        // When
        val result = repository.shortenUrl(request)
        
        // Then
        assertTrue(result.isSuccess)
        val shortUrl = result.getOrNull()
        assertEquals("https://example.com", shortUrl?.originalUrl)
        assertEquals("abc123", shortUrl?.alias)
        assertEquals("https://sou.nu/abc123", shortUrl?.shortUrl)
    }
    
    @Test
    fun `shortenUrl should return failure result when service throws exception`() = runTest {
        // Given
        val request = ShortenUrlRequest("https://example.com")
        val exception = IOException("Network error")
        
        coEvery { service.shortenUrl(request) } throws exception
        
        // When
        val result = repository.shortenUrl(request)
        
        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("Erro inesperado") == true)
    }
    
    @Test
    fun `getOriginalUrl should return success result when service succeeds`() = runTest {
        // Given
        val alias = "abc123"
        val response = GetUrlResponse("https://example.com")
        
        coEvery { service.getOriginalUrl(alias) } returns response
        
        // When
        val result = repository.getOriginalUrl(alias)
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals("https://example.com", result.getOrNull())
    }
    
    @Test
    fun `getRecentUrls should emit updated list after shortenUrl`() = runTest {
        // Given
        val request = ShortenUrlRequest("https://example.com")
        val response = ShortenUrlResponse(
            alias = "abc123",
            _links = UrlLinks(
                self = "https://example.com",
                short = "https://sou.nu/abc123"
            )
        )
        
        coEvery { service.shortenUrl(request) } returns response
        
        // When & Then
        repository.getRecentUrls().test {

            assertTrue(awaitItem().isEmpty())
            
            // After shortening URL
            repository.shortenUrl(request)
            
            val recentUrls = awaitItem()
            assertEquals(1, recentUrls.size)
            assertEquals("https://example.com", recentUrls[0].originalUrl)
            assertEquals("abc123", recentUrls[0].alias)
            assertEquals("https://sou.nu/abc123", recentUrls[0].shortUrl)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}
