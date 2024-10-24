package com.an.trailers_compose.remote.api

import com.an.trailers_compose.data.remote.api.MovieApiService
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

class MovieApiServiceTest {
    private val mockWebServer = MockWebServer()
    private lateinit var apiService: MovieApiService

    @Before
    fun setup() {
        mockWebServer.start()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build()
            .create(MovieApiService::class.java)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `read sample success json file`() {
        val reader = MockResponseFileReader("movie_list_api_response.json")
        assertNotNull(reader.content)
    }

    @Test
    fun `fetch movie list and check response Code 200 returned`() = runTest {
        // Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("movie_list_api_response.json").content)

        mockWebServer.enqueue(response)

        // Act
        val actualResponse = apiService.fetchMovies(
            "top_rated", 1
        )

        // Assert
        assertEquals(1, actualResponse.page)
        assertEquals(46401, actualResponse.totalPages)
        assertEquals(928019, actualResponse.totalResults)
        assertEquals(20, actualResponse.results.size)
    }

    @Test
    fun `fetch movie detail and check response Code 200 returned`() = runTest {
        // Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("movie_detail_api_response_success.json").content)

        mockWebServer.enqueue(response)

        // Act
        val actualResponse = apiService.fetchMovieDetail(1L)

        // Assert
        assertNotNull(actualResponse.videoApiResponse)
        assertNotNull(actualResponse.credits)
        assertNotNull(actualResponse.genres)
        assertNotNull(actualResponse.similarMoviesApiResponse)
        assertEquals(278L, actualResponse.remoteId)
        assertEquals(18, actualResponse.videoApiResponse?.videos?.size)
        assertEquals(60, actualResponse.credits?.cast?.size)
        assertEquals(147, actualResponse.credits?.crew?.size)
        assertEquals(20, actualResponse.similarMoviesApiResponse?.results?.size)
    }

    @Test
    fun `search movies and check response Code 200 returned`() = runTest {
        // Assign
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(MockResponseFileReader("movie_list_api_response.json").content)

        mockWebServer.enqueue(response)

        // Act
        val actualResponse = apiService.searchMovies(
            "batman", 1
        )

        // Assert
        assertEquals(1, actualResponse.page)
        assertEquals(46401, actualResponse.totalPages)
        assertEquals(928019, actualResponse.totalResults)
        assertEquals(20, actualResponse.results.size)
    }
}