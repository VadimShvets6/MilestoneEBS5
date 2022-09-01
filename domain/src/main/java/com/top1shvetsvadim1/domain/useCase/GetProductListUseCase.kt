package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.UseCaseNoParams
import com.top1shvetsvadim1.domain.models.ProductResponse
import com.top1shvetsvadim1.domain.repository.ProductRepository
import com.top1shvetsvadim1.domain.uimodels.ProductUIModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(
    private val repository: ProductRepository
) : UseCaseNoParams<ProductResponse> {

    override suspend operator fun invoke() = repository.getProductList().map {
        it.map { entity ->
            ProductUIModel(
                name = entity.name,
                sizes = entity.size,
                price = entity.price,
                mainImage = entity.mainImage,
                id = entity.id,
                isFavorite = entity.isFavorite,
                inCart = entity.inCart
            )
        }
    }.let {
        ProductResponse(it)
    }
}
