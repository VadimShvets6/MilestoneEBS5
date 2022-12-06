package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.UseCase
import com.top1shvetsvadim1.domain.models.LoginResponse
import com.top1shvetsvadim1.domain.repository.AuthRepository
import com.top1shvetsvadim1.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostLoginUseCase @Inject constructor(
    private val repository: AuthRepository
) : UseCase<LoginData, Flow<LoginResponse>> {

    override suspend fun invoke(params: LoginData): Flow<LoginResponse> {
        return repository.postLogin(params.email, params.password)
    }
}

data class LoginData(
    val email: String,
    val password: String
)