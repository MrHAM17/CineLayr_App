package com.example.domain.repository

import androidx.paging.PagingData
import com.example.domain.model.Movie
import com.example.domain.model.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getTrendingMovies(): Flow<PagingData<Movie>>
    suspend fun getMovieDetails(movieId: Int): MovieDetails?
    suspend fun toggleWatchlistStatus(movieId: Int, isInWatchlist: Boolean)
    fun getWatchlistMovies(): Flow<PagingData<Movie>>
    suspend fun isMovieInWatchlist(movieId: Int): Boolean
    suspend fun getRandomTrendingMovie(): Movie?
    suspend fun getTrendingMoviesPage1Unique(): List<Movie>?
}
