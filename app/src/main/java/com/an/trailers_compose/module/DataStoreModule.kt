package com.an.trailers_compose.module

import android.content.Context
import com.an.trailers_compose.data.local.CategoryStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideCategoryStore(
        @ApplicationContext context: Context
    ): CategoryStore = CategoryStore(context)
}