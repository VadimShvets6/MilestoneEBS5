package com.top1shvetsvadim1.data

import android.util.Log
import com.top1shvetsvadim1.data.database.FavoriteItemDao
import com.top1shvetsvadim1.data.network.ApiService
import com.top1shvetsvadim1.domain.ProductEntity
import com.top1shvetsvadim1.domain.ProductRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val favoriteItemDao: FavoriteItemDao
) : ProductRepository {

    override suspend fun getProductList(): Flow<List<ProductEntity>> {
        return combine(
            flowOf(apiService.getProductList(page = 1).results),
            getFavoriteList()
        ) { products, favorites ->
            products.map {
                ProductEntity(
                    id = it.id,
                    name = it.name,
                    details = it.details,
                    size = it.size,
                    colour = it.colour,
                    price = it.price,
                    mainImage = it.mainImage,
                    isFavorite = favorites.map { it.id }.contains(it.id)
                )
            }
        }
    }

    override suspend fun removeProductItemFromFavorite(id: Int) {
        favoriteItemDao.deleteProductItemFromFavorite(id)
    }

    override suspend fun addProductItemToFavorite(id: Int) {
        val productItem = apiService.getProductById(id)
        favoriteItemDao.addFavoriteItem(
            ProductEntity(
                id = productItem.id,
                name = productItem.name,
                details = productItem.details,
                size = productItem.size,
                colour = productItem.colour,
                price = productItem.price,
                mainImage = productItem.mainImage,
                isFavorite = !productItem.isFavorite
            )
        )
    }

    override suspend fun getItemById(id: Int): Flow<ProductEntity> {
        return combine(
            flowOf(apiService.getProductById(id)),
            getFavoriteList()
        ) { product, favorites ->
            ProductEntity(
                id = product.id,
                name = product.name,
                details = product.details,
                size = product.size,
                colour = product.colour,
                price = product.price,
                mainImage = product.mainImage,
                isFavorite = favorites.map { it.id }.contains(product.id)
            )
        }
    }

    override suspend fun changeStateItem(id: Int) {
        when (favoriteItemDao.getFavoriteList().first().map { it.id }.contains(id)) {
            true -> {
                Log.d("DetailFrag", "remove")
                removeProductItemFromFavorite(id)
            }
            false -> {
                Log.d("DetailFrag", "add")
                addProductItemToFavorite(id)
            }
        }
    }

    override suspend fun getFavoriteList(): Flow<List<ProductEntity>> {
        return favoriteItemDao.getFavoriteList()
    }
}