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
import com.an.trailers_compose.data.local.MovieDatabase
import com.an.trailers_compose.data.local.dao.MovieDao
import com.an.trailers_compose.data.local.dao.MovieRemoteKeyDao
import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.data.remote.api.MovieApiService
import com.an.trailers_compose.data.remote.model.Category
import com.an.trailers_compose.data.remote.model.MovieApiResponse
import com.an.trailers_compose.data.remote.model.api
import com.an.trailers_compose.data.repository.MovieRepository
import junit.framework.TestCase
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class MovieRemoteMediatorTest: TestCase() {
    private lateinit var db: MovieDatabase
    private lateinit var categoryStore: CategoryStore
    private val apiService: MovieApiService = mock()

    private lateinit var repository: MovieRepository
    private val mockMoviesList = ContentTest.getPopularMovies()

    @Before
    public override fun setUp() {
        // get context -- since this is an instrumental test it requires
        // context from the running application
        // initialize the db and dao variable
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).build()
        val dao: MovieDao = db.movieDao
        val remoteKeyDao: MovieRemoteKeyDao = db.remoteKeyDao
        categoryStore = CategoryStore(ApplicationProvider.getApplicationContext())
        repository = MovieRepository(dao, remoteKeyDao, apiService)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runBlocking {
        // Add mock results for the API to return
        val category = Category.NOW_PLAYING
        `when`(apiService.fetchMovies(category.api(), 1L)).thenReturn(
            MovieApiResponse(
                page = 1L, totalPages = 1212L, totalResults = 2323L, results = mockMoviesList
            )
        )
//        `when`(categoryStore.movieCategory).thenReturn(flow { Category.NOW_PLAYING })

        val remoteMediator = MovieRemoteMediator(
            repository = repository,
            category = category,
            categoryStore = categoryStore
        )
        val pagingState = PagingState<Int, MovieEntity>(
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
        `when`(apiService.fetchMovies(category.api(), 1L)).thenReturn(
            MovieApiResponse(
                page = 1L, totalPages = 1212L, totalResults = 2323L, results = emptyList()
            )
        )

        val remoteMediator = MovieRemoteMediator(
            repository = repository,
            category = category,
            categoryStore = categoryStore
        )
        val pagingState = PagingState<Int, MovieEntity>(
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

        val remoteMediator = MovieRemoteMediator(
            repository = repository,
            category = Category.NOW_PLAYING,
            categoryStore = categoryStore
        )
        val pagingState = PagingState<Int, MovieEntity>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )
        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue ( result is RemoteMediator.MediatorResult.Error )
    }
}