//package com.example.cinelayr.work
//
//import android.content.Context
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import com.example.core.network.TMDBService
//import org.koin.java.KoinJavaComponent.inject
//
//class RefreshWorker(
//    context: Context,
//    params: WorkerParameters
//) : CoroutineWorker(context, params) {
//
//    private val tmdbService: TMDBService by inject(TMDBService::class.java)
//
//    override suspend fun doWork(): Result {
//        return try {
//            // Refresh trending movies data
//            tmdbService.getTrendingMovies(page = 1)
//            Result.success()
//        } catch (e: Exception) {
//            Result.retry()
//        }
//    }
//}