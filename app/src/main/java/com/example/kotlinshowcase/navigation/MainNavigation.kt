package com.example.kotlinshowcase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlinshowcase.feature.amiibo.navigation.AmiiboNavigation
import com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel.AmiiboListViewModel
import org.koin.androidx.compose.koinViewModel

/**
 * Main navigation component that handles the app's navigation graph.
 * 
 * @param modifier Modifier for the NavHost
 * @param navController NavController for navigation
 * @param startDestination The starting destination route
 */
@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.AMIIBO_ROUTE,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.AMIIBO_ROUTE) {
            val amiiboViewModel: AmiiboListViewModel = koinViewModel()
            AmiiboNavigation(
                viewModel = amiiboViewModel
            )
        }
        
        // TODO: Adicionar navegação para as outras features
        composable(Screen.TEXT_UTILS_ROUTE) {
            // TODO: Implementar tela de TextUtils
        }
        
        composable(Screen.PASSWORD_GENERATOR_ROUTE) {
            // TODO: Implementar tela de PasswordGenerator
        }
    }
}
