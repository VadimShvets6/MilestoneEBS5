package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.UseCase
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.domain.models.ProductEntity
import com.top1shvetsvadim1.domain.repository.CartRepository
import com.top1shvetsvadim1.domain.repository.FavoriteRepository
import com.top1shvetsvadim1.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetItemByIdUseCase @Inject constructor(
    private val repository: ProductRepository,
    private val favoriteRepository: FavoriteRepository,
    private val cartRepository: CartRepository
) : UseCase<Int, Flow<ProductEntity>> {
    override suspend fun invoke(params: Int): Flow<ProductEntity> {
        return combine(
            repository.getItemById(params),
            favoriteRepository.getFavoriteList(Filters.RESET),
            cartRepository.getCartList()
        ){product, favorite, cart ->
            ProductEntity(
                id = product.id,
                name = product.name,
                details = product.details,
                size = product.size,
                colour = product.colour,
                price = product.price,
                mainImage = product.mainImage,
                isFavorite = favorite.map { it.id }.contains(product.id),
                inCart = cart.map { it.id }.contains(product.id),
                count = product.count
            )
        }
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

