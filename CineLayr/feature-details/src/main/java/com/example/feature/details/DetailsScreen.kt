package com.example.feature.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.core.designsystem.CineLayrTheme
import com.example.core.designsystem.theme.ui.ThemeViewModel
import com.example.core.designsystem.theme.CineLayrTopBar
import com.example.domain.model.CastMember
import com.example.domain.model.CrewMember
import com.example.domain.model.Genre
import org.koin.androidx.compose.koinViewModel
import com.example.domain.model.MovieDetails
import org.koin.core.parameter.parametersOf
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics


@Composable
fun DetailsRoute(
    movieId: Int, ///
    onBackClick: () -> Unit,
    viewModel: DetailsViewModel = koinViewModel(
        parameters = { parametersOf(movieId) } ///
    )
) {
    val uiState by viewModel.uiState.collectAsState(initial = DetailsUiState.Loading)
    val isInWatchlist by viewModel.isInWatchlist.collectAsState(initial = false)

    DetailsScreen(
        uiState = uiState,
        isInWatchlist = isInWatchlist,
        onBackClick = onBackClick,
        onToggleWatchlist = { viewModel.toggleWatchlist() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    uiState: DetailsUiState,
    isInWatchlist: Boolean,
    onBackClick: () -> Unit,
    onToggleWatchlist: () -> Unit,
    themeViewModel: ThemeViewModel = koinViewModel()
) {
    val isDark by themeViewModel.isDarkMode.collectAsState()

    Scaffold(
        topBar = {
//            TopAppBar(
//                title = { Text("Movie Details") },
//                navigationIcon = {
//                    IconButton(onClick = onBackClick) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
//                    }
//                }
//            )
            CineLayrTopBar(
                title = "Movie Details",
                isDark = isDark,
                onToggleTheme = { themeViewModel.toggleTheme(isDark) },
                onBackClick = onBackClick,  // Show back button
//                modifier = Modifier
//                    .testTag("details_topbar_toggle")  // Added testTag for baseline profile
//                    .semantics { contentDescription = "details_topbar_toggle" }
            )

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onToggleWatchlist,
                modifier = Modifier
                    .padding(WindowInsets.navigationBars.asPaddingValues())
                    .testTag("details_fab")    // For Tests - added here
                    .semantics { contentDescription = "details_fab" }
            ) {
                Icon(
                    imageVector = if (isInWatchlist) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isInWatchlist) "Remove from watchlist" else "Add to watchlist"
                )
            }
        }
    ) { paddingValues ->
        when (uiState) {
            is DetailsUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is DetailsUiState.Success -> {
                MovieDetailsContent(
                    movieDetails = uiState.movieDetails,
                    modifier = Modifier
                        .padding(paddingValues)
                        .testTag("details_scrollable") // Added testTag for baseline profile
                        .semantics { contentDescription = "details_scrollable" }
                )
            }
            is DetailsUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(uiState.message)
                }
            }
        }
    }
}

@Composable
fun MovieDetailsContent(
    movieDetails: MovieDetails,
    modifier: Modifier = Modifier
) {
//    val bottomPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 64.dp

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                bottom = 128.dp, //  ensures content not hidden behind FAB
//                bottom = bottomPadding, // dynamic bottom padding
            )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            val backdropUrl = "https://image.tmdb.org/t/p/w780${movieDetails.backdropPath}"
            Image(
                painter = rememberAsyncImagePainter(backdropUrl),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(16.dp)
                .padding(
//                    bottom = 112.dp, //  ensures content not hidden behind FAB
                    start = 12.dp,
                    end = 12.dp,
                    top = 4.dp
                )
        ) {
            Text(
                text = movieDetails.title,
                style = MaterialTheme.typography.displayLarge,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${movieDetails.releaseDate} • ${movieDetails.voteAverage}/10",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = movieDetails.overview,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (movieDetails.genres.isNotEmpty()) {
                Text(
                    text = "Genres: ${movieDetails.genres.joinToString { it.name }}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (movieDetails.cast.isNotEmpty()) {
                Text(
                    text = "Cast:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                movieDetails.cast.take(5).forEach { cast ->
                    Text(
                        text = "• ${cast.name} as ${cast.character}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun DetailsScreenPreview() {
//    CineLayrTheme {
//        DetailsScreen(
//            uiState = DetailsUiState.Success(
//                movieDetails = MovieDetails(
//                    id = 1,
//                    title = "Inception",
//                    overview = "A thief who steals corporate secrets through dream-sharing technology.",
//                    releaseDate = "2010-07-16",
//                    voteAverage = 8.8,
//                    backdropPath = "/s3TBrRGB1iav7gFOCNx3H31MoES.jpg",
//                    genres = listOf(Genre(1,"Sci-Fi")),
//                    cast = listOf(
//                        CastMember(1,"Leonardo DiCaprio","Cobb", "hi-fi"),
//                    ),
//                    posterPath = "",
//                    voteCount = 233,
//                    runtime = 344,
//                    crew = listOf(
//                        CrewMember(1,"Joseph Gordon-Levitt", "Arthur", "Tech")
//                    ),
//                    videos = listOf()
//                )
//            ),
//            isInWatchlist = true,
//            onBackClick = {},
//            onToggleWatchlist = {}
//        )
//    }
//}
