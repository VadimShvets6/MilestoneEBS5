package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.UseCase
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ChangeStateItemUseCase @Inject constructor(
    private val repository: FavoriteRepository
) : UseCase<Int, Unit> {
    override suspend fun invoke(params: Int) {
        when (repository.getFavoriteList().first().map { it.id }.contains(params)) {
            true -> {
                repository.removeProductItemFromFavorite(params)
            }
            false -> {
                repository.addProductItemToFavorite(params)
            }
        }
    }
}