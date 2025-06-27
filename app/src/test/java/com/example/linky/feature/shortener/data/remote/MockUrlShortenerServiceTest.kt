package com.example.linky.feature.shortener.data.remote

import com.example.linky.feature.shortener.domain.model.ShortenUrlRequest
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.NoSuchElementException

class MockUrlShortenerServiceTest {
    
    private lateinit var mockService: MockUrlShortenerService
    
    @Before
    fun setup() {
        mockService = MockUrlShortenerService()
    }
    
    @Test
    fun `shortenUrl should generate alias and return response`() = runBlocking {
        // Given
        val request = ShortenUrlRequest("https://example.com")
        
        // When
        val response = mockService.shortenUrl(request)
        
        // Then
        assertNotNull(response.alias)
        assertEquals(6, response.alias.length)
        assertEquals("https://sou.nu/api/alias/${response.alias}", response._links.self)
        assertEquals("https://sou.nu/${response.alias}", response._links.short)
    }
    
    @Test
    fun `getOriginalUrl should return URL for valid alias`() = runBlocking {
        // Given
        val request = ShortenUrlRequest("https://example.com")
        val shortenResponse = mockService.shortenUrl(request)
        val alias = shortenResponse.alias
        
        // When
        val response = mockService.getOriginalUrl(alias)
        
        // Then
        assertEquals("https://example.com", response.url)
    }
    
    @Test(expected = NoSuchElementException::class)
    fun `getOriginalUrl should throw exception when alias not found`() = runBlocking {
        // Given
        val nonExistentAlias = "nonexistent"
        
        // When - This should throw NoSuchElementException
        mockService.getOriginalUrl(nonExistentAlias)
        
        // Then - The test will pass if an exception is thrown
    }
    
    @Test
    fun `multiple shortenUrl calls should generate different aliases`() = runBlocking {
        // Given
        val request1 = ShortenUrlRequest("https://example.com")
        val request2 = ShortenUrlRequest("https://another-example.com")
        
        // When
        val response1 = mockService.shortenUrl(request1)
        val response2 = mockService.shortenUrl(request2)
        
        // Then
        assertNotEquals(response1.alias, response2.alias)
    }
}
