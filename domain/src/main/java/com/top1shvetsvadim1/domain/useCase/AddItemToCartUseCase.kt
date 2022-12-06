package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.UseCase
import com.top1shvetsvadim1.domain.repository.CartRepository
import com.top1shvetsvadim1.domain.repository.ProductRepository
import javax.inject.Inject

class AddItemToCartUseCase @Inject constructor(
    private val repository: CartRepository
) : UseCase<Int, Unit> {
    override suspend fun invoke(params: Int) {
        repository.addProductItemToCart(params)
    }
}