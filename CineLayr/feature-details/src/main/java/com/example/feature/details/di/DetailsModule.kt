package com.example.feature.details.di


import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.feature.details.DetailsViewModel

val FeatureDetails_DetailsModule = module {

//    viewModel { (savedStateHandle: SavedStateHandle) ->
//        DetailsViewModel(get(), savedStateHandle)
//    }
    ///
    viewModel { (movieId: Int) ->
        DetailsViewModel(repository = get(), movieId = movieId)
    }
}
