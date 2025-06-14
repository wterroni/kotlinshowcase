package com.example.kotlinshowcase.feature.amiibo.model

import kotlinx.serialization.Serializable

@Serializable
data class AmiiboResponse(
    val amiibo: List<Amiibo>
)

@Serializable
data class Amiibo(
    val name: String,
    val character: String,
    val gameSeries: String,
    val image: String,
    val amiiboSeries: String,
    val type: String,
    val release: ReleaseDate
)

@Serializable
data class ReleaseDate(
    val au: String? = null,
    val eu: String? = null,
    val jp: String? = null,
    val na: String? = null
)
