package com.top1shvetsvadim1.domain

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductUIModel(
    val name: String,
    val size: String,
    val price: Int,
    val mainImage: String,
    val id: Int,
    val isFavorite : Boolean
)

sealed interface ProductPayload {
    data class NameChanged(val newName: String) : ProductPayload
    data class PictureChange(val newPicture: String) : ProductPayload
    data class PriceChange(val newPrice: Int) : ProductPayload
    data class FavoriteChange(val newStatus : Boolean) : ProductPayload
}