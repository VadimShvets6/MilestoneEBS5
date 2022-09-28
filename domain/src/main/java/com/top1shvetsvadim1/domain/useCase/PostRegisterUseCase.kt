package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.UseCase
import com.top1shvetsvadim1.domain.models.LoginResponse
import com.top1shvetsvadim1.domain.repository.ProductRepository
import javax.inject.Inject

class PostRegisterUseCase @Inject constructor(
    private val repository: ProductRepository
) : UseCase<RegisterData, LoginResponse> {
    override suspend fun invoke(params: RegisterData): LoginResponse {
        return repository.postRegister(params.fullName, params.email, params.password)
    }
}

data class RegisterData(
    val fullName: String,
    val email: String,
    val password: String
)