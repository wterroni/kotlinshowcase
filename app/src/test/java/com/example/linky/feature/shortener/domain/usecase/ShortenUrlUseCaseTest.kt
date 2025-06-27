package com.example.linky.feature.shortener.domain.usecase

import com.example.linky.feature.shortener.domain.model.ShortUrl
import com.example.linky.feature.shortener.domain.model.ShortenUrlRequest
import com.example.linky.feature.shortener.domain.repository.UrlShortenerRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ShortenUrlUseCaseTest {
    
    private lateinit var repository: UrlShortenerRepository
    private lateinit var shortenUrlUseCase: ShortenUrlUseCase
    
    @Before
    fun setup() {
        repository = mockk()
        shortenUrlUseCase = ShortenUrlUseCase(repository)
    }
    
    @Test
    fun `when url is empty, should return error`() = runTest {
        // Given
        val emptyUrl = ""
        
        // When
        val result = shortenUrlUseCase(emptyUrl)
        
        // Then
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalArgumentException)
    }
    
    @Test
    fun `when url does not have protocol, should add https protocol`() = runTest {
        // Given
        val urlWithoutProtocol = "example.com"
        val expectedUrl = "https://example.com"
        val mockShortUrl = ShortUrl(
            originalUrl = expectedUrl,
            alias = "abc123",
            shortUrl = "https://sou.nu/abc123"
        )
        
        coEvery { repository.shortenUrl(ShortenUrlRequest(expectedUrl)) } returns Result.success(mockShortUrl)
        
        // When
        val result = shortenUrlUseCase(urlWithoutProtocol)
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(mockShortUrl, result.getOrNull())
        coVerify { repository.shortenUrl(ShortenUrlRequest(expectedUrl)) }
    }
    
    @Test
    fun `when url has http protocol, should keep it`() = runTest {
        // Given
        val urlWithHttp = "http://example.com"
        val mockShortUrl = ShortUrl(
            originalUrl = urlWithHttp,
            alias = "abc123",
            shortUrl = "https://sou.nu/abc123"
        )
        
        coEvery { repository.shortenUrl(ShortenUrlRequest(urlWithHttp)) } returns Result.success(mockShortUrl)
        
        // When
        val result = shortenUrlUseCase(urlWithHttp)
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(mockShortUrl, result.getOrNull())
        coVerify { repository.shortenUrl(ShortenUrlRequest(urlWithHttp)) }
    }
    
    @Test
    fun `when repository returns error, should propagate error`() = runTest {
        // Given
        val url = "https://example.com"
        val expectedException = Exception("API Error")
        
        coEvery { repository.shortenUrl(ShortenUrlRequest(url)) } returns Result.failure(expectedException)
        
        // When
        val result = shortenUrlUseCase(url)
        
        // Then
        assertTrue(result.isFailure)
        assertEquals(expectedException, result.exceptionOrNull())
    }
}
