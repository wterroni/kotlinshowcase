package com.example.linky.feature.shortener.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the API response after shortening a URL
 */
@Serializable
data class ShortenUrlResponse(
    val alias: String,
    val _links: UrlLinks
)
