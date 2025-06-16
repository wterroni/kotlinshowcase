package com.example.kotlinshowcase.feature.amiibo.navigation.model

import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.domain.model.ReleaseDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Data class representing the serializable argument for Amiibo navigation.
 * Used to pass Amiibo data between composables using navigation arguments.
 *
 * @property name The name of the Amiibo
 * @property image The URL of the Amiibo image
 * @property character The character name
 * @property gameSeries The game series the Amiibo belongs to
 * @property amiiboSeries The Amiibo series
 * @property type The type of Amiibo
 * @property release The release date information
 */
@Serializable
data class AmiiboArg(
    val name: String,
    val image: String,
    val character: String,
    val gameSeries: String,
    val amiiboSeries: String,
    val type: String,
    val release: ReleaseDate
) {
    companion object {
        val json = Json { ignoreUnknownKeys = true }
        
        /**
         * Converts an Amiibo object to a JSON string.
         * @param amiibo The Amiibo object to convert
         * @return JSON string representation of the Amiibo
         */
        fun Amiibo.toJson(): String {
            val arg = AmiiboArg(
                name = this.name,
                image = this.image,
                character = this.character,
                gameSeries = this.gameSeries,
                amiiboSeries = this.amiiboSeries,
                type = this.type,
                release = this.release
            )
            return json.encodeToString(serializer(), arg)
        }
    }
}
