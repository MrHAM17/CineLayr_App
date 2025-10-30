package com.example.data.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.core.database.CineLayrDatabase
import com.example.core.database.entity.MovieEntity
import com.example.core.database.entity.RemoteKeys
import com.example.core.network.TMDBService
import com.example.data.mapper.toMovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPagingApi::class)
class TrendingMoviesRemoteMediator(
    private val database: CineLayrDatabase,
    private val remoteService: TMDBService
) : RemoteMediator<Int, MovieEntity>() {

    private val movieDao = database.movieDao()

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    nextKey
                }
            }

            val response = remoteService.getTrendingMovies(page)
            val movies = response.results.map { it.toMovieEntity() }

            val endOfPaginationReached = movies.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDao.clearRemoteKeys()
                    movieDao.clearMovies()
                }

                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = movies.map { movie ->
                    RemoteKeys(
                        movieId = movie.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                movieDao.insertAllRemoteKeys(keys)
                movieDao.insertAll(movies)

                Log.d("TrendingMediator", "Inserted ${movies.size} movies")

            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            Log.e("TrendingMediator", "Error loading: ${e.message}")
//            MediatorResult.Error(e)
            // If offline or API fails, DO NOT clear local cache — keep cached data visible
            return MediatorResult.Success(endOfPaginationReached = true)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie ->
                withContext(Dispatchers.IO) {
                    database.movieDao().getRemoteKeysByMovieId(movie.id)
                }
            }
    }
}