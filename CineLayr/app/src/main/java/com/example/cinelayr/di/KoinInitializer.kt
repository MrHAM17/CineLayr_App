package com.example.cinelayr.di

import android.content.Context
import com.example.core.database.di.Core_Database_DatabaseModule
import com.example.core.designsystem.theme.di.Core_DesignSystem_ThemeModule
import com.example.core.network.di.Core_Network_NetworkModule
import com.example.data.di.Data_DataModule
import com.example.feature.details.di.FeatureDetails_DetailsModule
import com.example.feature.trending.di.FeatureTrending_TrendingModule
import com.example.feature.watchlist.di.FeatureWatchlist_WatchlistModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module



// Same as final version (but un common code)
//object KoinInitializer {
//
//    /**
//     * Initialize Koin for the app asynchronously (fast startup)
//     */
//    fun initAppAsync(context: Context) {
//        // Only start Koin if not already started
//        if (GlobalContext.getOrNull() != null) return
//
//        synchronized(this) {
//            if (GlobalContext.getOrNull() != null) return
//
//            // Start Koin minimally first
//            startKoin {
//                androidContext(context)
//                modules(emptyList()) // start empty first
//            }
//
//            // Load all modules async to avoid blocking main thread
//            CoroutineScope(Dispatchers.Default).launch {
//                GlobalContext.get().loadModules(
//                    listOf(
//                        Core_DesignSystem_ThemeModule,
//                        Core_Network_NetworkModule,
//                        Data_DataModule,
//                        FeatureTrending_TrendingModule,
//                        FeatureDetails_DetailsModule,
//                        Core_Database_DatabaseModule,
//                        FeatureWatchlist_WatchlistModule
//                    )
//                )
//            }
//        }
//    }
//
//    /**
//     * Initialize Koin synchronously (safe for widgets / background)
//     */
//    fun initSync(context: Context) {
//        if (GlobalContext.getOrNull() != null) return
//
//        synchronized(this) {
//            if (GlobalContext.getOrNull() != null) return
//
//            // Start Koin fully with all modules
//            startKoin {
//                androidContext(context)
//                modules(
//                    listOf(
//                        Core_DesignSystem_ThemeModule,
//                        Core_Network_NetworkModule,
//                        Data_DataModule,
//                        FeatureTrending_TrendingModule,
//                        FeatureDetails_DetailsModule,
//                        Core_Database_DatabaseModule,
//                        FeatureWatchlist_WatchlistModule
//                    )
//                )
//            }
//        }
//    }
//}


object KoinInitializer {

    // All your app modules in one place
    private val allModules = listOf(
        Core_DesignSystem_ThemeModule,
        Core_Network_NetworkModule,
        Data_DataModule,
        FeatureTrending_TrendingModule,
        FeatureDetails_DetailsModule,
        Core_Database_DatabaseModule,
        FeatureWatchlist_WatchlistModule
    )

    /**
     * Initialize Koin asynchronously (fast startup for app)
     */
    fun initAppAsync(context: Context) {
        //        // Only start Koin if not already started
        if (GlobalContext.getOrNull() != null) return

        synchronized(this) {
            if (GlobalContext.getOrNull() != null) return

            // Minimal start
            startKoin { androidContext(context); modules(emptyList()) }

            // Load all modules in background async to avoid blocking main thread
            CoroutineScope(Dispatchers.Default).launch {
                GlobalContext.get().loadModules(allModules)
            }
        }
    }

    /**
     * Initialize Koin synchronously (safe for widgets / background)
     */
    fun initSync(context: Context) {
        if (GlobalContext.getOrNull() != null) return

        synchronized(this) {
            if (GlobalContext.getOrNull() != null) return

            // Full start with all modules
            startKoin {
                androidContext(context)
                modules(allModules)
            }
        }
    }
}
