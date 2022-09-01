package com.top1shvetsvadim1.data.network

import com.top1shvetsvadim1.domain.models.ProductItem
import com.top1shvetsvadim1.domain.models.ProductList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    suspend fun getProductList(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = PARAM_PAGE_SIZE
    ): ProductList

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): ProductItem

    companion object {
        private const val PARAM_PAGE_SIZE = 10
    }
}