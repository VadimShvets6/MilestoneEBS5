package com.top1shvetsvadim1.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.top1shvetsvadim1.domain.models.ProductFavorite
import com.top1shvetsvadim1.domain.models.ProductsInCarts

@Database(
    entities = [ProductFavorite::class, ProductsInCarts::class],
    version = 8,
    exportSchema = false,
//    autoMigrations = [
//        AutoMigration(from = 5, to = 6)
//    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteItemDao(): FavoriteItemDao
}