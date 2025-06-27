package com.example.linky.feature.shortener.presentation.screen

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.example.linky.core.theme.LinkyTheme
import com.example.linky.feature.shortener.domain.model.ShortUrl
import com.example.linky.feature.shortener.presentation.viewmodel.UrlShortenerUiState
import com.example.linky.feature.shortener.presentation.viewmodel.UrlShortenerViewModel
import com.example.linky.util.MockKRule
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class ClipboardIntegrationTest {

    private val composeTestRule = createComposeRule()
    private val mockkRule = MockKRule()
    
    @get:Rule
    val testRule: RuleChain = RuleChain
        .outerRule(mockkRule)
        .around(composeTestRule)
    
    @Test
    fun copyButtonCopiesUrlToClipboard() {
        // Given
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val shortUrl = "https://sou.nu/abc123"
        val viewModel = mockk<UrlShortenerViewModel>(relaxed = true)
        
        val uiState = UrlShortenerUiState(
            recentUrls = listOf(
                ShortUrl(
                    originalUrl = "https://example.com",
                    alias = "abc123",
                    shortUrl = shortUrl
                )
            )
        )
        
        val uiStateFlow = MutableStateFlow(uiState)
        every { viewModel.uiState } returns uiStateFlow
        
        // When
        composeTestRule.setContent {
            LinkyTheme {
                UrlShortenerScreen(viewModel = viewModel)
            }
        }
        
        // Then
        // Find the Copy button and click it
        composeTestRule.onNodeWithText("Copy").performClick()
        
        // Verify that the URL was copied to clipboard
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = clipboardManager.primaryClip
        
        // Assert
        assert(clipData != null) { "Clipboard data should not be null" }
        clipData?.let {
            assertEquals(1, it.itemCount)
            assertEquals(shortUrl, it.getItemAt(0).text.toString())
        }
    }
}
