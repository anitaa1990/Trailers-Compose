package com.an.trailers_compose.ui.search

import androidx.lifecycle.ViewModel
import com.an.trailers_compose.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: SearchRepository,
) : ViewModel() {

}