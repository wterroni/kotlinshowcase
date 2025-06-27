package com.example.linky.feature.shortener.presentation.viewmodel

import app.cash.turbine.test
import com.example.linky.feature.shortener.domain.model.ShortUrl
import com.example.linky.feature.shortener.domain.usecase.GetRecentUrlsUseCase
import com.example.linky.feature.shortener.domain.usecase.ShortenUrlUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UrlShortenerViewModelTest {
    
    private lateinit var shortenUrlUseCase: ShortenUrlUseCase
    private lateinit var getRecentUrlsUseCase: GetRecentUrlsUseCase
    private lateinit var viewModel: UrlShortenerViewModel
    
    private val testDispatcher = StandardTestDispatcher()
    private val recentUrlsFlow = MutableStateFlow<List<ShortUrl>>(emptyList())
    
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        shortenUrlUseCase = mockk()
        getRecentUrlsUseCase = mockk()
        
        coEvery { getRecentUrlsUseCase() } returns recentUrlsFlow
        
        viewModel = UrlShortenerViewModel(shortenUrlUseCase, getRecentUrlsUseCase)
    }
    
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
    
    @Test
    fun `initial state should be correct`() = runTest {
        val initialState = viewModel.uiState.value
        
        assertEquals("", initialState.urlText)
        assertEquals(false, initialState.isLoading)
        assertNull(initialState.errorMessage)
        assertEquals(emptyList<ShortUrl>(), initialState.recentUrls)
        assertNull(initialState.lastShortenedUrl)
    }
    
    @Test
    fun `onUrlTextChanged should update urlText in state`() = runTest {
        // When
        viewModel.onUrlTextChanged("https://example.com")
        testDispatcher.scheduler.advanceUntilIdle()
        
        // Then
        assertEquals("https://example.com", viewModel.uiState.value.urlText)
    }
    
    @Test
    fun `shortenUrl should update state with loading and success`() = runTest {
        // Given
        val url = "https://example.com"
        val shortUrl = ShortUrl(
            originalUrl = url,
            alias = "abc123",
            shortUrl = "https://sou.nu/abc123"
        )
        
        coEvery { shortenUrlUseCase(url) } returns Result.success(shortUrl)
        
        // When & Then
        viewModel.uiState.test {
            // Initial state
            val initialState = awaitItem()
            assertEquals("", initialState.urlText)
            
            // Update URL text
            viewModel.onUrlTextChanged(url)
            val updatedTextState = awaitItem()
            assertEquals(url, updatedTextState.urlText)
            
            // Shorten URL
            viewModel.shortenUrl()
            
            // Loading state
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)
            assertEquals(url, loadingState.urlText)
            
            // Success state
            val successState = awaitItem()
            assertEquals(false, successState.isLoading)
            assertEquals("", successState.urlText) // Text field cleared
            assertEquals(shortUrl, successState.lastShortenedUrl)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `shortenUrl should update state with loading and error`() = runTest {
        // Given
        val url = "https://example.com"
        val errorMessage = "Failed to connect"
        
        coEvery { shortenUrlUseCase(url) } returns Result.failure(Exception(errorMessage))
        
        // When & Then
        viewModel.uiState.test {
            // Initial state
            val initialState = awaitItem()
            assertEquals("", initialState.urlText)
            
            // Update URL text
            viewModel.onUrlTextChanged(url)
            val updatedTextState = awaitItem()
            assertEquals(url, updatedTextState.urlText)
            
            // Shorten URL
            viewModel.shortenUrl()
            
            // Loading state
            val loadingState = awaitItem()
            assertEquals(true, loadingState.isLoading)
            
            // Error state
            val errorState = awaitItem()
            assertEquals(false, errorState.isLoading)
            assertEquals(errorMessage, errorState.errorMessage)
            assertEquals(url, errorState.urlText) // Text field not cleared on error
            
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `clearError should set errorMessage to null`() = runTest {
        // Given
        val url = "https://example.com"
        val errorMessage = "Failed to connect"
        
        coEvery { shortenUrlUseCase(url) } returns Result.failure(Exception(errorMessage))
        
        // When & Then
        viewModel.uiState.test {
            // Initial state
            val initialState = awaitItem()
            
            // Update URL text
            viewModel.onUrlTextChanged(url)
            skipItems(1) // Skip the text update state
            
            // Shorten URL
            viewModel.shortenUrl()
            skipItems(1) // Skip loading state
            
            // Error state
            val errorState = awaitItem()
            assertEquals(errorMessage, errorState.errorMessage)
            
            // Clear error
            viewModel.clearError()
            
            // Error cleared state
            val clearedState = awaitItem()
            assertNull(clearedState.errorMessage)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `shortenUrl should not do anything when url is empty`() = runTest {
        // Given - empty initial state
        
        // When & Then
        viewModel.uiState.test {
            // Initial state
            val initialState = awaitItem()
            assertEquals("", initialState.urlText)
            
            // Shorten URL with empty text
            viewModel.shortenUrl()
            
            // No state changes should occur
            expectNoEvents()
            
            cancelAndIgnoreRemainingEvents()
        }
    }
    
    @Test
    fun `viewModel should observe recent urls`() = runTest {
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
        
        // When & Then
        viewModel.uiState.test {
            // Initial state
            assertEquals(emptyList<ShortUrl>(), awaitItem().recentUrls)
            
            // Update recent URLs flow
            recentUrlsFlow.emit(listOf(shortUrl1))
            
            // State should update with new URLs
            assertEquals(listOf(shortUrl1), awaitItem().recentUrls)
            
            // Update recent URLs flow again
            recentUrlsFlow.emit(listOf(shortUrl2, shortUrl1))
            
            // State should update with new URLs
            assertEquals(listOf(shortUrl2, shortUrl1), awaitItem().recentUrls)
            
            cancelAndIgnoreRemainingEvents()
        }
    }
}
