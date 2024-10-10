package com.an.trailers_compose.data.remote.model

import com.google.gson.annotations.SerializedName

data class Crew(
    val id: Long,
    val name: String,
    val job: String,
    @SerializedName("profile_path")
    val profilePath: String
)