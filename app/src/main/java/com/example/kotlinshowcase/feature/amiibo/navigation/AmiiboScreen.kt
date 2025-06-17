package com.example.kotlinshowcase.feature.amiibo.navigation

import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.navigation.model.AmiiboArg.Companion.toJson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Sealed class representing the different screens in the Amiibo feature.
 *
 * This class defines the navigation routes and parameters for the Amiibo feature.
 * Each screen has its own route and any necessary parameters for navigation.
 */
sealed class AmiiboScreen(val route: String) {
    /**
     * Represents the Amiibo list screen.
     */
    object List : AmiiboScreen("amiibo_list")
    
    /**
     * Represents the Amiibo detail screen.
     */
    data object Detail : AmiiboScreen("detail") {
        const val AMIIBO_ARG = "amiibo"
        const val ROUTE_WITH_ARGS = "detail?$AMIIBO_ARG={$AMIIBO_ARG}"
        
        /**
         * Creates a navigation route for the detail screen with the given Amiibo data.
         * @param amiibo The Amiibo data to pass to the detail screen
         * @return The navigation route as a string
         */
        fun createRoute(amiibo: Amiibo): String {
            val json = amiibo.toJson()
            val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
            return "detail?$AMIIBO_ARG=$encoded"
        }
    }
}
