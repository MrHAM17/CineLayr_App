package com.example.cinelayr.widget

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import kotlinx.coroutines.runBlocking
import org.koin.java.KoinJavaComponent.inject
import androidx.compose.ui.unit.dp
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.action.actionStartActivity
//import androidx.glance.unit.dp
import androidx.glance.unit.Dimension
import androidx.room.util.copy
import com.example.cinelayr.MainActivity
import com.example.cinelayr.R
import com.example.domain.repository.MovieRepository
import com.example.feature.trending.MovieItem
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import com.google.gson.Gson


class MovieWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetContent(context)
        }
    }
}

@Composable
private fun WidgetContent(context: Context) {
    val widgetData = remember { loadWidgetData(context) }   // For 1 item
//    val widgetData = remember { loadWidgetData2(context) }  // For list - 5 items

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(GlanceTheme.colors.background)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (widgetData != null) {
            MovieWidgetContent(movieData = widgetData)     // For 1 item
//            MovieWidgetContent2(movieList = widgetData)  // For list - 5 items
        } else {
            EmptyWidgetContent()
        }
    }
}

@Composable
private fun MovieWidgetContent(movieData: WidgetMovieData) {

    // Create intent to open app and navigate to Details
    val context = LocalContext.current
    val intent = Intent(context, MainActivity::class.java).apply {
        putExtra("movieId", movieData.movieId)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .clickable(onClick = actionStartActivity(intent)),

        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Movie Poster
        Box(
            modifier = GlanceModifier
                .defaultWeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (movieData.posterPath != null) {
//                val imageUrl = "https://image.tmdb.org/t/p/w300${movieData.posterPath}"
                val drawableRes = R.drawable.image_placeholder_rounded
                androidx.glance.Image(
                    provider = ImageProvider(drawableRes),
                    contentDescription = movieData.title,
                    modifier = GlanceModifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            } else {
                PlaceholderPoster()
            }
        }

        Spacer(modifier = GlanceModifier.height(8.dp))

        // Movie Title
        Text(
            text = movieData.title,
            style = TextStyle(
                color = GlanceTheme.colors.onBackground,
                fontWeight = FontWeight.Medium
            ),
            maxLines = 2,
            modifier = GlanceModifier.fillMaxWidth()
        )

        Spacer(modifier = GlanceModifier.height(4.dp))

        // Movie Details
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rating
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⭐",
                    style = TextStyle(
                        color = GlanceTheme.colors.onBackground
                    )
                )
                Spacer(modifier = GlanceModifier.width(4.dp))
                Text(
                    text = "%.1f".format(movieData.voteAverage),
                    style = TextStyle(
                        color = GlanceTheme.colors.onBackground,
                        fontWeight = FontWeight.Normal
                    )
                )
            }

            Spacer(modifier = GlanceModifier.defaultWeight())

            // Release Year
            if (movieData.releaseYear.isNotBlank()) {
                Text(
                    text = movieData.releaseYear,
                    style = TextStyle(
//                        color = GlanceTheme.colors.onBackground.copy(alpha = 0.7f),
                        color = GlanceTheme.colors.onBackground,
                        fontWeight = FontWeight.Normal
                    )
                )
            }
        }
    }
}

private fun loadWidgetData(context: Context): WidgetMovieData? {
    return runBlocking {
       try {
            val prefs = context.getSharedPreferences("widget_cache", Context.MODE_PRIVATE)
            val repository: MovieRepository by inject(MovieRepository::class.java)    // Using Koin to get repository instance

            val widgetData =
                try {
                    repository.getRandomTrendingMovie()?.let { WidgetMovieData.fromMovie(it) }
                } catch (_: Exception) {
                    null
                }

            val finalData =
                widgetData
                    ?: prefs.getString("last_movie", null)?.let {
                        try {
                            Gson().fromJson(it, WidgetMovieData::class.java)
                        } catch (_: Exception) {
                            null
                        }
                    }
                    ?: WidgetMovieData(0, "Error- No Movie", "", 0.0, "0")

            // Cache it
            prefs.edit().putString("last_movie", Gson().toJson(finalData)).apply()

            finalData

       } catch (e: Exception) {
//            Log.e("MovieWidget", "Failed to load movie", e)
            WidgetMovieData(0,"Exception-No Movie","",0.0,"0")
        }
    }
}

@Composable
private fun EmptyWidgetContent() {
    Column(
        modifier = GlanceModifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎬",
            style = TextStyle(
                color = GlanceTheme.colors.onBackground
            )
        )
        Spacer(modifier = GlanceModifier.height(8.dp))
        Text(
            text = "No trending movies",
            style = TextStyle(
                color = GlanceTheme.colors.onBackground,
                fontWeight = FontWeight.Medium
            )
        )
        Text(
            text = "Open app to refresh",
            style = TextStyle(
                color =
//                    GlanceTheme.colors.onBackground.copy(alpha = 0.7f)
//                    GlanceModifier.alpha(0.7f)
                    GlanceTheme.colors.onBackground

            )
        )
    }
}

@Composable
private fun PlaceholderPoster() {
    Box(
        modifier = GlanceModifier
            .fillMaxWidth()
            .height(120.dp)
            .background(GlanceTheme.colors.surface),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🎭",
            style = TextStyle(
                color = GlanceTheme.colors.onSurface
            )
        )
    }
}


@Composable
private fun MovieWidgetContent2(movieList: List<WidgetMovieData>) {
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .padding(8.dp),
        verticalAlignment = Alignment.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🔥 Trending Movies",
            style = TextStyle(
                color = GlanceTheme.colors.onBackground,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1
        )

        Spacer(modifier = GlanceModifier.height(8.dp))

        // Show up to 5 movies (compact card style)
        movieList.take(5).forEach { movie ->
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalAlignment = Alignment.Start
            ) {
                // Poster placeholder (since Glance can't load network images yet)
                Box(
                    modifier = GlanceModifier
                        .width(40.dp)
                        .height(60.dp)
                        .background(GlanceTheme.colors.surface),
                    contentAlignment = Alignment.Center
                ) {
                    val emoji = listOf("🎬", "🎭", "⭐", "🎥", "🍿").random()
                    Text(text = emoji)
//                    Text(text = "🎬")
                }

                Spacer(modifier = GlanceModifier.width(8.dp))

                Column(
                    modifier = GlanceModifier.defaultWeight()
                ) {
                    Text(
                        text = movie.title,
                        style = TextStyle(
                            color = GlanceTheme.colors.onBackground,
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1
                    )
                    Text(
                        text = "⭐ %.1f  •  ${movie.releaseYear}".format(movie.voteAverage),
                        style = TextStyle(
                            color = GlanceTheme.colors.onBackground
                        ),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

private fun loadWidgetData2(context: Context): List<WidgetMovieData> {
    return runBlocking {
        try {
            val prefs = context.getSharedPreferences("widget_cache", Context.MODE_PRIVATE)
            val repository: MovieRepository by inject(MovieRepository::class.java)

            val widgetList =
                try {
                    repository.getTrendingMoviesPage1Unique()
                        ?.map { WidgetMovieData.fromMovie(it) }
                        ?.take(5)
                } catch (_: Exception) {
                    null
                }

            val finalList = widgetList
                ?: prefs.getString("last_movies", null)?.let {
                    try {
                        Gson().fromJson(it, Array<WidgetMovieData>::class.java).toList()
                    } catch (_: Exception) {
                        null
                    }
                }
                ?: listOf(WidgetMovieData(0, "Error-No Movie", "", 0.0, "0"))

            // Cache it
            prefs.edit().putString("last_movies", Gson().toJson(finalList)).apply()

            finalList

        } catch (e: Exception) {
            listOf(WidgetMovieData(0, "Exception-No Movie", "", 0.0, "0"))
        }
    }
}


/*
1. Widget is safe now: it won’t crash if the repo returns null or if a network/database error happens.
2. runBlocking usage - It’s fine here because widget updates are already background/suspending context. It won’t freeze the main app UI.
3. Avoid runBlocking if scaling - For now fine, but if the widget fetch grows, consider using GlanceAppWidgetReceiver’s coroutine scope.
*/