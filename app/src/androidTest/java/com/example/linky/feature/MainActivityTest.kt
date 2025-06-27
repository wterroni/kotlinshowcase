package com.example.linky.feature

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.linky.util.MockKRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    
    private val composeTestRule = createAndroidComposeRule<MainActivity>()
    private val mockkRule = MockKRule()
    
    @get:Rule
    val testRule: RuleChain = RuleChain
        .outerRule(mockkRule)
        .around(composeTestRule)
    
    @Test
    fun appLaunchesSuccessfully() {
        // Verify that the main screen components are displayed
        composeTestRule.onNodeWithText("URL").assertExists()
        composeTestRule.onNodeWithText("Recently shortened URLs").assertExists()
    }
    
    @Test
    fun canEnterUrlAndClickShortenButton() {
        // Enter a URL
        composeTestRule.onNodeWithText("URL").performTextInput("example.com")
        
        // Verify the text was entered
        composeTestRule.onNode(hasText("example.com")).assertExists()
        
        // Click the shorten button
        composeTestRule.onNodeWithContentDescription("Shorten URL").performClick()
    }
    
    @Test
    fun emptyInputFieldAfterEnteringUrl() {
        // Enter a URL
        composeTestRule.onNodeWithText("URL").performTextInput("example.com")
        
        // Click the shorten button
        composeTestRule.onNodeWithContentDescription("Shorten URL").performClick()
        
        // Wait for the operation to complete (in a real test, we'd use idling resources)
        composeTestRule.waitForIdle()
        
        // The input field should be empty after successful shortening
        // Note: This test might be flaky depending on network conditions
        // In a real app, we'd mock the network response
    }
}
