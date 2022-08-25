package com.top1shvetsvadim1.domain

import com.top1shvetsvadim1.coreutils.UseCase
import javax.inject.Inject

class RemoveProductFromFavoriteUseCase @Inject constructor(
    private val repository: ProductRepository
) : UseCase<Int, Unit> {
    override suspend fun invoke(params: Int) {
        repository.removeProductItemFromFavorite(params)
    }
}