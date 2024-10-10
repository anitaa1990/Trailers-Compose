package com.an.trailers_compose.data.remote.model

data class CreditsApiResponse(
    val cast: List<Cast>,
    val crew: List<Crew>
)