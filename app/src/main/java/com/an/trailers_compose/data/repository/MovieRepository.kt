package com.an.trailers_compose.data.repository

import com.an.trailers_compose.data.local.dao.MovieDao
import com.an.trailers_compose.data.local.dao.MovieRemoteKeyDao
import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.data.local.entity.MovieRemoteKey
import com.an.trailers_compose.data.remote.api.MovieApiService
import com.an.trailers_compose.data.remote.model.Category
import com.an.trailers_compose.data.remote.model.MovieApiResponse
import com.an.trailers_compose.data.remote.model.api
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val remoteKeyDao: MovieRemoteKeyDao,
    private val movieApiService: MovieApiService
) {
    suspend fun fetchMovies(
        category: Category,
        nextPage: Long
    ): MovieApiResponse {
        return try {
            val response = movieApiService.fetchMovies(
                type = category.api(),
                page = nextPage
            )
            response
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e)
        }
    }

    private suspend fun fetchMovie(remoteId: Long): MovieEntity {
        return try {
            movieApiService.fetchMovieDetail(movieId = remoteId)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e)
        }
    }

    suspend fun getMovie(remoteId: Long): MovieEntity =
        try {
            // Fetch movie from API
            val movie = fetchMovie(remoteId)
            // store movie in local db
            movieDao.addMovie(movie)
            // return movie
            movie
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e)
        }

    suspend fun addMovies(movies: List<MovieEntity>) = movieDao.addMovies(movies)

    suspend fun deleteAll() = movieDao.clearAll()

    fun getMoviesPagingSource() = movieDao.pagingSource()

    fun getMovies() = movieDao.getMovies()

    suspend fun addRemoteKeys(remoteKeys: List<MovieRemoteKey>) = remoteKeyDao.insertAll(remoteKeys)

    suspend fun getRemoteKeyCreatedTime() = remoteKeyDao.getCreationTime()

    suspend fun getRemoteKeyByMovieId(remoteId: Long) = remoteKeyDao.getRemoteKeyByMovieId(remoteId)

    suspend fun clearRemoteKeys() = remoteKeyDao.clearRemoteKeys()
}