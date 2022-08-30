package com.top1shvetsvadim1.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.top1shvetsvadim1.domain.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 4, //TODO: version 4, however there are no migrations
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteItemDao(): FavoriteItemDao
}