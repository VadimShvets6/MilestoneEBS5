package com.top1shvetsvadim1.domain.uimodels

import com.squareup.moshi.JsonClass
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.Payloadable

@JsonClass(generateAdapter = true)
data class ProductUIModel(
    val name: String,
    val sizes: String,
    val price: Int,
    val mainImage: String,
    val id: Int,
    val isFavorite: Boolean,
    val inCart: Boolean
) : BaseUIModel {

    override fun areItemsTheSame(other: BaseUIModel): Boolean {
        return other is ProductUIModel && other.id == id
    }

    override fun areContentsTheSame(other: BaseUIModel): Boolean {
        return other is ProductUIModel && other.name == name &&
                other.price == price &&
                other.isFavorite == isFavorite &&
                other.mainImage == mainImage &&
                other.sizes == sizes &&
                other.inCart == inCart

    }

    override fun changePayload(other: BaseUIModel): Any {
        return mutableListOf<Payloadable>().apply {
            if (other is ProductUIModel) {
                if (other.name != name) {
                    add(ProductPayload.NameChanged(other.name))
                }
                if (other.price != price) {
                    add(ProductPayload.PriceChange(other.price))
                }
                if (other.mainImage != mainImage) {
                    add(ProductPayload.PictureChange(other.mainImage))
                }
                if (other.isFavorite != isFavorite) {
                    add(ProductPayload.FavoriteChange(other.isFavorite))
                }
                if (other.sizes != sizes) {
                    add(ProductPayload.SizeChange(other.sizes))
                }
                if(other.inCart != inCart){
                    add(ProductPayload.CartChange(other.inCart))
                }
            }
        }
    }
}

sealed interface ProductPayload : Payloadable {
    data class NameChanged(val newName: String) : ProductPayload
    data class PictureChange(val newPicture: String) : ProductPayload
    data class PriceChange(val newPrice: Int) : ProductPayload
    data class FavoriteChange(val newStatus: Boolean) : ProductPayload
    data class SizeChange(val newSize: String) : ProductPayload
    data class CartChange(val newStatus : Boolean) : ProductPayload
}
