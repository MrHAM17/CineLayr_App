package com.example.feature.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.MovieDetails
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DetailsViewModel(
    private val repository: MovieRepository,
//    savedStateHandle: SavedStateHandle ///
    ///
    movieId: Int

) : ViewModel() {

//    private val movieId = savedStateHandle.get<String>(DetailsDestination.movieIdArg)?.toIntOrNull() ?: 0  ///
    ///
    private val movieIdProperty = movieId     // Store movieId as a property

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    private val _isInWatchlist = MutableStateFlow(false)
    val isInWatchlist = _isInWatchlist.asStateFlow()

    init {
//        loadMovieDetails()
//        checkWatchlistStatus()
        loadMovieDetails(movieIdProperty )
        checkWatchlistStatus(movieIdProperty )
    }

//    private fun loadMovieDetails() {
    private fun loadMovieDetails(movieId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            try {
                val movieDetails = repository.getMovieDetails(movieId)
                if (movieDetails != null) {
                    _uiState.value = DetailsUiState.Success(movieDetails)
                } else {
                    _uiState.value = DetailsUiState.Error("Movie not found")
                }
            } catch (e: Exception) {
                _uiState.value = DetailsUiState.Error("Failed to load movie details")
            }
        }
    }

//    private fun checkWatchlistStatus() {
    private fun checkWatchlistStatus(movieId: Int) {
    viewModelScope.launch {
            _isInWatchlist.value = repository.isMovieInWatchlist(movieId)
        }
    }

    fun toggleWatchlist() {
        viewModelScope.launch {
            val currentStatus = _isInWatchlist.value
//            repository.toggleWatchlistStatus(movieId, !currentStatus)
            repository.toggleWatchlistStatus(movieId = movieIdProperty , isInWatchlist = !currentStatus)
            _isInWatchlist.value = !currentStatus
        }
    }
}

sealed interface DetailsUiState {
    object Loading : DetailsUiState
    data class Success(val movieDetails: MovieDetails) : DetailsUiState
    data class Error(val message: String) : DetailsUiState
}