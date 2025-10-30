package com.example.feature.trending

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.model.Movie
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class TrendingViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    val movies: Flow<PagingData<Movie>> = repository
        .getTrendingMovies()
        .cachedIn(viewModelScope)
}