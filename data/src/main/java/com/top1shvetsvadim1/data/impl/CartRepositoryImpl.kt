package com.top1shvetsvadim1.data.impl

import com.top1shvetsvadim1.data.database.FavoriteItemDao
import com.top1shvetsvadim1.data.network.ApiService
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.domain.models.ProductsInCarts
import com.top1shvetsvadim1.domain.repository.CartRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val favoriteItemDao: FavoriteItemDao
) : CartRepository {
    override suspend fun getCartList(filters: Filters): Flow<List<ProductsInCarts>> {
        return when (filters) {
            Filters.PRICE -> favoriteItemDao.getCartListSortByPrice()
            Filters.SIZE -> favoriteItemDao.getCartList()
            Filters.COLOR -> favoriteItemDao.getCartList()
            Filters.RESET -> favoriteItemDao.getCartList()
        }
    }

    override suspend fun removeProductItemFromCart(id: Int) {
        favoriteItemDao.deleteProductItemFromCart(id)
    }

    override suspend fun getCartCount(): Flow<Int> {
        return favoriteItemDao.getCartCount()
    }

    override suspend fun addProductItemToCart(id: Int) {
        val productItem = apiService.getProductById(id)
        favoriteItemDao.addItemToCart(
            ProductsInCarts(
                id = productItem.id,
                name = productItem.name,
                details = productItem.details,
                size = productItem.size,
                colour = productItem.colour,
                price = productItem.price,
                mainImage = productItem.mainImage,
                inCart = !productItem.inCart,
                count = productItem.count,
                isFavorite = productItem.isFavorite
            )
        )
    }
}