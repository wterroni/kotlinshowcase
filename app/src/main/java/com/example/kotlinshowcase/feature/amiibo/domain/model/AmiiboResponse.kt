package com.example.kotlinshowcase.feature.amiibo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AmiiboResponse(
    val amiibo: List<Amiibo> = emptyList()
)
