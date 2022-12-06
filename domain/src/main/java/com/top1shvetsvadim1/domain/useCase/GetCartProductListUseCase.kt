package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.UseCase
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.domain.models.ProductEntity
import com.top1shvetsvadim1.domain.models.ProductResponse
import com.top1shvetsvadim1.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCartProductListUseCase @Inject constructor(
    private val repository: CartRepository
) : UseCase<Filters, ProductResponse> {
    override suspend fun invoke(params: Filters): ProductResponse = repository.getCartList(params).map {
        it.map { entity ->
            ProductEntity(
                name = entity.name,
                size = entity.size,
                price = entity.price,
                mainImage = entity.mainImage,
                id = entity.id,
                inCart = entity.inCart,
                details = entity.details,
                colour = entity.colour,
                isFavorite = entity.isFavorite,
                count = entity.count,
            )
        }
    }.let {
        ProductResponse(it)
    }
}

@JvmInline
value class CartSize(
    val size: Flow<Int>
)