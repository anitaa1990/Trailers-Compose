package com.an.trailers_compose.ui.state

import com.an.trailers_compose.ui.model.Content

sealed class ContentDetailUiState {
    data class Success(val content: Content) : ContentDetailUiState()
    data object Error : ContentDetailUiState()
    data object Loading : ContentDetailUiState()
}
