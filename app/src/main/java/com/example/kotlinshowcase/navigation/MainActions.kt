package com.example.kotlinshowcase.navigation

import androidx.navigation.NavHostController

/**
 * Class that defines all navigation actions available in the application.
 * Provides a centralized way to handle navigation throughout the app.
 *
 * @property navController The NavController used for navigation
 */
class MainActions(private val navController: NavHostController) {
    /**
     * Navigates to the home screen, clearing the back stack.
     */
    fun navigateToHome() {
        navController.navigate(Screen.HOME_ROUTE) {
            popUpTo(Screen.HOME_ROUTE) { inclusive = true }
        }
    }
    
    /**
     * Navigates to the Amiibo list screen.
     */
    fun navigateToAmiiboList() {
        navController.navigate(Screen.AMIIBO_ROUTE)
    }
    
    /**
     * Navigates to the Text Utils screen.
     */
    fun navigateToTextUtils() {
        navController.navigate(Screen.TEXT_UTILS_ROUTE)
    }
    
    /**
     * Navigates to the Password Generator screen.
     */
    fun navigateToPasswordGenerator() {
        navController.navigate(Screen.PASSWORD_GENERATOR_ROUTE)
    }
    
    /**
     * Navigates back to the previous screen.
     */
    fun navigateBack() {
        navController.popBackStack()
    }
}
