package com.example.kotlinshowcase.feature.amiibo.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel.AmiiboListState
import com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel.AmiiboListViewModel
import com.example.kotlinshowcase.feature.amiibo.presentation.ui.screen.AmiiboDetailScreen
import com.example.kotlinshowcase.feature.amiibo.presentation.ui.screen.AmiiboListScreen
import org.koin.androidx.compose.koinViewModel

sealed class AmiiboScreen(val route: String) {
    object List : AmiiboScreen("amiibo_list")
    
    object Detail : AmiiboScreen("amiibo_detail") {
        const val AMIIBO_ID = "amiiboId"
        
        fun createRoute(amiiboId: String): String {
            return "${route}/$amiiboId"
        }
        
        val ROUTE_WITH_ARGS = "${Detail.route}/{$AMIIBO_ID}"
    }
}

@Composable
fun AmiiboNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: AmiiboListViewModel = koinViewModel(),
    onBackClick: () -> Unit = { navController.popBackStack() }
) {
    LaunchedEffect(Unit) {
        viewModel.loadAmiibos()
    }
    
    NavHost(
        navController = navController,
        startDestination = AmiiboScreen.List.route,
        modifier = modifier
    ) {

        composable(AmiiboScreen.List.route) {
            val state by viewModel.state.collectAsStateWithLifecycle()
            
            LaunchedEffect(Unit) {
                viewModel.loadAmiibos()
            }
            
            AmiiboListScreen(
                viewModel = viewModel,
                onAmiiboClick = { amiiboName ->
                    navController.navigate(
                        AmiiboScreen.Detail.createRoute(amiiboName)
                    )
                }
            )
        }

        composable(
            route = AmiiboScreen.Detail.ROUTE_WITH_ARGS,
            arguments = listOf(
                navArgument(AmiiboScreen.Detail.AMIIBO_ID) {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val amiiboName = backStackEntry.arguments?.getString(AmiiboScreen.Detail.AMIIBO_ID) ?: ""
            val state by viewModel.state.collectAsStateWithLifecycle()

            val amiibo = when (state) {
                is AmiiboListState.Success -> {
                    (state as AmiiboListState.Success).amiibos.find { it.name == amiiboName }
                }
                else -> null
            }

            if (amiibo != null) {
                AmiiboDetailScreen(
                    amiibo = amiibo,
                    onBackClick = onBackClick
                )
            } else {
                LaunchedEffect(Unit) {
                    onBackClick()
                }

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}
