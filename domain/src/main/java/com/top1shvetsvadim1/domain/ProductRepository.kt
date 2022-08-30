package com.top1shvetsvadim1.domain

import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductList(): Flow<List<ProductEntity>>
    suspend fun addProductItemToFavorite(id: Int)
    suspend fun removeProductItemFromFavorite(id: Int)
    suspend fun getFavoriteList(): Flow<List<ProductEntity>>
    suspend fun getItemById(id: Int): Flow<ProductEntity>
    suspend fun changeStateItem(id: Int)
    suspend fun checkIfElementIsFavorite(id: Int) : Flow<Boolean>
}