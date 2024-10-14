package com.an.trailers_compose.ui.detail.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.trailers_compose.AppConstants.ROUTE_DETAIL_ARG_NAME
import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: MovieRepository,
) : ViewModel() {
    private val remoteId: Long = requireNotNull(savedStateHandle[ROUTE_DETAIL_ARG_NAME])

    // This is a mutable state flow that will be used internally in the viewmodel,
    private val _movieUiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)

    // Immutable state flow that is exposed to the UI
    val movieUiState = _movieUiState.asStateFlow()

    init { getMovie() }

    private fun getMovie() = viewModelScope.launch {
        _movieUiState.update {
            try {
                MovieDetailUiState.Success(repository.getMovie(remoteId))
            } catch (e: Exception) { MovieDetailUiState.Error }
        }
    }

    sealed class MovieDetailUiState {
        data class Success(val movie: MovieEntity) : MovieDetailUiState()
        data object Error : MovieDetailUiState()
        data object Loading : MovieDetailUiState()
    }
}