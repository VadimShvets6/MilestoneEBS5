package com.top1shvetsvadim1.domain

import com.top1shvetsvadim1.coreutils.UseCaseNoParams
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(
    private val repository: ProductRepository
) : UseCaseNoParams<ProductResponse> {

    override suspend operator fun invoke() = repository.getProductList().map {
        it.map { entity ->
            ProductUIModel(
                name = entity.name,
                size = entity.size,
                price = entity.price,
                mainImage = entity.mainImage,
                id = entity.id,
                isFavorite = entity.isFavorite
            )
        }
    }.let {
        ProductResponse(it)
    }
}

