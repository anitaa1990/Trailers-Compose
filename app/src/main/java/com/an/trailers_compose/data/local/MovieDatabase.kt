package com.an.trailers_compose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.an.trailers_compose.data.local.converter.CreditResponseTypeConverter
import com.an.trailers_compose.data.local.converter.GenreListTypeConverter
import com.an.trailers_compose.data.local.converter.LongListTypeConverter
import com.an.trailers_compose.data.local.converter.MovieApiResponseTypeConverter
import com.an.trailers_compose.data.local.converter.VideoResponseTypeConverter
import com.an.trailers_compose.data.local.dao.MovieDao
import com.an.trailers_compose.data.local.dao.MovieRemoteKeyDao
import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.data.local.entity.MovieRemoteKey

@Database(
    entities = [MovieEntity::class, MovieRemoteKey::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    GenreListTypeConverter::class,
    LongListTypeConverter::class,
    CreditResponseTypeConverter::class,
    VideoResponseTypeConverter::class,
    MovieApiResponseTypeConverter::class
)
abstract class MovieDatabase: RoomDatabase() {
    abstract val movieDao: MovieDao
    abstract val remoteKeyDao: MovieRemoteKeyDao
}