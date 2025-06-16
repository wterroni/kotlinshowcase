package com.example.kotlinshowcase.feature.amiibo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlinshowcase.feature.amiibo.domain.model.Amiibo
import com.example.kotlinshowcase.feature.amiibo.navigation.model.AmiiboArg
import com.example.kotlinshowcase.feature.amiibo.presentation.ui.screen.AmiiboDetailScreen
import com.example.kotlinshowcase.feature.amiibo.presentation.ui.screen.AmiiboListScreen
import com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel.AmiiboListViewModel
import org.koin.androidx.compose.koinViewModel
import java.net.URLDecoder
import java.nio.charset.StandardCharsets


@Composable
fun AmiiboNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: AmiiboListViewModel = koinViewModel(),
    onBackClick: () -> Unit = { navController.popBackStack() }
) {
    NavHost(
        navController = navController,
        startDestination = AmiiboScreen.List.route,
        modifier = modifier
    ) {
        composable(AmiiboScreen.List.route) {
            val onRetry = {
                viewModel.refresh()
            }
            
            AmiiboListScreen(
                viewModel = viewModel,
                onAmiiboClick = { amiibo ->
                    navController.navigate(
                        AmiiboScreen.Detail.createRoute(amiibo)
                    )
                },
                onNavigateBack = onBackClick,
                onRetry = onRetry
            )
        }

        composable(
            route = AmiiboScreen.Detail.ROUTE_WITH_ARGS,
            arguments = listOf(
                navArgument(AmiiboScreen.Detail.AMIIBO_ARG) {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val json = backStackEntry.arguments?.getString(AmiiboScreen.Detail.AMIIBO_ARG) ?: run {
                navController.popBackStack()
                return@composable
            }
            
            val amiiboArg = try {
                val decodedJson = URLDecoder.decode(json, StandardCharsets.UTF_8.toString())
                AmiiboArg.json.decodeFromString<AmiiboArg>(decodedJson)
            } catch (e: Exception) {
                e.printStackTrace()
                navController.popBackStack()
                return@composable
            }

            AmiiboDetailScreen(
                amiibo = Amiibo(
                    name = amiiboArg.name,
                    image = amiiboArg.image,
                    character = amiiboArg.character,
                    gameSeries = amiiboArg.gameSeries,
                    amiiboSeries = amiiboArg.amiiboSeries,
                    type = amiiboArg.type,
                    release = amiiboArg.release
                ),
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
