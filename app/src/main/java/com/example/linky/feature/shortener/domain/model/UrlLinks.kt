package com.example.linky.feature.shortener.domain.model

import kotlinx.serialization.Serializable

/**
 * Represents the links associated with a shortened URL
 */
@Serializable
data class UrlLinks(
    val self: String,
    val short: String
)
