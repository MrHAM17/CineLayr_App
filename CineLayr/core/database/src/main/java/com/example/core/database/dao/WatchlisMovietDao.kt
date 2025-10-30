package com.example.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.core.database.entity.WatchlistMovieEntity


@Dao
interface WatchlistMovieDao {

    @Query("SELECT * FROM watchlist_movies ORDER BY createdAt DESC")
    fun getWatchlistMovies(): PagingSource<Int, WatchlistMovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: WatchlistMovieEntity)

    @Query("DELETE FROM watchlist_movies WHERE id = :id")
    suspend fun remove(id: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist_movies WHERE id = :id)")
    suspend fun isInWatchlist(id: Int): Boolean
}
