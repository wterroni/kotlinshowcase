package com.example.linky.feature.shortener.presentation.screen

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.linky.core.theme.LinkyTheme
import com.example.linky.feature.shortener.domain.model.ShortUrl
import com.example.linky.feature.shortener.presentation.viewmodel.UrlShortenerUiState
import com.example.linky.feature.shortener.presentation.viewmodel.UrlShortenerViewModel
import com.example.linky.util.MockKRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class UrlShortenerScreenTest {
    
    private val composeTestRule = createComposeRule()
    private val mockkRule = MockKRule()
    
    @get:Rule
    val testRule: RuleChain = RuleChain
        .outerRule(mockkRule)
        .around(composeTestRule)
    
    @Test
    fun initialStateDisplaysEmptyInput() {
        // Given
        val viewModel = mockk<UrlShortenerViewModel>(relaxed = true)
        val uiState = UrlShortenerUiState()
        val uiStateFlow = MutableStateFlow(uiState)
        
        every { viewModel.uiState } returns uiStateFlow
        
        // When
        composeTestRule.setContent {
            LinkyTheme {
                UrlShortenerScreen(viewModel = viewModel)
            }
        }
        
        // Then
        composeTestRule.onNodeWithText("URL").assertExists()
        composeTestRule.onNodeWithText("Recently shortened URLs").assertExists()
    }
    
    @Test
    fun enteringUrlAndClickingIconCallsShortenUrl() {
        // Given
        val viewModel = mockk<UrlShortenerViewModel>(relaxed = true)
        val uiState = UrlShortenerUiState(urlText = "")
        val uiStateFlow = MutableStateFlow(uiState)
        
        every { viewModel.uiState } returns uiStateFlow
        
        // When
        composeTestRule.setContent {
            LinkyTheme {
                UrlShortenerScreen(viewModel = viewModel)
            }
        }
        
        // Enter URL
        composeTestRule.onNodeWithText("URL").performTextInput("example.com")
        
        // Click the button
        composeTestRule.onNodeWithContentDescription("Shorten URL").performClick()
        
        // Then
        verify { viewModel.onUrlTextChanged("example.com") }
        verify { viewModel.shortenUrl() }
    }
    
    @Test
    fun loadingStateDisplaysProgressIndicator() {
        // Given
        val viewModel = mockk<UrlShortenerViewModel>(relaxed = true)
        val uiState = UrlShortenerUiState(isLoading = true)
        val uiStateFlow = MutableStateFlow(uiState)
        
        every { viewModel.uiState } returns uiStateFlow
        
        // When
        composeTestRule.setContent {
            LinkyTheme {
                UrlShortenerScreen(viewModel = viewModel)
            }
        }
        
        // Then
        composeTestRule.onNode(isProgressBarIndeterminate()).assertExists()
    }
    
    @Test
    fun errorStateDisplaysErrorMessage() {
        // Given
        val viewModel = mockk<UrlShortenerViewModel>(relaxed = true)
        val errorMessage = "Error shortening URL"
        val uiState = UrlShortenerUiState(errorMessage = errorMessage)
        val uiStateFlow = MutableStateFlow(uiState)
        
        every { viewModel.uiState } returns uiStateFlow
        
        // When
        composeTestRule.setContent {
            LinkyTheme {
                UrlShortenerScreen(viewModel = viewModel)
            }
        }
        
        // Then
        composeTestRule.onNodeWithText(errorMessage).assertExists()
    }
    
    @Test
    fun recentUrlsDisplayedInList() {
        // Given
        val viewModel = mockk<UrlShortenerViewModel>(relaxed = true)
        val recentUrls = listOf(
            ShortUrl(
                originalUrl = "https://example.com",
                alias = "abc123",
                shortUrl = "https://sou.nu/abc123"
            ),
            ShortUrl(
                originalUrl = "https://another-example.com",
                alias = "def456",
                shortUrl = "https://sou.nu/def456"
            )
        )
        val uiState = UrlShortenerUiState(recentUrls = recentUrls)
        val uiStateFlow = MutableStateFlow(uiState)
        
        every { viewModel.uiState } returns uiStateFlow
        
        // When
        composeTestRule.setContent {
            LinkyTheme {
                UrlShortenerScreen(viewModel = viewModel)
            }
        }
        
        // Then
        composeTestRule.onNodeWithText("https://example.com").assertExists()
        composeTestRule.onNodeWithText("https://sou.nu/abc123").assertExists()
        composeTestRule.onNodeWithText("https://another-example.com").assertExists()
        composeTestRule.onNodeWithText("https://sou.nu/def456").assertExists()
    }
    
    private fun isProgressBarIndeterminate(): SemanticsMatcher {
        return SemanticsMatcher.expectValue(
            androidx.compose.ui.semantics.SemanticsProperties.ProgressBarRangeInfo,
            androidx.compose.ui.semantics.ProgressBarRangeInfo.Indeterminate
        )
    }
}
