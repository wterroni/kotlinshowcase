package com.example.linky.feature.shortener.domain.usecase

import app.cash.turbine.test
import com.example.linky.feature.shortener.domain.model.ShortUrl
import com.example.linky.feature.shortener.domain.repository.UrlShortenerRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetRecentUrlsUseCaseTest {
    
    private lateinit var repository: UrlShortenerRepository
    private lateinit var getRecentUrlsUseCase: GetRecentUrlsUseCase
    
    @Before
    fun setup() {
        repository = mockk()
        getRecentUrlsUseCase = GetRecentUrlsUseCase(repository)
    }
    
    @Test
    fun `invoke should return flow from repository`() = runTest {
        // Given
        val shortUrl1 = ShortUrl(
            originalUrl = "https://example.com",
            alias = "abc123",
            shortUrl = "https://sou.nu/abc123"
        )
        val shortUrl2 = ShortUrl(
            originalUrl = "https://another-example.com",
            alias = "def456",
            shortUrl = "https://sou.nu/def456"
        )
        
        val recentUrlsFlow = MutableStateFlow<List<ShortUrl>>(emptyList())
        every { repository.getRecentUrls() } returns recentUrlsFlow
        
        // When & Then
        getRecentUrlsUseCase().test {
            // Initial state
            assertEquals(emptyList<ShortUrl>(), awaitItem())
            
            // Update flow with first URL
            recentUrlsFlow.emit(listOf(shortUrl1))
            assertEquals(listOf(shortUrl1), awaitItem())
            
            // Update flow with two URLs
            recentUrlsFlow.emit(listOf(shortUrl1, shortUrl2))
            assertEquals(listOf(shortUrl1, shortUrl2), awaitItem())
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}
