package com.example.kotlinshowcase.navigation

/**
 * Sealed class representing all screens in the app with their routes.
 * This provides a type-safe way to handle navigation routes.
 */
sealed class Screen(val route: String) {
    companion object {
        const val HOME_ROUTE = "home"
        const val AMIIBO_ROUTE = "amiibo"
        const val TEXT_UTILS_ROUTE = "text_utils"
        const val PASSWORD_GENERATOR_ROUTE = "password_generator"
    }
    
    object Home : Screen(HOME_ROUTE)
    object Amiibo : Screen(AMIIBO_ROUTE)
    object TextUtils : Screen(TEXT_UTILS_ROUTE)
    object PasswordGenerator : Screen(PASSWORD_GENERATOR_ROUTE)
}
