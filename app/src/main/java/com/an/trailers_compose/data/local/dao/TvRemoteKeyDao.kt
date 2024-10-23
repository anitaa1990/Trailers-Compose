package com.an.trailers_compose.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.an.trailers_compose.data.local.entity.TvRemoteKey

@Dao
interface TvRemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<TvRemoteKey>)

    @Query("Select * From tv_remote_key Where tv_id = :id")
    suspend fun getRemoteKeyByTvId(id: Long): TvRemoteKey?

    @Query("Delete From tv_remote_key")
    suspend fun clearRemoteKeys()

    @Query("Select created_at From tv_remote_key Order By created_at DESC LIMIT 1")
    suspend fun getCreationTime(): Long?
}
