package com.an.trailers_compose.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.an.trailers_compose.data.local.entity.MovieEntity

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovies(movies: List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity")
    fun pagingSource(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM MovieEntity")
    fun getMovies(): List<MovieEntity>

    @Query("DELETE FROM MovieEntity")
    suspend fun clearAll()
}