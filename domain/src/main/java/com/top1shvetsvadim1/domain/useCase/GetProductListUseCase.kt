package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.UseCase
import com.top1shvetsvadim1.domain.models.ProductResponse
import com.top1shvetsvadim1.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(
    private val repository: ProductRepository
) : UseCase<String, ProductResponse> {

    override suspend operator fun invoke(params: String): ProductResponse {
        return ProductResponse(repository.getProductList(params))
    }
}

@JvmInline
value class ProductUIResponse(
    val flow: Flow<List<BaseUIModel>>
)

