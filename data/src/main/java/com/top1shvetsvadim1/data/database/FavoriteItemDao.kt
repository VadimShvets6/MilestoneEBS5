package com.top1shvetsvadim1.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.top1shvetsvadim1.domain.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteItemDao {

    @Query("SELECT * FROM items")
    fun getFavoriteList(): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteItem(productItem: ProductEntity)

    @Query("DELETE FROM items WHERE id=:id")
    suspend fun deleteProductItemFromFavorite(id: Int)
}