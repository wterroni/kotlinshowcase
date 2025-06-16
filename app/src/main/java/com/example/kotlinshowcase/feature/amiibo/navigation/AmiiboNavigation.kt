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
import com.example.kotlinshowcase.feature.amiibo.navigation.AmiiboArg.Companion.toJson
import com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel.AmiiboListViewModel
import com.example.kotlinshowcase.feature.amiibo.presentation.ui.screen.AmiiboDetailScreen
import com.example.kotlinshowcase.feature.amiibo.presentation.ui.screen.AmiiboListScreen
import org.koin.androidx.compose.koinViewModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

import com.example.kotlinshowcase.feature.amiibo.domain.model.ReleaseDate
import com.example.kotlinshowcase.feature.amiibo.presentation.viewmodel.AmiiboListState

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

sealed class AmiiboScreen(val route: String) {
    object List : AmiiboScreen("amiibo_list")
    
    data object Detail : AmiiboScreen("detail") {
        const val AMIIBO_ARG = "amiibo"
        const val ROUTE_WITH_ARGS = "detail?$AMIIBO_ARG={$AMIIBO_ARG}"
        
        fun createRoute(amiibo: Amiibo): String {
            val json = amiibo.toJson()
            val encoded = URLEncoder.encode(json, StandardCharsets.UTF_8.toString())
            return "detail?$AMIIBO_ARG=$encoded"
        }
    }
}

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
            LaunchedEffect(Unit) {
                if (viewModel.state.value is AmiiboListState.Success) {
                    // Se já tiver dados, não precisa recarregar
                    return@LaunchedEffect
                }
                viewModel.loadAmiibos()
            }
            
            AmiiboListScreen(
                viewModel = viewModel,
                onAmiiboClick = { amiibo ->
                    navController.navigate(
                        AmiiboScreen.Detail.createRoute(amiibo)
                    )
                },
                onNavigateBack = onBackClick
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
