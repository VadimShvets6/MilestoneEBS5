package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.UseCase
import com.top1shvetsvadim1.domain.repository.FavoriteRepository
import com.top1shvetsvadim1.domain.repository.ProductRepository
import javax.inject.Inject

class AddItemToFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository
) : UseCase<Int, Unit> {

    override suspend fun invoke(params: Int) {
        repository.addProductItemToFavorite(params)
    }
}

