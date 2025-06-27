package com.example.linky.feature.shortener.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the API response when retrieving an original URL by alias
 */
@Serializable
data class GetUrlResponse(
    val url: String
)
