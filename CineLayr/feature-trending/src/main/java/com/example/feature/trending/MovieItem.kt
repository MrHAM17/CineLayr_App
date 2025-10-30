/* UI Version - 01 */
//package com.example.feature.trending
//
//import androidx.compose.ui.semantics.contentDescription
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.material3.Card
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import coil.compose.AsyncImage
//import com.example.domain.model.Movie
//import androidx.compose.ui.platform.testTag
//import androidx.compose.ui.semantics.semantics
//import androidx.compose.ui.unit.sp
//
//@Composable
//fun MovieItem(
//    movie: Movie,
//    onMovieClick: (Int) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .testTag("movie_item_${movie.id}")     // For Unit Test - added here
//            .semantics { contentDescription = "movie_item_${movie.id}" }
//            .clickable { onMovieClick(movie.id) }
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            AsyncImage(
//                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
//                contentDescription = movie.title,
//                modifier = Modifier
//                    .width(90.dp)
//                    .height(140.dp),
//                contentScale = ContentScale.Crop
//            )
//
//            Column(
//                modifier = Modifier.weight(1f),
//                verticalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Text(
//                    text = movie.title,
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.Bold,
//                    fontSize = 16.sp,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//                Text(
//                    text = movie.releaseDate,
//                    style = MaterialTheme.typography.bodyLarge,
//                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//                )
//
//                Text(
//                    text = movie.overview,
//                    style = MaterialTheme.typography.bodyLarge,
//                    maxLines = 3,
//                    overflow = TextOverflow.Ellipsis
//                )
//
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text(
//                        text = "⭐ ${movie.voteAverage}",
//                        style = MaterialTheme.typography.labelLarge
//                    )
//
//                    Text(
//                        text = "(${movie.voteCount} votes)",
//                        style = MaterialTheme.typography.labelLarge,
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
//                    )
//                }
//            }
//        }
//    }
//}

package com.example.feature.trending

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.model.Movie

@Composable
fun MovieItem(movie: Movie, onMovieClick: (Int) -> Unit, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("movie_item_${movie.id}")    // For Tests - added here
            .semantics { contentDescription = "movie_item_${movie.id}" }       // For Tests - added here
            .clickable { onMovieClick(movie.id) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AsyncImage(
                model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(90.dp)
                    .height(130.dp)
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(movie.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(movie.releaseDate, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(movie.overview, maxLines = 3, overflow = TextOverflow.Ellipsis)
                Text("⭐ ${movie.voteAverage} (${movie.voteCount} votes)", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
