package com.example.data

import com.example.core.database.dao.MovieDao
import com.example.core.database.dao.WatchlistMovieDao
import com.example.core.network.TMDBService
import com.example.data.repository.MovieRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.Test

class MovieRepositoryImplTest {

    private val service = mockk<TMDBService>()
    private val movieDao = mockk<MovieDao>(relaxed = true)
    private val watchlistDao = mockk<WatchlistMovieDao>(relaxed = true)
    private val mediator = mockk<com.example.data.paging.TrendingMoviesRemoteMediator>(relaxed = true)

    @Test
    fun getRandomTrendingMovieReturnsNullOnError() = runBlocking {
        coEvery { service.getTrendingMovies(1) } throws Exception("error")
        val repo = MovieRepositoryImpl(service, movieDao, watchlistDao, mediator)
        val result = repo.getRandomTrendingMovie()
        assertNull(result)
    }
}
