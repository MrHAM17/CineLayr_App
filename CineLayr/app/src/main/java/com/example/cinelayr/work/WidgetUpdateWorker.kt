package com.example.cinelayr.work

import android.content.Context
import androidx.glance.appwidget.updateAll
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.cinelayr.di.KoinInitializer
import com.example.cinelayr.widget.MovieWidget
import com.example.cinelayr.widget.WidgetMovieData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import java.util.concurrent.TimeUnit
import com.example.domain.repository.MovieRepository
import com.google.gson.Gson
import org.koin.core.context.startKoin
import com.example.core.database.di.Core_Database_DatabaseModule
import com.example.core.designsystem.theme.di.Core_DesignSystem_ThemeModule
import com.example.core.network.di.Core_Network_NetworkModule
import com.example.data.di.Data_DataModule
import com.example.feature.details.di.FeatureDetails_DetailsModule
import com.example.feature.trending.di.FeatureTrending_TrendingModule
import com.example.feature.watchlist.di.FeatureWatchlist_WatchlistModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext


class WidgetUpdateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    private val repository: MovieRepository by inject(
        MovieRepository::class.java
    )

    override suspend fun doWork(): Result {
        try {

            // Ensure Koin started before any widget content loads
            // Synchronous initialization to avoid null injection or Koin crash
            KoinInitializer.initSync(applicationContext)

            // Fetch new movie data in background thread
            val movie = withContext(Dispatchers.IO) {
                repository.getRandomTrendingMovie()
            }

            // Force refresh by getting new trending movie
            movie?.let {
                val data = WidgetMovieData.fromMovie(it)
                val prefs = applicationContext.getSharedPreferences("widget_cache", Context.MODE_PRIVATE)
                prefs.edit().putString("last_movie", Gson().toJson(data)).apply()
                MovieWidget().updateAll(applicationContext)
            }

            // Update all widgets
//            MovieWidget().updateAll(applicationContext)

            return Result.success()
        } catch (e: Exception) {
           return Result.retry()
        }
    }

    companion object {
        private const val WORKER_TAG = "movie_widget_update"

        fun schedulePeriodicUpdates(context: Context) {
            val updateRequest = PeriodicWorkRequestBuilder<WidgetUpdateWorker>(
                24, // Repeat every 24 hours
                TimeUnit.HOURS
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORKER_TAG,
                ExistingPeriodicWorkPolicy.KEEP,
                updateRequest
            )
        }

        fun cancelUpdates(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORKER_TAG)
        }
    }
}