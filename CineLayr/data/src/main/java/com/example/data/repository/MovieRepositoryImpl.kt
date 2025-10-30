package com.example.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.core.database.dao.MovieDao
import com.example.core.database.dao.WatchlistMovieDao
import com.example.core.network.TMDBService
import com.example.data.mapper.toMovie
import com.example.data.mapper.toMovieDetails
import com.example.data.mapper.toMovieEntity
import com.example.data.mapper.toWatchlistMovieEntity
import com.example.data.paging.TrendingMoviesRemoteMediator
import com.example.domain.model.Movie
import com.example.domain.model.MovieDetails
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(androidx.paging.ExperimentalPagingApi::class)
class MovieRepositoryImpl(
    private val remoteService: TMDBService,
    private val movieDao: MovieDao,
    private val watchlistMovieDao: WatchlistMovieDao,
    private val remoteMediator: TrendingMoviesRemoteMediator
) : MovieRepository {

    override fun getTrendingMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = { movieDao.getTrendingPagingSource() }
        ).flow.map { pagingData ->
            pagingData.map { it.toMovie() }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetails? {
        return try {
            val response = remoteService.getMovieDetails(movieId)
            response.toMovieDetails()
        } catch (e: Exception) {
            // Fallback to local data if available
            movieDao.getMovieById(movieId)?.let { entity ->
                MovieDetails(
                    id = entity.id,
                    title = entity.title,
                    overview = entity.overview,
                    posterPath = entity.posterPath,
                    backdropPath = entity.backdropPath,
                    releaseDate = entity.releaseDate,
                    voteAverage = entity.voteAverage,
                    voteCount = entity.voteCount,
                    runtime = null,
                    genres = emptyList(),
                    cast = emptyList(),
                    crew = emptyList(),
                    videos = emptyList()
                )
            }
        }
    }

    override suspend fun toggleWatchlistStatus(movieId: Int, isInWatchlist: Boolean) {
        if (isInWatchlist) {
            // --- ADD to watchlist table ---
            val localMovie = movieDao.getMovieById(movieId)
            if (localMovie != null) {
                // already in local DB → add to watchlist
                watchlistMovieDao.insert(localMovie.toWatchlistMovieEntity())
                // also update localMovie row flag so it reflects current user choice
                movieDao.updateWatchlistStatus(movieId, true)
            } else {
                // edge case: movie not in local DB (e.g., opened via widget/deep link)
                // so if it is missing, fetch minimal details then insert into watchlist only
                val detailsResponse = remoteService.getMovieDetails(movieId)
                val details = detailsResponse.toMovieDetails()
                val movieEntity = details.toMovieEntity(isInWatchlist = true)
                watchlistMovieDao.insert(movieEntity.toWatchlistMovieEntity())
                // optionally insert into movie table as well to keep cache consistent
                movieDao.insert(movieEntity)

            }
        } else {
            // --- REMOVE from watchlist table ---
            watchlistMovieDao.remove(movieId)
            // update local movie table row flag if cached row exists
            val localMovie = movieDao.getMovieById(movieId)
            if (localMovie != null) {
                movieDao.updateWatchlistStatus(movieId, false)
            }
        }
    }

    override fun getWatchlistMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
//            pagingSourceFactory = { movieDao.getWatchlistPagingSource() }
            pagingSourceFactory = { watchlistMovieDao.getWatchlistMovies() }
        ).flow.map { pagingData ->
            pagingData.map { it.toMovie() }
        }
    }

    override suspend fun isMovieInWatchlist(movieId: Int): Boolean {
        return watchlistMovieDao.isInWatchlist(movieId)
    }

    override suspend fun getRandomTrendingMovie(): Movie? {
        return try {
            // Get first page of trending movies and pick a random one
            val response = remoteService.getTrendingMovies(page = 1)
            response.results.randomOrNull()?.toMovie()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getTrendingMoviesPage1Unique(): List<Movie>? {
        return try {
            val response = remoteService.getTrendingMovies(page = 1)
            response.results.distinctBy { it.id }.map { it.toMovie() }
        } catch (e: Exception) {
            null
        }
    }

}