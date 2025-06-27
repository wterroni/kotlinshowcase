package com.example.linky.feature.shortener.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linky.feature.shortener.domain.model.ShortUrl
import com.example.linky.feature.shortener.domain.usecase.GetRecentUrlsUseCase
import com.example.linky.feature.shortener.domain.usecase.ShortenUrlUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the URL shortening screen
 */
class UrlShortenerViewModel(
    private val shortenUrlUseCase: ShortenUrlUseCase,
    private val getRecentUrlsUseCase: GetRecentUrlsUseCase
) : ViewModel() {
    
    // UI state
    private val _uiState = MutableStateFlow(UrlShortenerUiState())
    val uiState: StateFlow<UrlShortenerUiState> = _uiState.asStateFlow()
    
    init {
        // Observe recent URLs
        viewModelScope.launch {
            getRecentUrlsUseCase().collect { recentUrls ->
                _uiState.update { it.copy(recentUrls = recentUrls) }
            }
        }
    }
    
    /**
     * Updates the URL text in the input field
     */
    fun onUrlTextChanged(text: String) {
        _uiState.update { it.copy(urlText = text) }
    }
    
    /**
     * Shortens the current URL
     */
    fun shortenUrl() {
        val url = uiState.value.urlText.trim()
        if (url.isEmpty()) return
        
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        
        viewModelScope.launch {
            shortenUrlUseCase(url)
                .onSuccess { shortUrl ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            urlText = "",
                            lastShortenedUrl = shortUrl
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Unknown error"
                        )
                    }
                }
        }
    }
    
    /**
     * Clears the error message
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

/**
 * UI state for the URL shortening screen
 */
data class UrlShortenerUiState(
    val urlText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val recentUrls: List<ShortUrl> = emptyList(),
    val lastShortenedUrl: ShortUrl? = null
)
