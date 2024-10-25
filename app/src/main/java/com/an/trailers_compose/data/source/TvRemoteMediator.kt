package com.an.trailers_compose.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.an.trailers_compose.data.local.CategoryStore
import com.an.trailers_compose.data.local.entity.TvEntity
import com.an.trailers_compose.data.local.entity.TvRemoteKey
import com.an.trailers_compose.data.remote.model.Category
import com.an.trailers_compose.data.remote.model.equalTo
import com.an.trailers_compose.data.repository.TvRepository
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class TvRemoteMediator @Inject constructor(
    private val category: Category,
    private val categoryStore: CategoryStore,
    private val repository: TvRepository
) : RemoteMediator<Int, TvEntity>() {
    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        val currentCategory = categoryStore.tvCategory.firstOrNull()

        return if (
            System.currentTimeMillis() - (repository.getRemoteKeyCreatedTime() ?: 0) < cacheTimeout
            && category.equalTo(currentCategory)
            ) {
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
        state: PagingState<Int, TvEntity>
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
            val apiResponse = repository.fetchTvList(category, nextPage = page.toLong())

            val tvList = apiResponse.results
            val endOfPaginationReached = tvList.isEmpty()

            if (loadType == LoadType.REFRESH) {
                repository.clearRemoteKeys()
                repository.deleteAll()
            }

            val prevKey = if (page > 1) page - 1 else null
            val nextKey = if (endOfPaginationReached) null else page + 1
            val remoteKeys = tvList.map {
                TvRemoteKey(
                    tvId = it.remoteId,
                    prevKey = prevKey,
                    currentPage = page,
                    nextKey = nextKey
                )
            }

            repository.addRemoteKeys(remoteKeys)
            repository.addTvList(tvList)
            categoryStore.storeTvCategory(category)

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
     * When we need to load data at the end of the currently loaded data set, the load parameter is
     * LoadType.APPEND
     */
    private suspend fun PagingState<Int, TvEntity>.getRemoteKeyForLastItem(): TvRemoteKey? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return this.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { tv ->
            repository.getRemoteKeyByTvId(tv.remoteId)
        }
    }

    /** LoadType.Prepend
     * When we need to load data at the beginning of the currently loaded data set, the load
     * parameter is LoadType.PREPEND
     */
    private suspend fun PagingState<Int, TvEntity>.getRemoteKeyForFirstItem(): TvRemoteKey? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return this.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { tv ->
            repository.getRemoteKeyByTvId(tv.remoteId)
        }
    }

    /** LoadType.REFRESH
     * Gets called when it's the first time we're loading data, or when PagingDataAdapter.refresh()
     * is called; so now the point of reference for loading our data is the state.anchorPosition.
     * If this is the first load, then the anchorPosition is null. When PagingDataAdapter.refresh()
     * is called, the anchorPosition is the first visible position in the displayed list, so we
     * will need to load the page that contains that specific item.
     */
    private suspend fun PagingState<Int, TvEntity>.getRemoteKeyClosestToCurrentPosition(): TvRemoteKey? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return this.anchorPosition?.let { position ->
            this.closestItemToPosition(position)?.remoteId?.let { id ->
                repository.getRemoteKeyByTvId(id)
            }
        }
    }
}
