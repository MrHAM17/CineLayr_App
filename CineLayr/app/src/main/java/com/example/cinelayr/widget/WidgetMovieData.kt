package com.example.cinelayr.widget

import com.example.domain.model.Movie

data class WidgetMovieData(
    val movieId: Int,
    val title: String,
    val posterPath: String?,
    val voteAverage: Double,
    val releaseYear: String
) {
    companion object {
        fun fromMovie(movie: Movie): WidgetMovieData {
            val releaseYear = movie.releaseDate.takeIf { it.isNotBlank() }
                ?.split("-")
                ?.getOrNull(0)
                ?: ""

            return WidgetMovieData(
                movieId = movie.id,
                title = movie.title,
                posterPath = movie.posterPath,
                voteAverage = movie.voteAverage,
                releaseYear = releaseYear
            )
        }
    }
}