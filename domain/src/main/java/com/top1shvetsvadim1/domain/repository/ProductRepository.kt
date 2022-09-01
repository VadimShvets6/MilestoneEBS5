package com.top1shvetsvadim1.domain.repository

import com.top1shvetsvadim1.domain.models.ProductEntity
import com.top1shvetsvadim1.domain.models.ProductFavorite
import com.top1shvetsvadim1.domain.models.ProductsInCarts
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductList(): Flow<List<ProductEntity>>
    suspend fun addProductItemToFavorite(id: Int)
    suspend fun removeProductItemFromFavorite(id: Int)
    suspend fun getFavoriteList(): Flow<List<ProductFavorite>>
    suspend fun getCartList(): Flow<List<ProductsInCarts>>
    suspend fun getItemById(id: Int): Flow<ProductEntity>
    suspend fun changeStateItem(id: Int)
    suspend fun checkIfElementIsFavorite(id: Int): Flow<Boolean>
    suspend fun addProductItemToCart(id: Int)
    suspend fun removeProductItemFromCart(id: Int)

}