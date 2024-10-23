package com.an.trailers_compose.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("tv_remote_key")
data class TvRemoteKey(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "tv_id")
    val movieId: Long,
    val prevKey: Int?,
    val currentPage: Int,
    val nextKey: Int?,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
