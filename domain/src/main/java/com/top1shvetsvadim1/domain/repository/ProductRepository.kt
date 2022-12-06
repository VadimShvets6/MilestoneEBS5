package com.top1shvetsvadim1.domain.repository

import androidx.paging.PagingData
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.domain.models.*
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductList(filter: Filters = Filters.RESET): Flow<List<ProductItem>>
    suspend fun getItemById(id: Int): Flow<ProductItem>
    suspend fun checkIfElementIsFavorite(id: Int): Flow<Boolean>
    suspend fun getBaseUIItemList(): Flow<List<BaseUIModel>>
}