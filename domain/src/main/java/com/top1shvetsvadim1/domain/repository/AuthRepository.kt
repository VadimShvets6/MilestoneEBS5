package com.top1shvetsvadim1.domain.repository

import com.top1shvetsvadim1.domain.models.LoginResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun postLogin(email: String, password: String): Flow<LoginResponse>
    suspend fun postRegister(fullName: String, email: String, password: String): LoginResponse
}