package com.example.linky.feature.shortener.data.remote

import com.example.linky.feature.shortener.domain.model.GetUrlResponse
import com.example.linky.feature.shortener.domain.model.ShortenUrlRequest
import com.example.linky.feature.shortener.domain.model.ShortenUrlResponse
import com.example.linky.feature.shortener.domain.model.UrlLinks
import com.example.linky.util.MockLogRule
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FallbackUrlShortenerServiceTest {
    
    @get:Rule
    val mockLogRule = MockLogRule()
    
    private lateinit var realService: UrlShortenerServiceImpl
    private lateinit var mockService: MockUrlShortenerService
    private lateinit var fallbackService: FallbackUrlShortenerService
    
    private val testRequest = ShortenUrlRequest("https://example.com")
    private val testResponse = ShortenUrlResponse(
        alias = "abc123",
        _links = UrlLinks(
            self = "https://example.com",
            short = "https://sou.nu/abc123"
        )
    )
    private val testGetResponse = GetUrlResponse("https://example.com")
    
    @Before
    fun setup() {
        realService = mockk()
        mockService = mockk()
        fallbackService = FallbackUrlShortenerService(realService, mockService)
    }
    
    @Test
    fun `shortenUrl should use real service when it succeeds`() = runTest {
        // Given
        coEvery { realService.shortenUrl(testRequest) } returns testResponse
        
        // When
        val result = fallbackService.shortenUrl(testRequest)
        
        // Then
        assertEquals(testResponse, result)
        coVerify { realService.shortenUrl(testRequest) }
        coVerify(exactly = 0) { mockService.shortenUrl(any()) }
    }
    
    @Test
    fun `shortenUrl should fall back to mock service when real service fails`() = runTest {
        // Given
        coEvery { realService.shortenUrl(testRequest) } throws RuntimeException("API Error")
        coEvery { mockService.shortenUrl(testRequest) } returns testResponse
        
        // When
        val result = fallbackService.shortenUrl(testRequest)
        
        // Then
        assertEquals(testResponse, result)
        coVerify { realService.shortenUrl(testRequest) }
        coVerify { mockService.shortenUrl(testRequest) }
    }
    
    @Test
    fun `getOriginalUrl should use real service when it succeeds`() = runTest {
        // Given
        val alias = "abc123"
        coEvery { realService.getOriginalUrl(alias) } returns testGetResponse
        
        // When
        val result = fallbackService.getOriginalUrl(alias)
        
        // Then
        assertEquals(testGetResponse, result)
        coVerify { realService.getOriginalUrl(alias) }
        coVerify(exactly = 0) { mockService.getOriginalUrl(any()) }
    }
    
    @Test
    fun `getOriginalUrl should fall back to mock service when real service fails`() = runTest {
        // Given
        val alias = "abc123"
        coEvery { realService.getOriginalUrl(alias) } throws RuntimeException("API Error")
        coEvery { mockService.getOriginalUrl(alias) } returns testGetResponse
        
        // When
        val result = fallbackService.getOriginalUrl(alias)
        
        // Then
        assertEquals(testGetResponse, result)
        coVerify { realService.getOriginalUrl(alias) }
        coVerify { mockService.getOriginalUrl(alias) }
    }
}
