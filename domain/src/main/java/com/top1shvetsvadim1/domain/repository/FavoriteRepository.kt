package com.top1shvetsvadim1.domain.repository

import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.domain.models.ProductFavorite
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun addProductItemToFavorite(id: Int)
    suspend fun removeProductItemFromFavorite(id: Int)
    suspend fun getFavoriteList(filters: Filters = Filters.RESET): Flow<List<ProductFavorite>>
}