package com.example.feature.watchlist.di

import com.example.feature.watchlist.WatchlistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val FeatureWatchlist_WatchlistModule = module {
    viewModel { WatchlistViewModel(get()) } // inject repository
}
