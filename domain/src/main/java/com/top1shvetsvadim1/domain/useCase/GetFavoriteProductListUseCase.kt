package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.UseCaseNoParams
import com.top1shvetsvadim1.domain.models.ProductEntity
import com.top1shvetsvadim1.domain.models.ProductResponse
import com.top1shvetsvadim1.domain.repository.ProductRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFavoriteProductListUseCase @Inject constructor(
    private val repository: ProductRepository
) : UseCaseNoParams<ProductResponse> {
    override suspend fun invoke(): ProductResponse = repository.getFavoriteList().map {
        it.map { item ->
            ProductEntity(
                id = item.id,
                name = item.name,
                details = item.details,
                size = item.size,
                colour = item.colour,
                price = item.price,
                mainImage = item.mainImage,
                isFavorite = item.isFavorite,
                inCart = item.inCart,
                count = item.count,
            )
        }
    }.let {
        ProductResponse(it)
    }
}