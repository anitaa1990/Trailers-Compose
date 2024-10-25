package com.an.trailers_compose.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.data.repository.SearchRepository
import javax.inject.Inject

class MovieSearchDataSource @Inject constructor (
    private val repository: SearchRepository,
    private val query: String
): PagingSource<Long, MovieEntity>() {
    override fun getRefreshKey(state: PagingState<Long, MovieEntity>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, MovieEntity> {
        val nextPage = params.key ?: 1L
        val apiResponse = repository.searchMovies(query, nextPage)
        return try {
            if (apiResponse.results.isNotEmpty()) {
                LoadResult.Page(
                    data = apiResponse.results,
                    prevKey = if (nextPage == 1L) null else nextPage - 1,
                    nextKey = nextPage.plus(1)
                )
            } else LoadResult.Error(Exception("No results found"))
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}