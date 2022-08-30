package com.top1shvetsvadim1.data.network

import com.top1shvetsvadim1.domain.ProductItemDTO
import com.top1shvetsvadim1.domain.ProductListDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("products")
    suspend fun getProductList(
        @Query(QUERY_PARAM_PAGE) page: Int,
        @Query(QUERY_PARAM_PAGE_SIZE) pageSize: Int = PARAM_PAGE_SIZE
    ): ProductListDTO

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): ProductItemDTO

    //TODO: do not use const strings in annotations, that causes harder debugging and code understanding.
    companion object {
        private const val QUERY_PARAM_PAGE = "page"
        private const val QUERY_PARAM_PAGE_SIZE = "page_size"
        private const val PARAM_PAGE_SIZE = 10
    }
}