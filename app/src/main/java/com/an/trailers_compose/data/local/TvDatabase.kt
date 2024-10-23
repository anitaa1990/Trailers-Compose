package com.an.trailers_compose.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.an.trailers_compose.data.local.converter.CreditResponseTypeConverter
import com.an.trailers_compose.data.local.converter.GenreListTypeConverter
import com.an.trailers_compose.data.local.converter.LongListTypeConverter
import com.an.trailers_compose.data.local.converter.TvApiResponseTypeConverter
import com.an.trailers_compose.data.local.converter.VideoResponseTypeConverter
import com.an.trailers_compose.data.local.dao.TvDao
import com.an.trailers_compose.data.local.dao.TvRemoteKeyDao
import com.an.trailers_compose.data.local.entity.TvEntity
import com.an.trailers_compose.data.local.entity.TvRemoteKey

@Database(
    entities = [TvEntity::class, TvRemoteKey::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    GenreListTypeConverter::class,
    LongListTypeConverter::class,
    CreditResponseTypeConverter::class,
    VideoResponseTypeConverter::class,
    TvApiResponseTypeConverter::class
)
abstract class TvDatabase: RoomDatabase() {
    abstract val tvDao: TvDao
    abstract val remoteKeyDao: TvRemoteKeyDao
}