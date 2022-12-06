package com.top1shvetsvadim1.domain.useCase

import com.top1shvetsvadim1.coreutils.UseCase
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.domain.models.ProductEntity
import com.top1shvetsvadim1.domain.models.ProductResponse
import com.top1shvetsvadim1.domain.repository.CartRepository
import com.top1shvetsvadim1.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetFavoriteProductListUseCase @Inject constructor(
    private val repository: FavoriteRepository,
    private val cartRepository: CartRepository
) : UseCase<Filters, ProductResponse> {
    override suspend fun invoke(params: Filters): ProductResponse {
        return combine(
            repository.getFavoriteList(params),
            cartRepository.getCartList(params)
        ) { favorite, cart ->
            favorite.map {
                ProductEntity(
                    id = it.id,
                    name = it.name,
                    details = it.details,
                    size = it.size,
                    colour = it.colour,
                    price = it.price,
                    count = it.count,
                    mainImage = it.mainImage,
                    isFavorite = it.isFavorite,
                    inCart = cart.map { item -> item.id }.contains(it.id)
                )
            }
        }.let {
            ProductResponse(it)
        }
    }
}