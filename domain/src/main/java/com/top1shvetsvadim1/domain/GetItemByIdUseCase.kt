package com.top1shvetsvadim1.domain

import com.top1shvetsvadim1.coreutils.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemByIdUseCase @Inject constructor(
    private val repository: ProductRepository
) : UseCase<Int, Flow<ProductEntity>>{
    override suspend fun invoke(params: Int): Flow<ProductEntity> {
        return repository.getItemById(params)
    }
}