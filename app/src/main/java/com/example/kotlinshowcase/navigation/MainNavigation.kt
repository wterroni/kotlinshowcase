package com.example.kotlinshowcase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kotlinshowcase.feature.amiibo.navigation.AmiiboNavigation
import com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel.AmiiboListViewModel
import com.example.kotlinshowcase.feature.home.presentation.ui.screen.HomeScreen
import com.example.kotlinshowcase.feature.password.presentation.screen.PasswordGeneratorScreen
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

        composable(Screen.HOME_ROUTE) {
            HomeScreen(
                onNavigate = { route ->
                    navController.navigate(route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        

        composable(Screen.AMIIBO_ROUTE) {
            val amiiboViewModel: AmiiboListViewModel = koinViewModel()
            AmiiboNavigation(
                viewModel = amiiboViewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
        

        composable(Screen.TEXT_UTILS_ROUTE) {
            TextUtilsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        

        composable(Screen.PASSWORD_GENERATOR_ROUTE) {
            PasswordGeneratorScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
