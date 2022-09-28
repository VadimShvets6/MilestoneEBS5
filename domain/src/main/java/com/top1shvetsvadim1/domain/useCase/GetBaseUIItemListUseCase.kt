package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.UseCaseNoParams
import com.top1shvetsvadim1.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

//TODO: strange Use Case. Map items into UI models directly into viewModels or mappers
class GetBaseUIItemListUseCase @Inject constructor(
    private val repository: ProductRepository
) : UseCaseNoParams<Flow<List<BaseUIModel>>> {
    override suspend fun invoke(): Flow<List<BaseUIModel>> {
        return repository.getBaseUIItemList()
    }
}