/* UI Version - 01 */
//package com.example.cinelayr.ui
//
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Brightness2
//import androidx.compose.material3.Icon
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.stringResource
//import androidx.navigation.NavDestination.Companion.hierarchy
//import androidx.navigation.NavGraph.Companion.findStartDestination
//import androidx.navigation.compose.currentBackStackEntryAsState
//import androidx.navigation.compose.rememberNavController
//import com.example.core.designsystem.CineLayrTheme
//import com.example.feature.details.navigation.DetailsDestination
//import com.example.feature.trending.navigation.TrendingDestination
//import com.example.feature.watchlist.navigation.WatchlistDestination
//import com.example.cinelayr.navigation.CineLayrNavHost
//import com.example.cinelayr.R
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.TrendingUp
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.TopAppBar
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.platform.testTag
//import androidx.compose.ui.semantics.contentDescription
//import androidx.compose.ui.semantics.semantics
//import com.example.core.designsystem.theme.ui.ThemeViewModel
//import org.koin.androidx.compose.getViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
////fun CineLayrApp()
//fun CineLayrApp(startMovieId: Int?)   // For widgetToApp entry...
//{
//    val themeViewModel: ThemeViewModel = getViewModel()
//    val isDark by themeViewModel.isDarkMode.collectAsState()
//
//    CineLayrTheme(darkTheme = isDark) {
//        val navController = rememberNavController()
//        val currentBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentDestination = currentBackStackEntry?.destination
//
//        val topLevelDestinations = listOf(
//            TopLevelDestination.Trending,
//            TopLevelDestination.Watchlist
//        )
//
//        // 🔹 Handle widget launch navigation
//        LaunchedEffect(startMovieId) {
//            if (startMovieId != null && startMovieId > 0) {
//                navController.navigate("${DetailsDestination.route}/$startMovieId")    // Navigate to Details without popping Trending
//            }
//        }
//
//        Scaffold(
////             topBar = {
////                TopAppBar(
////                    title = { Text("CineLayr") },
////                    actions = {
////                        IconButton(onClick = { themeViewModel.toggleTheme(isDark) }) {
////                            Icon(
////                                imageVector = Icons.Default.Brightness2,
////                                contentDescription = "Toggle Theme"
////                            )
////                        }
////                    }
////                )
////            },
//            bottomBar = {
//                NavigationBar {
//                    topLevelDestinations.forEach { destination ->
//                        val selected = currentDestination?.hierarchy?.any { it.route == destination.route } == true
//                        NavigationBarItem(
//                            selected = selected,
//                            onClick = {
//                                navController.navigate(destination.route) {
//                                    popUpTo(navController.graph.findStartDestination().id) {
//                                        saveState = true
//                                    }
//                                    launchSingleTop = true
//                                    restoreState = true
//                                }
//                            },
//                            icon = { Icon( imageVector = destination.icon,
////                                contentDescription = null
//                                contentDescription = destination.route  // optional but helpful
//
//                            )},
//                            label = { Text(stringResource(destination.titleTextId)) },
//                            modifier = Modifier
//                                .testTag(destination.route)   // 🔹 key addition
//                                .semantics { contentDescription = destination.route }
//
//                        )
//                    }
//                }
//            }
//        ) { paddingValues ->
//            CineLayrNavHost(
//                navController = navController,
//                modifier = Modifier.padding(paddingValues)
//            )
//        }
//    }
//}
//
//sealed class TopLevelDestination(
//    val route: String,
//    val titleTextId: Int,
//    val icon: androidx.compose.ui.graphics.vector.ImageVector
//)
//{
//    object Trending : TopLevelDestination(
//        route = TrendingDestination.route,
//        titleTextId = R.string.trending,
//        icon = androidx.compose.material.icons.Icons.Default.TrendingUp
//    )
//
//    object Watchlist : TopLevelDestination(
//        route = WatchlistDestination.route,
//        titleTextId = R.string.watchlist,
//        icon = androidx.compose.material.icons.Icons.Default.Favorite
//    )
//}

package com.example.cinelayr.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cinelayr.R
import com.example.cinelayr.navigation.CineLayrNavHost
import com.example.core.designsystem.CineLayrTheme
import com.example.core.designsystem.theme.ui.ThemeViewModel
import com.example.feature.details.navigation.DetailsDestination
import com.example.feature.trending.navigation.TrendingDestination
import com.example.feature.watchlist.navigation.WatchlistDestination
import org.koin.androidx.compose.getViewModel

@Composable
////fun CineLayrApp()
fun CineLayrApp(startMovieId: Int?)    // For widgetToApp entry...
{
    val themeViewModel: ThemeViewModel = getViewModel()
    // Wait for theme value to be loaded
//    val isDark by themeViewModel.isDarkMode.collectAsState()
    val isDark by themeViewModel.isDarkMode.collectAsState(initial = null)
// If theme not loaded yet, show splash/blank background
    if (isDark == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Optional: CircularProgressIndicator() or just blank background
        }
        return
    }

    val navController = rememberNavController()
    val currentDestination by navController.currentBackStackEntryAsState()

    val destinations = listOf(
        TopLevelDestination(TrendingDestination.route, R.string.trending, Icons.Default.TrendingUp),
        TopLevelDestination(WatchlistDestination.route, R.string.watchlist, Icons.Default.Favorite)
    )

    // 🔹 Handle widget launch navigation
    LaunchedEffect(startMovieId) {
        if (startMovieId != null && startMovieId > 0) {
            navController.navigate("${DetailsDestination.route}/$startMovieId")    // Navigate to Details without popping Trending
        }
    }

    // force unwrap is safe here
    CineLayrTheme(darkTheme = isDark!!) {
        Scaffold(
//            contentWindowInsets = WindowInsets(0, 0, 0, 0),
//            contentWindowInsets = WindowInsets(0, 0, 0, 0), // 🚫 disable automatic system insets    // pending resolving ui issues - android 12+
            modifier = Modifier
                .fillMaxSize(),
//                .padding(WindowInsets.systemBars.asPaddingValues()),  // consume system bar insets so content fills behind them

                    bottomBar = {
                NavigationBar(
                    modifier = Modifier
                        .height(102.dp),
//                        .windowInsetsPadding(WindowInsets.navigationBars), //  respect gestures only
//                    contentWindowInsets = WindowInsets(0, 0, 0, 0), // disables default inset padding
                    containerColor = MaterialTheme.colorScheme.background // surface
                    ) {
                    destinations.forEach { destination ->
                        val selected = currentDestination?.destination?.hierarchy?.any { it.route == destination.route } == true
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(destination.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(destination.icon,
//                                contentDescription = null
                                contentDescription = stringResource(destination.titleTextId),   // optional but helpful
                                modifier = Modifier.size(if (selected) 26.dp else 22.dp) // ✅ adjust icon size
                            ) },
                            label = { Text(
                                stringResource(destination.titleTextId),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontSize = if (selected) 13.sp else 12.sp,
                                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant )
                                ) },
//                            alwaysShowLabel = false, // optional: only show label for selected
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ),
                            modifier = Modifier
                                .testTag(destination.route)
                                .semantics { contentDescription = destination.route }
                        )
                    }
                }
            }
        ) { paddingValues ->
            CineLayrNavHost(
                navController = navController,
                modifier = Modifier
                    .fillMaxSize()
//                    .padding(paddingValues)
                    //  remove extra bottom padding only
                    .padding(
                        start = paddingValues.calculateStartPadding(LayoutDirection.Ltr),
                        end = paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                        top = paddingValues.calculateTopPadding()
//                        top = 0.dp // 🚫 remove top padding       // pending resolving ui issues - android 12+

                    )
            )
        }
    }
}

data class TopLevelDestination(
    val route: String,
    val titleTextId: Int,
    val icon: ImageVector
)
