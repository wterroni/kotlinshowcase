package com.example.kotlinshowcase.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlinshowcase.feature.amiibo.navigation.AmiiboNavigation
import com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel.AmiiboListViewModel
import com.example.kotlinshowcase.feature.home.presentation.ui.screen.HomeScreen
import com.example.kotlinshowcase.feature.textutils.presentation.ui.screen.TextUtilsScreen
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
    startDestination: String = Screen.HOME_ROUTE,
) {
    val actions = remember(navController) { MainActions(navController) }
    
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Home Screen
        composable(Screen.HOME_ROUTE) {
            HomeScreen(
                onNavigate = { route ->
                    navController.navigate(route) {
                        // Evita múltiplas cópias da mesma tela na pilha
                        launchSingleTop = true
                    }
                }
            )
        }
        
        // Amiibo Feature
        composable(Screen.AMIIBO_ROUTE) {
            val amiiboViewModel: AmiiboListViewModel = koinViewModel()
            AmiiboNavigation(
                viewModel = amiiboViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // Text Utils Feature
        composable(Screen.TEXT_UTILS_ROUTE) {
            TextUtilsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        // Password Generator Feature
        composable(Screen.PASSWORD_GENERATOR_ROUTE) {
            // TODO: Implementar tela de PasswordGenerator
            Text("Password Generator - Em breve")
        }
    }
}

/**
 * Define as ações de navegação disponíveis na aplicação
 */
class MainActions(private val navController: NavHostController) {
    fun navigateToHome() {
        navController.navigate(Screen.HOME_ROUTE) {
            popUpTo(Screen.HOME_ROUTE) { inclusive = true }
        }
    }
    
    fun navigateToAmiiboList() {
        navController.navigate(Screen.AMIIBO_ROUTE)
    }
    
    fun navigateToTextUtils() {
        navController.navigate(Screen.TEXT_UTILS_ROUTE)
    }
    
    fun navigateToPasswordGenerator() {
        navController.navigate(Screen.PASSWORD_GENERATOR_ROUTE)
    }
    
    fun navigateBack() {
        navController.popBackStack()
    }
}
