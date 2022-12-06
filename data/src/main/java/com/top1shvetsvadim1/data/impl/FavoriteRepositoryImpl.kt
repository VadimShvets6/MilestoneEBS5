package com.top1shvetsvadim1.data.impl

import android.util.Log
import com.top1shvetsvadim1.data.database.FavoriteItemDao
import com.top1shvetsvadim1.data.network.ApiService
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.domain.models.ProductFavorite
import com.top1shvetsvadim1.domain.models.filtersToString
import com.top1shvetsvadim1.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val favoriteItemDao: FavoriteItemDao
) : FavoriteRepository {

    override suspend fun removeProductItemFromFavorite(id: Int) {
        favoriteItemDao.deleteProductItemFromFavorite(id)
    }

    override suspend fun addProductItemToFavorite(id: Int) {
        val productItem = apiService.getProductById(id)
        favoriteItemDao.addFavoriteItem(
            ProductFavorite(
                id = productItem.id,
                name = productItem.name,
                details = productItem.details,
                size = productItem.size,
                colour = productItem.colour,
                price = productItem.price,
                mainImage = productItem.mainImage,
                isFavorite = !productItem.isFavorite,
                inCart = productItem.inCart,
                count = productItem.count
            )
        )
    }

    override suspend fun getFavoriteList(filters: Filters): Flow<List<ProductFavorite>> {
        return when(filters){
            Filters.PRICE -> favoriteItemDao.getFavoriteListSortByPrice()
            Filters.SIZE -> favoriteItemDao.getFavoriteListSortBySize()
            Filters.COLOR -> favoriteItemDao.getFavoriteListSortByColour()
            Filters.RESET -> favoriteItemDao.getFavoriteList()
        }
    }
}