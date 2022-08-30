package com.top1shvetsvadim1.domain

import com.top1shvetsvadim1.coreutils.UseCase
import javax.inject.Inject

class AddItemToFavoriteUseCase @Inject constructor(
    private val repository: ProductRepository
) : UseCase<Int, Unit> {
    //TODO: do not use primitive as a param. Try with inline classes.

    /*@JvmInline
    value class FavouriteID(val id: Int) */

    override suspend fun invoke(params: Int) {
        repository.addProductItemToFavorite(params)
    }
}