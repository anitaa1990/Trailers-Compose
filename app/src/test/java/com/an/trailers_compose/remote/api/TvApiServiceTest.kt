package com.an.trailers_compose.remote.api

import com.an.trailers_compose.data.remote.api.TvApiService
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class TvApiServiceTest {
    private val mockWebServer = MockWebServer()
    private lateinit var apiService: TvApiService

    @Before
    fun setup() {
        mockWebServer.start()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(TvApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `read sample success json file`() {
        val reader = MockResponseFileReader("tv_list_api_response.json")
        assertNotNull(reader.content)
    }

    @Test
    fun `fetch tv list and check response Code 200 returned`() = runTest {
        // Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("tv_list_api_response.json").content)

        mockWebServer.enqueue(response)

        // Act
        val actualResponse = apiService.fetchTvList(
            "top_rated", 1
        )

        // Assert
        assertEquals(1, actualResponse.page)
        assertEquals(9185, actualResponse.totalPages)
        assertEquals(183684, actualResponse.totalResults)
        assertEquals(20, actualResponse.results.size)
    }

    @Test
    fun `fetch tv detail and check response Code 200 returned`() = runTest {
        // Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("tv_detail_api_response_success.json").content)

        mockWebServer.enqueue(response)

        // Act
        val actualResponse = apiService.fetchTvDetail(1L)

        // Assert
        assertNotNull(actualResponse.videoApiResponse)
        assertNotNull(actualResponse.credits)
        assertNotNull(actualResponse.genres)
        assertNotNull(actualResponse.similarTvApiResponse)
        assertEquals(1396L, actualResponse.remoteId)
        assertEquals(1, actualResponse.videoApiResponse?.videos?.size)
        assertEquals(8, actualResponse.credits?.cast?.size)
        assertEquals(25, actualResponse.credits?.crew?.size)
        assertEquals(20, actualResponse.similarTvApiResponse?.results?.size)
    }
}