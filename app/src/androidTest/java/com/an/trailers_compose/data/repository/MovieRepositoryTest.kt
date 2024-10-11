package com.an.trailers_compose.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.an.trailers_compose.MovieTest
import com.an.trailers_compose.data.local.MovieDatabase
import com.an.trailers_compose.data.local.dao.MovieDao
import com.an.trailers_compose.data.local.dao.MovieRemoteKeyDao
import com.an.trailers_compose.data.local.entity.MovieRemoteKey
import com.an.trailers_compose.data.remote.api.MovieApiService
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class MovieRepositoryTest: TestCase() {
    private lateinit var db: MovieDatabase
    private val apiService: MovieApiService = mock()

    private lateinit var repository: MovieRepository

    private val expectedMovies = MovieTest.getPopularMovies()

    // Override function setUp() and annotate it with @Before.
    // The @Before annotation makes sure fun setupDatabase() is executed before each class.
    // The function then creates a database using Room.inMemoryDatabaseBuilder which creates
    // a database in RAM instead of the persistence storage. This means the database will be
    // cleared once the process is killed.
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
        repository = MovieRepository(dao, remoteKeyDao, apiService)
    }

    // Override function closeDb() and annotate it with @After.
    // @After annotation means our closing function will be called every-time after
    // executing test cases. This function will be called at last when this test class is called.
    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun getMovieRecordsWhenNoneExistsInDb() = runBlocking {
        // When there are no records in the db, when we fetch movies by categories,
        // the list is empty and does not cause errors
        val popularMovies = repository.getMovies()
        assertEquals(0, popularMovies.size)

        val upcomingMovies = repository.getMovies()
        assertEquals(0, upcomingMovies.size)

        val topRatedMovies = repository.getMovies()
        assertEquals(0, topRatedMovies.size)

        val nowPlayingMovies = repository.getMovies()
        assertEquals(0, nowPlayingMovies.size)
    }

    @Test
    fun insertMoviesIfNoRecordsExistsInDb() = runBlocking {
        repository.addMovies(expectedMovies)

        val movies = repository.getMovies()
        assertEquals(expectedMovies.size, movies.size)

        for(index in 0..19) {
            assertEquals(expectedMovies[index].title, movies[index].title)
            assertEquals(expectedMovies[index].remoteId, movies[index].remoteId)
        }
    }

    @Test
    fun deleteAllMoviesAndVerifyRecordsAreDeleted() = runBlocking {
        repository.addMovies(expectedMovies)

        // delete movies from db
        repository.deleteAll()

        // verify all records are deleted
        val dbMovies = repository.getMovies()
        assertEquals(0, dbMovies.size)
    }

    @Test
    fun insertRemoteKeysAndVerifyInsertIsSuccessful() = runBlocking {
        val movies = MovieTest.getPopularMovies()

        // insert
        val page = 1
        val prevKey = null
        val nextKey = page + 1
        val remoteKeys = movies.map {
            MovieRemoteKey(
                movieId = it.remoteId,
                prevKey = prevKey,
                currentPage = page,
                nextKey = nextKey
            )
        }

        repository.addRemoteKeys(remoteKeys)

        // Verify records exist in db
        for(index in 0..19) {
            val insertedKeys = repository.getRemoteKeyByMovieId(movies[index].remoteId)
            assertEquals(remoteKeys[index].movieId, insertedKeys?.movieId)
            assertEquals(remoteKeys[index].prevKey, insertedKeys?.prevKey)
            assertEquals(remoteKeys[index].nextKey, insertedKeys?.nextKey)
            assertEquals(remoteKeys[index].currentPage, insertedKeys?.currentPage)
        }
    }

    @Test
    fun updateRemoteKeysAndVerifyUpdateIsSuccessful() = runBlocking {
        val movies = MovieTest.getPopularMovies()

        // insert
        val page = 1
        val prevKey = null
        val nextKey = page + 1
        val remoteKeys = movies.map {
            MovieRemoteKey(
                movieId = it.remoteId,
                prevKey = prevKey,
                currentPage = page,
                nextKey = nextKey
            )
        }

        repository.addRemoteKeys(remoteKeys)

        // update
        val page2 = 2
        val updatePrevKey = page2 - 1
        val updatedNextKey = page2 + 1
        val updatedRemoteKeys = movies.map {
            MovieRemoteKey(
                movieId = it.remoteId,
                prevKey = updatePrevKey,
                currentPage = page2,
                nextKey = updatedNextKey
            )
        }
        repository.addRemoteKeys(updatedRemoteKeys)

        // Verify records are updated in db
        for(index in 0..19) {
            val updatedKeys = repository.getRemoteKeyByMovieId(movies[index].remoteId)
            assertEquals(updatedRemoteKeys[index].movieId, updatedKeys?.movieId)
            assertEquals(updatedRemoteKeys[index].prevKey, updatedKeys?.prevKey)
            assertEquals(updatedRemoteKeys[index].nextKey, updatedKeys?.nextKey)
            assertEquals(updatedRemoteKeys[index].currentPage, updatedKeys?.currentPage)
        }
    }

    @Test
    fun deleteRemoteKeyRecordsFromDbAndVerifyDeleteIsSuccessful() = runBlocking {
        val movies = MovieTest.getPopularMovies()

        // insert
        val page = 1
        val prevKey = null
        val nextKey = page + 1
        val remoteKeys = movies.map {
            MovieRemoteKey(
                movieId = it.remoteId,
                prevKey = prevKey,
                currentPage = page,
                nextKey = nextKey
            )
        }

        repository.addRemoteKeys(remoteKeys)

        // delete keys from db
        repository.clearRemoteKeys()

        // verify all records are deleted
        for(index in 0..19) {
            val keys = repository.getRemoteKeyByMovieId(movies[index].remoteId)
            assertNull(keys)
        }
    }
}