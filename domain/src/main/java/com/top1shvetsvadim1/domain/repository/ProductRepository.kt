package com.top1shvetsvadim1.domain.repository

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.domain.models.LoginResponse
import com.top1shvetsvadim1.domain.models.ProductEntity
import com.top1shvetsvadim1.domain.models.ProductFavorite
import com.top1shvetsvadim1.domain.models.ProductsInCarts
import kotlinx.coroutines.flow.Flow

//TODO: consider to make fav, cart and main repos
interface ProductRepository {
    suspend fun getProductList(filter: String = ""): Flow<List<ProductEntity>>
    suspend fun addProductItemToFavorite(id: Int)
    suspend fun removeProductItemFromFavorite(id: Int)
    suspend fun getFavoriteList(): Flow<List<ProductFavorite>>
    suspend fun getCartList(): Flow<List<ProductsInCarts>>
    suspend fun getItemById(id: Int): Flow<ProductEntity>
    suspend fun changeStateItem(id: Int)
    suspend fun checkIfElementIsFavorite(id: Int): Flow<Boolean>
    suspend fun addProductItemToCart(id: Int)
    suspend fun getBaseUIItemList(): Flow<List<BaseUIModel>>
    suspend fun removeProductItemFromCart(id: Int)

    //TODO: AuthRepository
    suspend fun postLogin(email: String, password: String): Flow<LoginResponse>
    suspend fun postRegister(fullName: String, email: String, password: String): LoginResponse
}