package com.an.trailers_compose.module

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.an.trailers_compose.AppConstants.PAGE_SIZE
import com.an.trailers_compose.AppConstants.PREFETCH_DISTANCE
import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.data.repository.MovieRepository
import com.an.trailers_compose.data.source.MovieRemoteMediator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MoviePagerModule {
    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    fun provideMoviePager(
        repository: MovieRepository
    ): Pager<Int, MovieEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                initialLoadSize = PAGE_SIZE, // How many items you want to load initially
            ),
            pagingSourceFactory = {
                // The pagingSourceFactory lambda should always return a brand new PagingSource
                // when invoked as PagingSource instances are not reusable.
                repository.getMoviesPagingSource()
            },
            remoteMediator = MovieRemoteMediator(repository = repository)
        )
    }
}