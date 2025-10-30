package com.example.core.database


import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.core.database.dao.MovieDao
import com.example.core.database.dao.WatchlistMovieDao
import com.example.core.database.entity.MovieEntity
import com.example.core.database.entity.RemoteKeys
import com.example.core.database.entity.WatchlistMovieEntity
import java.util.concurrent.Executors

@Database(
    entities = [MovieEntity::class, RemoteKeys::class, WatchlistMovieEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CineLayrDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun watchlistMovieDao(): WatchlistMovieDao

    companion object {
        @Volatile
        private var INSTANCE: CineLayrDatabase? = null

        fun getInstance(context: Context): CineLayrDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CineLayrDatabase::class.java,
                    "cinelayr_database"
                )
                    .fallbackToDestructiveMigration() // <-- wipes DB and recreates
                    .setQueryExecutor(Executors.newSingleThreadExecutor()) // off main thread
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}