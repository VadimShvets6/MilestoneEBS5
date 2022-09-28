package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.UseCase
import com.top1shvetsvadim1.domain.models.ProductEntity
import com.top1shvetsvadim1.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetItemByIdUseCase @Inject constructor(
    private val repository: ProductRepository
) : UseCase<Int, Flow<ProductEntity>> {
    override suspend fun invoke(params: Int): Flow<ProductEntity> {
        return repository.getItemById(params)
    }
}


@JvmInline
value class DetailsResponse(
    val flow: Flow<List<BaseUIModel>>
)

@JvmInline
value class BaseUIResponse(
    val list: Flow<List<BaseUIModel>>
)

