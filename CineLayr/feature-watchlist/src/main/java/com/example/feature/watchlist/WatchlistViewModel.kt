package com.example.feature.watchlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class WatchlistViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    val watchlistMovies: Flow<PagingData<Movie>> = repository
        .getWatchlistMovies()
        .distinctUntilChanged() // Avoid Empty Flicker - This prevents clearing the UI before new data arrives.
        .cachedIn(viewModelScope)
}

//Pager(...).flow.cachedIn(viewModelScope)
