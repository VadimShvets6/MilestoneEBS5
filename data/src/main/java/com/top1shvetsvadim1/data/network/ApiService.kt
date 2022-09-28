package com.top1shvetsvadim1.data.network

import com.top1shvetsvadim1.domain.models.LoginResponse
import com.top1shvetsvadim1.domain.models.ProductItem
import com.top1shvetsvadim1.domain.models.ProductList
import retrofit2.http.*

interface ApiService {

    @GET("products")
    suspend fun getProductList(
        @Query("ordering") ordering: String = "",
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = PARAM_PAGE_SIZE
    ): ProductList

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: Int
    ): ProductItem

    @POST("users/login")
    suspend fun postLogin(
        @Body params: LoginParam
    ): LoginResponse

    @POST("users/register")
    suspend fun postRegister(
        @Body params: RegisterPara
    ): LoginResponse

    companion object {
        private const val PARAM_PAGE_SIZE = 10
    }
}

data class LoginParam(
    val email: String,
    val password: String
)

data class RegisterPara(
    val full_name: String,
    val email: String,
    val password: String
)