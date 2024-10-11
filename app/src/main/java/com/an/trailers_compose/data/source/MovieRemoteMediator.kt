package com.an.trailers_compose.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.data.local.entity.MovieRemoteKey
import com.an.trailers_compose.data.remote.model.Category
import com.an.trailers_compose.data.repository.MovieRepository
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator @Inject constructor(
    private val category: Category,
    private val repository: MovieRepository
) : RemoteMediator<Long, MovieEntity>() {
    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

        return if (System.currentTimeMillis() - (repository.getRemoteKeyCreatedTime() ?: 0) < cacheTimeout) {
            // Cached data is up-to-date, so there is no need to re-fetch from the network.
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            // Need to refresh cached data from network; returning
            // LAUNCH_INITIAL_REFRESH here will also block RemoteMediator's
            // APPEND and PREPEND from running until REFRESH succeeds.
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    /**.
     *
     * @param state This gives us information about the pages that were loaded before,
     * the most recently accessed index in the list, and the PagingConfig we defined when initializing the paging stream.
     * @param loadType this tells us whether we need to load data at the end (LoadType.APPEND)
     * or at the beginning of the data (LoadType.PREPEND) that we previously loaded,
     * or if this the first time we're loading data (LoadType.REFRESH).
     */
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Long, MovieEntity>
    ): MediatorResult {
        val page: Int = when (loadType) {
            LoadType.REFRESH -> {
                //New Query so clear the DB
                val remoteKeys = state.getRemoteKeyClosestToCurrentPosition()
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = state.getRemoteKeyForFirstItem()
                // If remoteKeys is null, that means the refresh result is not in the database yet.
                val prevKey = remoteKeys?.prevKey
                prevKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
            LoadType.APPEND -> {
                val remoteKeys = state.getRemoteKeyForLastItem()

                // If remoteKeys is null, that means the refresh result is not in the database yet.
                // We can return Success with endOfPaginationReached = false because Paging
                // will call this method again if RemoteKeys becomes non-null.
                // If remoteKeys is NOT NULL but its nextKey is null, that means we've reached
                // the end of pagination for append.
                val nextKey = remoteKeys?.nextKey
                nextKey ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val apiResponse = repository.fetchMovies(category, nextPage = page.toLong())

            val movies = apiResponse.results
            val endOfPaginationReached = movies.isEmpty()

            if (loadType == LoadType.REFRESH) {
                repository.clearRemoteKeys()
                repository.deleteAll()
            }

            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (endOfPaginationReached) null else page + 1
            val remoteKeys = movies.map {
                MovieRemoteKey(
                    movieId = it.remoteId,
                    prevKey = prevKey,
                    currentPage = page,
                    nextKey = nextKey
                )
            }

            repository.addRemoteKeys(remoteKeys)
            repository.addMovies(movies)

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (error: IOException) {
            return MediatorResult.Error(error)
        } catch (error: HttpException) {
            return MediatorResult.Error(error)
        } catch (error: Exception) {
            return MediatorResult.Error(error)
        }
    }

    /** LoadType.Append
     * When we need to load data at the end of the currently loaded data set, the load parameter is LoadType.APPEND
     */
    private suspend fun PagingState<Long, MovieEntity>.getRemoteKeyForLastItem(): MovieRemoteKey? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return this.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { movie ->
            repository.getRemoteKeyByMovieId(movie.remoteId)
        }
    }

    /** LoadType.Prepend
     * When we need to load data at the beginning of the currently loaded data set, the load parameter is LoadType.PREPEND
     */
    private suspend fun PagingState<Long, MovieEntity>.getRemoteKeyForFirstItem(): MovieRemoteKey? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return this.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { movie ->
            repository.getRemoteKeyByMovieId(movie.remoteId)
        }
    }

    /** LoadType.REFRESH
     * Gets called when it's the first time we're loading data, or when PagingDataAdapter.refresh() is called;
     * so now the point of reference for loading our data is the state.anchorPosition.
     * If this is the first load, then the anchorPosition is null.
     * When PagingDataAdapter.refresh() is called, the anchorPosition is the first visible position in the displayed list, so we will need to load the page that contains that specific item.
     */
    private suspend fun PagingState<Long, MovieEntity>.getRemoteKeyClosestToCurrentPosition(): MovieRemoteKey? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return this.anchorPosition?.let { position ->
            this.closestItemToPosition(position)?.remoteId?.let { id ->
                repository.getRemoteKeyByMovieId(id)
            }
        }
    }
}
