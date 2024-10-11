package com.an.trailers_compose.ui.list.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.an.trailers_compose.data.local.entity.MovieEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val moviesPager: Pager<Int, MovieEntity>
) : ViewModel() {
    fun getMovieMovies(): Flow<PagingData<MovieEntity>> =
        moviesPager
            .flow
            .cachedIn(viewModelScope)
}