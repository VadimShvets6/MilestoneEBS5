package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.UseCaseNoParams
import com.top1shvetsvadim1.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartListSizeUseCase @Inject constructor(
    private val repository: CartRepository
) : UseCaseNoParams<Flow<Int>> {
    override suspend fun invoke(): Flow<Int> {
        return repository.getCartCount()
    }
}