package com.an.trailers_compose.data.repository

import com.an.trailers_compose.data.local.dao.TvDao
import com.an.trailers_compose.data.local.dao.TvRemoteKeyDao
import com.an.trailers_compose.data.local.entity.TvEntity
import com.an.trailers_compose.data.local.entity.TvRemoteKey
import com.an.trailers_compose.data.remote.api.TvApiService
import com.an.trailers_compose.data.remote.model.Category
import com.an.trailers_compose.data.remote.model.TvApiResponse
import com.an.trailers_compose.data.remote.model.api
import javax.inject.Inject

class TvRepository @Inject constructor(
    private val tvDao: TvDao,
    private val remoteKeyDao: TvRemoteKeyDao,
    private val apiService: TvApiService
) {
    suspend fun fetchTvList(
        category: Category,
        nextPage: Long
    ): TvApiResponse {
        return try {
            val response = apiService.fetchTvList(
                type = category.api(),
                page = nextPage
            )
            response
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e)
        }
    }

    private suspend fun fetchTv(remoteId: Long): TvEntity {
        return try {
            apiService.fetchTvDetail(tvId = remoteId)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e)
        }
    }

    suspend fun getTv(remoteId: Long): TvEntity =
        try {
            // Fetch Tv from API
            val tv = fetchTv(remoteId)
            // store Tv in local db
            tvDao.addTv(tv)
            // return tv
            tv
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e)
        }

    suspend fun addTvList(tvList: List<TvEntity>) = tvDao.addTvList(tvList)

    suspend fun deleteAll() = tvDao.clearAll()

    fun getTvPagingSource() = tvDao.pagingSource()

    fun getTvList() = tvDao.getTvList()

    suspend fun addRemoteKeys(remoteKeys: List<TvRemoteKey>) = remoteKeyDao.insertAll(remoteKeys)

    suspend fun getRemoteKeyCreatedTime() = remoteKeyDao.getCreationTime()

    suspend fun getRemoteKeyByTvId(remoteId: Long) = remoteKeyDao.getRemoteKeyByTvId(remoteId)

    suspend fun clearRemoteKeys() = remoteKeyDao.clearRemoteKeys()
}