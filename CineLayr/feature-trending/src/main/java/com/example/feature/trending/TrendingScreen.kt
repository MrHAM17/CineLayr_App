/* UI Version - 01 */
//package com.example.feature.trending
//
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.paging.LoadState
//import androidx.paging.compose.LazyPagingItems
//import androidx.paging.compose.collectAsLazyPagingItems
//import com.example.core.designsystem.CineLayrTheme
//import com.example.core.designsystem.ui.CineLayrTopBar
//import com.example.domain.model.Movie
//import org.koin.androidx.compose.koinViewModel
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.collectAsState
//import androidx.compose.ui.platform.testTag
//import androidx.compose.ui.semantics.contentDescription
//import androidx.compose.ui.semantics.semantics
//import com.example.core.designsystem.theme.ui.ThemeViewModel
//
//
//@Composable
//fun TrendingRoute(
//    onMovieClick: (Int) -> Unit,
//    viewModel: TrendingViewModel = koinViewModel()
//) {
//    val movies = viewModel.movies.collectAsLazyPagingItems()
//
//    TrendingScreen(
//        movies = movies,
//        onMovieClick = onMovieClick
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TrendingScreen(
//    movies: LazyPagingItems<Movie>,
//    onMovieClick: (Int) -> Unit,
//    themeViewModel: ThemeViewModel = koinViewModel()
//
//) {
//    val isDark by themeViewModel.isDarkMode.collectAsState()
//
//    Scaffold(
////        topBar = { TopAppBar(title = { Text("Trending Movies") })  }
//        topBar = {
//            CineLayrTopBar(
//                title = "Trending Movies",
//                isDark = isDark,
//                onToggleTheme = { themeViewModel.toggleTheme(isDark) },
////                modifier = Modifier
////                    .testTag("trending_topbar_toggle") // For baseline profile
////                    .semantics { contentDescription = "trending_topbar_toggle" }
//            )
//        }
//    ) { paddingValues ->
//        when {
//            movies.loadState.refresh is LoadState.Loading -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues),
//                    contentAlignment = Alignment.Center
//                ) {
//                    CircularProgressIndicator()
//                }
//            }
//
//            movies.loadState.refresh is LoadState.Error -> {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("Error loading movies")
//                }
//            }
//
//            else -> {
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues)
//                        .testTag("trending_list") // Added testTag for baseline profile
//                        .semantics { contentDescription = "trending_list" }
//
//                ) {
////                    items(movies.itemCount) { index ->
////                        movies[index]?.let { movie ->
////                            MovieItem(
////                                movie = movie,
////                                onMovieClick = onMovieClick
////                            )
////                        }
////                    }
////                    Above one works, but the recommended way for Paging 3 is:
//                    items(
//                            count = movies.itemCount,
//                            key = { index -> movies[index]?.id ?: index }
//                        ) { index ->
//                            movies[index]?.let { movie ->
//                                MovieItem(movie, onMovieClick)
//                            }
//                        }
//
//                    item {
//                        if (movies.loadState.append is LoadState.Loading) {
//                            Box(
//                                modifier = Modifier.fillParentMaxSize(),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                CircularProgressIndicator()
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Preview
//@Composable
//fun TrendingScreenPreview() {
//    CineLayrTheme {
//        // TrendingScreen preview
//    }
//}

package com.example.feature.trending

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.core.designsystem.theme.CineLayrTopBar
import com.example.core.designsystem.theme.ui.ThemeViewModel
import com.example.domain.model.Movie
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrendingRoute(
    onMovieClick: (Int) -> Unit,
    viewModel: TrendingViewModel = koinViewModel()
) { TrendingScreen(viewModel.movies.collectAsLazyPagingItems(), onMovieClick)  }



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    movies: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit,
    themeViewModel: ThemeViewModel = koinViewModel()
) {
    val isDark by themeViewModel.isDarkMode.collectAsState()

    Scaffold(
        topBar = {
            CineLayrTopBar(
                title = "Trending Movies",
                isDark = isDark,
                onToggleTheme = { themeViewModel.toggleTheme(isDark) },
//                modifier = Modifier
//                    .testTag("trending_topbar_toggle") // For baseline profile
//                    .semantics { contentDescription = "trending_topbar_toggle" }

            )
        }
    ) { paddingValues ->
        when {
            movies.loadState.refresh is LoadState.Loading -> LoadingScreen(paddingValues)
            movies.loadState.refresh is LoadState.Error -> ErrorScreen(paddingValues, "Error loading movies")
            else -> MovieList(movies, onMovieClick, paddingValues)
        }
    }
}



@Composable
fun MovieList(movies: LazyPagingItems<Movie>, onMovieClick: (Int) -> Unit, padding: PaddingValues) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .testTag("trending_list") // Added testTag for baseline profile
            .semantics { contentDescription = "trending_list" } // Added testTag for baseline profile
    ) {
// //                    items(movies.itemCount) { index ->
// //                        movies[index]?.let { movie ->
// //                            MovieItem( movie = movie, onMovieClick = onMovieClick )
// //                        }
// //                    }
// // ***                Above one works, but the recommended way for Paging 3 is:
        items(
            count = movies.itemCount,
            key = { index -> movies[index]?.id ?: index }
        )
        {   index -> movies[index]?.let { movie -> MovieItem(movie, onMovieClick) }     }

        // 👇 Add this block for pagination loading footer
        item {
            if (movies.loadState.append is LoadState.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
        }

        // Optional: handle append error (like retry button)
//        item {
//            val appendState = movies.loadState.append
//            if (appendState is LoadState.Error) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(16.dp),
//                    contentAlignment = Alignment.Center
//                ) {  Text("Error loading more movies. Please try again.")  }
//            }
//        }
    }
}
@Composable
fun LoadingScreen(padding: PaddingValues) = Box(
    modifier = Modifier.fillMaxSize().padding(padding),
    contentAlignment = Alignment.Center
) { CircularProgressIndicator() }
@Composable
fun ErrorScreen(padding: PaddingValues, message: String) = Box(
    modifier = Modifier.fillMaxSize().padding(padding),
    contentAlignment = Alignment.Center
) { Text(message) }
