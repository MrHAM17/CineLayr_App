package com.example.cinelayr.navigation

import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.example.feature.details.navigation.DetailsDestination
import com.example.feature.details.DetailsRoute
import com.example.feature.trending.navigation.TrendingDestination
import com.example.feature.trending.TrendingRoute
import com.example.feature.watchlist.navigation.WatchlistDestination
import com.example.feature.watchlist.WatchlistRoute

@Composable
fun CineLayrNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
)
{
    NavHost(
        navController = navController,
        startDestination = TrendingDestination.route,
        modifier = modifier
    )
    {
        composable(route = TrendingDestination.route) {
            TrendingRoute(
                onMovieClick = { movieId ->
                    navController.navigate("${DetailsDestination.route}/$movieId")
                }
            )
        }

        composable(route = WatchlistDestination.route) {
            WatchlistRoute(
                onMovieClick = { movieId ->
                    navController.navigate("${DetailsDestination.route}/$movieId")
                }
            )
        }

        composable(
//            route = "${DetailsDestination.route}/{${DetailsDestination.movieIdArg}}"
            ///
            route = DetailsDestination.routeWithArgs
        ) { backStackEntry ->
//            val movieId = backStackEntry.arguments?.getString(DetailsDestination.movieIdArg)?.toIntOrNull() ?: 0
//            DetailsRoute(
//                onBackClick = { navController.popBackStack() }
//            )
            ///
            val movieIdStr = backStackEntry.arguments?.getString(DetailsDestination.movieIdArg)
            val movieId = movieIdStr?.toIntOrNull()

            if (movieId != null) {
                DetailsRoute(
                    movieId = movieId, onBackClick = { navController.popBackStack() }
                )
            } else {
                // Handle invalid/missing ID
                Text("Invalid movie ID", color = Color.Red)
            }
        }

        composable(
            route = DetailsDestination.routeWithArgs,
            deepLinks = listOf(navDeepLink { uriPattern = "cinelayr://details/{movieId}" })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString(DetailsDestination.movieIdArg)?.toIntOrNull()
            if (movieId != null) {
                DetailsRoute(
                    movieId = movieId,
                    onBackClick = { navController.popBackStack() })
            } else {
                Text("DeepLinks-Invalid movie ID", color = Color.Red)
            }
        }

    }
}


/**
 * CHANGE SUMMARY – Safe movieId handling (instead of SavedStateHandle)
 *
 * 1️⃣ CineLayrNavHost.kt
 * BEFORE: movieId = backStackEntry.arguments?.getString(...)?.toIntOrNull() ?: 0
 * AFTER:
 * val movieIdStr = backStackEntry.arguments?.getString(DetailsDestination.movieIdArg)
 * val movieId = movieIdStr?.toIntOrNull()
 * if (movieId != null) DetailsRoute(movieId, ...) else Text("Invalid movie ID")
 *
 * 2️⃣ DetailsScreen.kt/DetailsRoute.kt:
 * BEFORE: koinViewModel() without parameters
 * AFTER: koinViewModel(parameters = { parametersOf(movieId) })
 *
 * 3️⃣ DetailsViewModel.kt
 * BEFORE: Using SavedStateHandle to get movieId, init block fun sign & definations had no args, fun "toggleWatchlist" has movieId=movieId
 * * class DetailsViewModel(..savedStateHandle: SavedStateHandle)
 * * private val movieId = savedStateHandle.get<String>(..)
 * * loadMovieDetails(), checkWatchlistStatus()
 * * private fun loadMovieDetails() {..}, private fun checkWatchlistStatus() {..},
 * * fun toggleWatchlist() {..repository.toggleWatchlistStatus(movieId = movieId,...}
 * *
 * AFTER: Inject movieId directly via constructor, store movieId, init block fun sign & definations has args,  fun "toggleWatchlist" has movieId=movieIdProperty
 * * class DetailsViewModel(..movieId: Int)
 * * private val movieIdProperty = movieId
 * loadMovieDetails(movieIdProperty), checkWatchlistStatus(movieIdProperty)
 * * private fun loadMovieDetails(movieId: Int) {..}, private fun checkWatchlistStatus(movieId: Int) {..},
 * * fun toggleWatchlist() {..repository.toggleWatchlistStatus(movieId = movieIdProperty,...}
 *
 * 4️⃣ detailsModule.kt
 * BEFORE:
 *     viewModel { (savedStateHandle: SavedStateHandle) -> DetailsViewModel(get(), savedStateHandle)  }
 * AFTER:
 *     viewModel { (movieId: Int) -> DetailsViewModel(repository = get(), movieId = movieId) }
 *
 * ✅ BENEFIT:
 * - Navigation and ViewModel fully type-safe
 * - Avoid crash if movieId missing/invalid
 * - Clean professional approach, no forced SavedStateHandle casting
 */
