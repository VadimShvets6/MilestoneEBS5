package com.top1shvetsvadim1.data

import android.util.Log
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.data.database.FavoriteItemDao
import com.top1shvetsvadim1.data.network.ApiService
import com.top1shvetsvadim1.data.network.LoginParam
import com.top1shvetsvadim1.data.network.RegisterPara
import com.top1shvetsvadim1.domain.models.LoginResponse
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

    override suspend fun getProductList(filter: String): Flow<List<ProductEntity>> {
        Log.d("Test", "Repo: $filter")
        return combine(
            flowOf(apiService.getProductList(page = 1, ordering = filter).results),
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
                    inCart = carts.map { item -> item.id }.contains(it.id),
                    count = it.count
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
                isFavorite = !productItem.isFavorite,
                inCart = productItem.inCart,
                count = productItem.count
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
                inCart = !productItem.inCart,
                count = productItem.count,
                isFavorite = productItem.isFavorite
            )
        )
    }

    override suspend fun removeProductItemFromCart(id: Int) {
        favoriteItemDao.deleteProductItemFromCart(id)
    }

    override suspend fun getItemById(id: Int): Flow<ProductEntity> {
        return combine(
            flowOf(apiService.getProductById(id)),
            getCartList(),
            getFavoriteList()
        ) { product, favorites, carts ->
            ProductEntity(
                id = product.id,
                name = product.name,
                details = product.details,
                size = product.size,
                colour = product.colour,
                price = product.price,
                mainImage = product.mainImage,
                isFavorite = favorites.map { it.id }.contains(product.id),
                inCart = carts.map { it.id }.contains(product.id),
                count = product.count
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

    override suspend fun getBaseUIItemList(): Flow<List<BaseUIModel>> {
        return flowOf(mutableListOf<BaseUIModel>())
    }

    override suspend fun getFavoriteList(): Flow<List<ProductFavorite>> {
        return combine(
            favoriteItemDao.getFavoriteList(),
            getCartList()
        ) { favorite, carts ->
            favorite.map {
                ProductFavorite(
                    id = it.id,
                    name = it.name,
                    details = it.details,
                    size = it.size,
                    colour = it.colour,
                    price = it.price,
                    count = it.count,
                    mainImage = it.mainImage,
                    isFavorite = it.isFavorite,
                    inCart = carts.map { cart -> cart.id }.contains(it.id)
                )
            }
        }
    }

    override suspend fun getCartList(): Flow<List<ProductsInCarts>> {
        return favoriteItemDao.getCartList()
    }

    override suspend fun postLogin(email: String, password: String): Flow<LoginResponse> {
        return flowOf(apiService.postLogin(LoginParam(email, password))).map {
            LoginResponse(
                email = it.email,
                password = it.password,
            )
        }
    }

    override suspend fun postRegister(fullName: String, email: String, password: String): LoginResponse {
        return apiService.postRegister(RegisterPara(fullName, email, password))
    }
}