package com.example.core.database.di


import com.example.core.database.CineLayrDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val Core_Database_DatabaseModule = module {
    single { CineLayrDatabase.Companion.getInstance(androidContext()) }
    single { get<CineLayrDatabase>().movieDao() }
    single { get<CineLayrDatabase>().watchlistMovieDao() }

}