package com.an.trailers_compose.data.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.an.trailers_compose.ContentTest
import com.an.trailers_compose.data.local.TvDatabase
import com.an.trailers_compose.data.local.dao.TvDao
import com.an.trailers_compose.data.local.dao.TvRemoteKeyDao
import com.an.trailers_compose.data.local.entity.TvRemoteKey
import com.an.trailers_compose.data.remote.api.TvApiService
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

@RunWith(AndroidJUnit4::class)
class TvRepositoryTest: TestCase() {
    private lateinit var db: TvDatabase
    private val apiService: TvApiService = mock()

    private lateinit var repository: TvRepository

    private val expectedTvList = ContentTest.getTvList()

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
            TvDatabase::class.java
        ).build()
        val dao: TvDao = db.tvDao
        val remoteKeyDao: TvRemoteKeyDao = db.remoteKeyDao
        repository = TvRepository(dao, remoteKeyDao, apiService)
    }

    // Override function closeDb() and annotate it with @After.
    // @After annotation means our closing function will be called every-time after
    // executing test cases. This function will be called at last when this test class is called.
    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun getTvRecordsWhenNoneExistsInDb() = runBlocking {
        // When there are no records in the db, when we fetch tv list by categories,
        // the list is empty and does not cause errors
        val tvSeriesList = repository.getTvList()
        assertEquals(0, tvSeriesList.size)
    }

    @Test
    fun insertTvListIfNoRecordsExistsInDb() = runBlocking {
        repository.addTvList(expectedTvList)

        val tvList = repository.getTvList()
        assertEquals(expectedTvList.size, tvList.size)

        for(index in 0..19) {
            assertEquals(expectedTvList[index].title, tvList[index].title)
            assertEquals(expectedTvList[index].remoteId, tvList[index].remoteId)
        }
    }

    @Test
    fun deleteAllTvListAndVerifyRecordsAreDeleted() = runBlocking {
        repository.addTvList(expectedTvList)

        // delete tv list from db
        repository.deleteAll()

        // verify all records are deleted
        val dbList = repository.getTvList()
        assertEquals(0, dbList.size)
    }

    @Test
    fun insertRemoteKeysAndVerifyInsertIsSuccessful() = runBlocking {
        val tvList = ContentTest.getTvList()

        // insert
        val page = 1
        val prevKey = null
        val nextKey = page + 1
        val remoteKeys = tvList.map {
            TvRemoteKey(
                tvId = it.remoteId,
                prevKey = prevKey,
                currentPage = page,
                nextKey = nextKey
            )
        }

        repository.addRemoteKeys(remoteKeys)

        // Verify records exist in db
        for(index in 0..19) {
            val insertedKeys = repository.getRemoteKeyByTvId(tvList[index].remoteId)
            assertEquals(remoteKeys[index].tvId, insertedKeys?.tvId)
            assertEquals(remoteKeys[index].prevKey, insertedKeys?.prevKey)
            assertEquals(remoteKeys[index].nextKey, insertedKeys?.nextKey)
            assertEquals(remoteKeys[index].currentPage, insertedKeys?.currentPage)
        }
    }

    @Test
    fun updateRemoteKeysAndVerifyUpdateIsSuccessful() = runBlocking {
        val tvList = ContentTest.getTvList()

        // insert
        val page = 1
        val prevKey = null
        val nextKey = page + 1
        val remoteKeys = tvList.map {
            TvRemoteKey(
                tvId = it.remoteId,
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
        val updatedRemoteKeys = tvList.map {
            TvRemoteKey(
                tvId = it.remoteId,
                prevKey = updatePrevKey,
                currentPage = page2,
                nextKey = updatedNextKey
            )
        }
        repository.addRemoteKeys(updatedRemoteKeys)

        // Verify records are updated in db
        for(index in 0..19) {
            val updatedKeys = repository.getRemoteKeyByTvId(tvList[index].remoteId)
            assertEquals(updatedRemoteKeys[index].tvId, updatedKeys?.tvId)
            assertEquals(updatedRemoteKeys[index].prevKey, updatedKeys?.prevKey)
            assertEquals(updatedRemoteKeys[index].nextKey, updatedKeys?.nextKey)
            assertEquals(updatedRemoteKeys[index].currentPage, updatedKeys?.currentPage)
        }
    }

    @Test
    fun deleteRemoteKeyRecordsFromDbAndVerifyDeleteIsSuccessful() = runBlocking {
        val tvList = ContentTest.getTvList()

        // insert
        val page = 1
        val prevKey = null
        val nextKey = page + 1
        val remoteKeys = tvList.map {
            TvRemoteKey(
                tvId = it.remoteId,
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
            val keys = repository.getRemoteKeyByTvId(tvList[index].remoteId)
            assertNull(keys)
        }
    }
}