package com.example.feature.trending.di

import com.example.feature.trending.TrendingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val FeatureTrending_TrendingModule = module {
    viewModel { TrendingViewModel(get()) } // inject repository
}
