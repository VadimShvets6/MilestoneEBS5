package com.top1shvetsvadim1.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.domain.models.ProductFavorite
import com.top1shvetsvadim1.domain.models.ProductsInCarts
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteItemDao {

    @Query("SELECT * FROM items")
    fun getFavoriteList(): Flow<List<ProductFavorite>>

    @Query("SELECT * FROM items ORDER BY price ASC")
    fun getFavoriteListSortByPrice(): Flow<List<ProductFavorite>>

    @Query("SELECT * FROM items ORDER BY size ASC")
    fun getFavoriteListSortBySize(): Flow<List<ProductFavorite>>

    @Query("SELECT * FROM items ORDER BY colour ASC")
    fun getFavoriteListSortByColour(): Flow<List<ProductFavorite>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavoriteItem(productItem: ProductFavorite)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItemToCart(productItem: ProductsInCarts)

    @Query("SELECT COUNT(*) FROM cart_items")
    fun getCartCount(): Flow<Int>

    @Query("DELETE FROM items WHERE id=:id")
    suspend fun deleteProductItemFromFavorite(id: Int)

    @Query("DELETE FROM cart_items WHERE id=:id")
    suspend fun deleteProductItemFromCart(id: Int)

    @Query("SELECT * FROM cart_items")
    fun getCartList(): Flow<List<ProductsInCarts>>

    @Query("SELECT * FROM cart_items ORDER BY price ASC")
    fun getCartListSortByPrice(): Flow<List<ProductsInCarts>>
}