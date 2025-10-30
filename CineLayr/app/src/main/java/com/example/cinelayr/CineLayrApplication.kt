package com.example.cinelayr

import android.app.Application
import com.example.cinelayr.di.KoinInitializer
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
import org.koin.core.context.startKoin
import org.koin.core.context.GlobalContext

class CineLayrApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // // Start Koin - Load all modules synchronously to have GlobalContext available
//        startKoin {
//            androidContext(this@CineLayrApplication)
//            modules(
//                // include manually all
//                FeatureTrending_TrendingModule,
//                FeatureDetails_DetailsModule,
//                FeatureWatchlist_WatchlistModule,
//
//                Core_Network_NetworkModule,
//                Core_Database_DatabaseModule,
//                Core_DesignSystem_ThemeModule,
//
//                Data_DataModule
//            )
//        }

        // //Start Koin minimally to have GlobalContext available
        //        startKoin {
//            androidContext(this@CineLayrApplication)
//            modules(emptyList()
//            ) // Start main or empty first
//        }
//
//        // Initialize modules asynchronously to avoid blocking UI
//        CoroutineScope(Dispatchers.Default).launch {
//            GlobalContext.get().loadModules(
//                listOf(
//                    Core_DesignSystem_ThemeModule,
//                    Core_Network_NetworkModule,
//                    Data_DataModule,
//                    FeatureTrending_TrendingModule,
//                    FeatureDetails_DetailsModule,
//                    Core_Database_DatabaseModule,
//                    FeatureWatchlist_WatchlistModule
//                )
//            )
//        }
        KoinInitializer.initAppAsync(this)
    }
}
