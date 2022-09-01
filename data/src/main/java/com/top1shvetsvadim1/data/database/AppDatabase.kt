package com.top1shvetsvadim1.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.top1shvetsvadim1.domain.models.ProductEntity
import com.top1shvetsvadim1.domain.models.ProductFavorite
import com.top1shvetsvadim1.domain.models.ProductsInCarts

@Database(
    entities = [ProductFavorite::class, ProductsInCarts::class],
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 4, to = 5)
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteItemDao(): FavoriteItemDao
}