package com.top1shvetsvadim1.domain

import com.top1shvetsvadim1.coreutils.UseCase
import javax.inject.Inject

class AddItemToFavoriteUseCase @Inject constructor(
    private val repository: ProductRepository
) : UseCase<Int, Unit> {
    override suspend fun invoke(params: Int) {
        repository.addProductItemToFavorite(params)
    }
}