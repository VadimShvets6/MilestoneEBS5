package com.top1shvetsvadim1.domain.repository

import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.domain.models.ProductsInCarts
import com.top1shvetsvadim1.domain.useCase.CartSize
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getCartList(filters: Filters = Filters.RESET): Flow<List<ProductsInCarts>>
    suspend fun removeProductItemFromCart(id: Int)
    suspend fun addProductItemToCart(id: Int)
    suspend fun getCartCount() : Flow<Int>
}