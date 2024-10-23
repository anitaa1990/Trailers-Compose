package com.an.trailers_compose.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.an.trailers_compose.ContentTest
import com.an.trailers_compose.data.local.CategoryStore
import com.an.trailers_compose.data.local.TvDatabase
import com.an.trailers_compose.data.local.dao.TvDao
import com.an.trailers_compose.data.local.dao.TvRemoteKeyDao
import com.an.trailers_compose.data.local.entity.TvEntity
import com.an.trailers_compose.data.remote.api.TvApiService
import com.an.trailers_compose.data.remote.model.Category
import com.an.trailers_compose.data.remote.model.TvApiResponse
import com.an.trailers_compose.data.remote.model.api
import com.an.trailers_compose.data.repository.TvRepository
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class TvRemoteMediatorTest: TestCase() {
    private lateinit var db: TvDatabase
    private lateinit var categoryStore: CategoryStore
    private val apiService: TvApiService = mock()

    private lateinit var repository: TvRepository
    private val mockTvList = ContentTest.getTvList()

    @Before
    public override fun setUp() {
        // get context -- since this is an instrumental test it requires
        // context from the running application
        // initialize the db and dao variable
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            TvDatabase::class.java
        ).build()
        val dao: TvDao = db.tvDao
        val remoteKeyDao: TvRemoteKeyDao = db.remoteKeyDao
        categoryStore = CategoryStore(ApplicationProvider.getApplicationContext())
        repository = TvRepository(dao, remoteKeyDao, apiService)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runBlocking {
        // Add mock results for the API to return
        val category = Category.NOW_PLAYING
        `when`(apiService.fetchTvList(category.api(), 1L)).thenReturn(
            TvApiResponse(
                page = 1L, totalPages = 1212L, totalResults = 2323L, results = mockTvList
            )
        )

        val remoteMediator = TvRemoteMediator(
            repository = repository,
            category = category,
            categoryStore = categoryStore
        )
        val pagingState = PagingState<Int, TvEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue( result is RemoteMediator.MediatorResult.Success )
        assertFalse( (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached )
    }

    @Test
    fun refreshLoadSuccessAndEndOfPaginationWhenNoMoreData() = runBlocking {
        val category = Category.NOW_PLAYING

        // To test endOfPaginationReached, don't set up the mockApi to return post data here
        `when`(apiService.fetchTvList(category.api(), 1L)).thenReturn(
            TvApiResponse(
                page = 1L, totalPages = 1212L, totalResults = 2323L, results = emptyList()
            )
        )

        val remoteMediator = TvRemoteMediator(
            repository = repository,
            category = category,
            categoryStore = categoryStore
        )
        val pagingState = PagingState<Int, TvEntity>(
            listOf(),
            null,
            PagingConfig(10),
            100
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue ( result is RemoteMediator.MediatorResult.Success )
        assertTrue ( (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached )
    }

    @Test
    fun refreshLoadReturnsErrorResultWhenErrorOccurs() = runBlocking {
        // By default, when apiService is called, the result will be null so an exception will be
        // thrown so we don't need to simulate error exception here

        val remoteMediator = TvRemoteMediator(
            repository = repository,
            category = Category.NOW_PLAYING,
            categoryStore = categoryStore
        )
        val pagingState = PagingState<Int, TvEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue ( result is RemoteMediator.MediatorResult.Error )
    }
}