package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.UseCase
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.domain.models.ProductEntity
import com.top1shvetsvadim1.domain.models.ProductPagingResponse
import com.top1shvetsvadim1.domain.models.ProductResponse
import com.top1shvetsvadim1.domain.repository.CartRepository
import com.top1shvetsvadim1.domain.repository.FavoriteRepository
import com.top1shvetsvadim1.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(
    private val repository: ProductRepository,
    private val favoriteRepository: FavoriteRepository,
    private val cartRepository: CartRepository
) : UseCase<Filters, ProductResponse> {

    override suspend operator fun invoke(params: Filters): ProductResponse {
        return combine(
            repository.getProductList(params),
            favoriteRepository.getFavoriteList(params),
            cartRepository.getCartList()
        ){product, favorite, cart ->
            product.map {
                ProductEntity(
                    id = it.id,
                    name = it.name,
                    details = it.details,
                    size = it.size,
                    colour = it.colour,
                    price = it.price,
                    count = it.count,
                    mainImage = it.mainImage,
                    isFavorite = favorite.map { item -> item.id }.contains(it.id),
                    inCart = cart.map { item -> item.id }.contains(it.id)
                )
            }
        }.let {
            ProductResponse(it)
        }
        //return ProductResponse(repository.getProductList(params))
    }
}

@JvmInline
value class ProductUIResponse(
    val flow: Flow<List<BaseUIModel>>
)


