package com.example.data.di

import com.example.data.paging.TrendingMoviesRemoteMediator
import com.example.data.repository.MovieRepositoryImpl
import com.example.domain.repository.MovieRepository
import org.koin.dsl.module

val Data_DataModule = module {
    single<TrendingMoviesRemoteMediator> {
        TrendingMoviesRemoteMediator(
            database = get(),
            remoteService = get()
        )
    }

    single<MovieRepository> {
        MovieRepositoryImpl(
            remoteService = get(),
            movieDao = get(),
            watchlistMovieDao = get(),
            remoteMediator = get(),
        )
    }
}