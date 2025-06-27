package com.example.linky.feature.shortener.domain.model

/**
 * Represents a shortened URL for display in the UI
 */
data class ShortUrl(
    val originalUrl: String,
    val alias: String,
    val shortUrl: String,
    val timestamp: Long = System.currentTimeMillis()
)
