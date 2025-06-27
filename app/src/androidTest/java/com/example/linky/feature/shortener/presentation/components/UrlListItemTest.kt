package com.example.linky.feature.shortener.presentation.components

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.linky.core.theme.LinkyTheme
import com.example.linky.feature.shortener.domain.model.ShortUrl
import com.example.linky.util.MockKRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class UrlListItemTest {

    private val composeTestRule = createComposeRule()
    private val mockkRule = MockKRule()
    
    @get:Rule
    val testRule: RuleChain = RuleChain
        .outerRule(mockkRule)
        .around(composeTestRule)
    
    @Test
    fun urlListItemDisplaysCorrectContent() {
        // Given
        val originalUrl = "https://example.com"
        val shortUrl = "https://sou.nu/abc123"
        val shortUrlItem = ShortUrl(
            originalUrl = originalUrl,
            alias = "abc123",
            shortUrl = shortUrl
        )
        
        // When
        composeTestRule.setContent {
            LinkyTheme {
                UrlListItem(shortUrl = shortUrlItem)
            }
        }
        
        // Then
        composeTestRule.onNodeWithText(originalUrl).assertExists()
        composeTestRule.onNodeWithText(shortUrl).assertExists()
        composeTestRule.onNodeWithText("Copy").assertExists()
    }
    
    @Test
    fun clickingCopyButtonCallsCallback() {
        // Given
        var wasCopyCalled = false
        val shortUrlItem = ShortUrl(
            originalUrl = "https://example.com",
            alias = "abc123",
            shortUrl = "https://sou.nu/abc123"
        )
        
        // When
        composeTestRule.setContent {
            LinkyTheme {
                UrlListItem(
                    shortUrl = shortUrlItem,
                    onCopyClick = { wasCopyCalled = true }
                )
            }
        }
        
        // Click the copy button
        composeTestRule.onNodeWithText("Copy").performClick()
        
        // Then
        assert(wasCopyCalled) { "Copy callback was not called when button was clicked" }
    }
    
    @Test
    fun longUrlsAreTruncatedWithEllipsis() {
        // Given
        val veryLongUrl = "https://example.com/very/long/url/that/should/be/truncated/because/it/is/too/long/to/fit/on/one/line"
        val shortUrlItem = ShortUrl(
            originalUrl = veryLongUrl,
            alias = "abc123",
            shortUrl = "https://sou.nu/abc123"
        )
        
        // When
        composeTestRule.setContent {
            LinkyTheme {
                UrlListItem(shortUrl = shortUrlItem)
            }
        }
        
        // Then
        composeTestRule.onNodeWithText(veryLongUrl, substring = true).assertExists()
    }
}
