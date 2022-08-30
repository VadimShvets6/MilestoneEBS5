package com.top1shvetsvadim1.domain

import com.top1shvetsvadim1.coreutils.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckIfElementIsFavoriteUseCase @Inject constructor(
    private val repository: ProductRepository
) : UseCase<Int, Flow<Boolean>> {
    override suspend fun invoke(params: Int) : Flow<Boolean> {
        return repository.checkIfElementIsFavorite(params)
    }
}