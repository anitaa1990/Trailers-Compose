package com.an.trailers_compose.ui.search.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.an.trailers_compose.AppConstants
import com.an.trailers_compose.data.repository.SearchRepository
import com.an.trailers_compose.data.source.TvSearchDataSource
import com.an.trailers_compose.ui.model.toContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class TvSearchViewModel @Inject constructor(
    private val repository: SearchRepository,
) : ViewModel() {
    private val _inputText: MutableStateFlow<String> = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val tvSearchResults = inputText
        .filter { it.isNotEmpty() }
        .debounce(300.milliseconds)
        .flatMapLatest { query ->
        Pager(
            config = PagingConfig(
                pageSize = AppConstants.PAGE_SIZE,
                prefetchDistance = AppConstants.PREFETCH_DISTANCE,
                initialLoadSize = AppConstants.PAGE_SIZE,
            ),
            pagingSourceFactory = {
                TvSearchDataSource(repository, query)
            }
        ).flow
            .cachedIn(viewModelScope)
            .map { pagingData ->
                pagingData.map { tv -> tv.toContent() }
            }
    }
    fun updateInput(inputText: String) {
        _inputText.value = inputText
    }

    fun clearInput() {
        _inputText.value = ""
    }
}