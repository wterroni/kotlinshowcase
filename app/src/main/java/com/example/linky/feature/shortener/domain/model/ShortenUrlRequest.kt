package com.example.linky.feature.shortener.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents a request to shorten a URL
 */
@Serializable
data class ShortenUrlRequest(
    val url: String
)
