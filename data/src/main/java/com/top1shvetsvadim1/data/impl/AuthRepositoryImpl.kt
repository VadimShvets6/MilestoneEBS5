package com.top1shvetsvadim1.data.impl

import com.top1shvetsvadim1.data.network.ApiService
import com.top1shvetsvadim1.data.network.LoginParam
import com.top1shvetsvadim1.data.network.RegisterPara
import com.top1shvetsvadim1.domain.models.LoginResponse
import com.top1shvetsvadim1.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {

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