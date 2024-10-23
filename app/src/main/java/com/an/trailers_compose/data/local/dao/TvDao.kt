package com.an.trailers_compose.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.an.trailers_compose.data.local.entity.TvEntity

@Dao
interface TvDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTvList(tvList: List<TvEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTv(tv: TvEntity)

    @Query("SELECT * FROM TvEntity")
    fun pagingSource(): PagingSource<Int, TvEntity>

    @Query("SELECT * FROM TvEntity")
    fun getTvList(): List<TvEntity>

    @Query("DELETE FROM TvEntity")
    suspend fun clearAll()
}