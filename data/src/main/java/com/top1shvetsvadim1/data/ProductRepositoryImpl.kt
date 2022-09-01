package com.top1shvetsvadim1.data

import com.top1shvetsvadim1.data.database.FavoriteItemDao
import com.top1shvetsvadim1.data.network.ApiService
import com.top1shvetsvadim1.domain.models.ProductEntity
import com.top1shvetsvadim1.domain.models.ProductFavorite
import com.top1shvetsvadim1.domain.models.ProductsInCarts
import com.top1shvetsvadim1.domain.repository.ProductRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val favoriteItemDao: FavoriteItemDao
) : ProductRepository {

    override suspend fun getProductList(): Flow<List<ProductEntity>> {
        return combine(
            flowOf(apiService.getProductList(page = 1).results),
            getFavoriteList(),
            getCartList()
        ) { products, favorites, carts ->
            products.map {
                ProductEntity(
                    id = it.id,
                    name = it.name,
                    details = it.details,
                    size = it.size,
                    colour = it.colour,
                    price = it.price,
                    mainImage = it.mainImage,
                    isFavorite = favorites.map { item -> item.id }.contains(it.id),
                    inCart = carts.map { item -> item.id }.contains(it.id)
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
            ProductFavorite(
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
                inCart = !productItem.inCart
            )
        )
    }

    override suspend fun removeProductItemFromCart(id: Int) {
        favoriteItemDao.deleteProductItemFromCart(id)
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
                isFavorite = favorites.map { it.id }.contains(product.id),
                inCart = false
            )
        }
    }

    override suspend fun changeStateItem(id: Int) {
        when (favoriteItemDao.getFavoriteList().first().map { it.id }.contains(id)) {
            true -> {
                removeProductItemFromFavorite(id)
            }
            false -> {
                addProductItemToFavorite(id)
            }
        }
    }

    override suspend fun checkIfElementIsFavorite(id: Int): Flow<Boolean> {
        return favoriteItemDao.getFavoriteList().map { it.map { it.id }.contains(id) }
    }

    override suspend fun getFavoriteList(): Flow<List<ProductFavorite>> {
        return favoriteItemDao.getFavoriteList()
    }

    override suspend fun getCartList(): Flow<List<ProductsInCarts>> {
        return favoriteItemDao.getCartList()
    }
}