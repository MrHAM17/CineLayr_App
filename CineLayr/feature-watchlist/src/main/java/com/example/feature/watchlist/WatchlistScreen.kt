package com.example.feature.watchlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.paging.LoadState
//import androidx.paging.compose.items
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.core.designsystem.CineLayrTheme
import com.example.core.designsystem.theme.ui.ThemeViewModel
import com.example.core.designsystem.theme.CineLayrTopBar
import com.example.domain.model.Movie
import com.example.feature.trending.MovieItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun WatchlistRoute(
    onMovieClick: (Int) -> Unit,
    viewModel: WatchlistViewModel = koinViewModel()
) {
    val movies = viewModel.watchlistMovies.collectAsLazyPagingItems()

    WatchlistScreen(
        movies = movies,
        onMovieClick = onMovieClick
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen(
    movies: LazyPagingItems<Movie>,
    onMovieClick: (Int) -> Unit,
    themeViewModel: ThemeViewModel = koinViewModel()

) {
    val isDark by themeViewModel.isDarkMode.collectAsState()

    Scaffold(
//        topBar = { TopAppBar(title = { Text("My Watchlist") })  }
        topBar = { CineLayrTopBar(
            title = "My Watchlist",
            isDark = isDark,
            onToggleTheme = { themeViewModel.toggleTheme(isDark) },
//            modifier = Modifier
//                .testTag("watchlist_topbar_toggle")   // Added testTag for baseline profile
//                .semantics { contentDescription = "watchlist_topbar_toggle" }

        ) }

    ) { paddingValues ->
        when {
            movies.loadState.refresh is LoadState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            movies.loadState.refresh is LoadState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Error loading watchlist")
                }
            }

            movies.itemCount == 0 -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Your watchlist is empty")
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .testTag("watchlist_list") // Added testTag for baseline profile
                        .semantics { contentDescription = "watchlist_list" }

                ) {
//                    items(movies) { movie ->
//                        movie?.let {
//                            MovieItem(
//                                movie = it,
//                                onMovieClick = onMovieClick
////                                onMovieClick = { id -> onMovieClick(id) } // explicit for clarity
//
//                            )
//                        }
//                    }
                    // FIX: Use the correct approach for LazyPagingItems
                    items(
                        count = movies.itemCount,
                        key = { index ->
                            movies[index]?.id ?: index
                        }
                    ) { index ->
                        movies[index]?.let { movie ->
                            MovieItem(
                                movie = movie,
                                onMovieClick = onMovieClick,
                                modifier = Modifier
                                    .testTag("watchlist_item_${movie.id}")   // for BaselineProfile + UI Test
                                    .semantics { contentDescription = "watchlist_item_${movie.id}" } // accessibility/test
                            )
                        }
                    }

                    item {
                        if (movies.loadState.append is LoadState.Loading) {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}


//@Preview
//@Composable
//fun WatchlistScreenPreview() {
//    CineLayrTheme {
//        // WatchlistScreen preview
//    }
//}