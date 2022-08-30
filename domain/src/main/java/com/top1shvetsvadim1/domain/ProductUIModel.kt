package com.top1shvetsvadim1.domain

import com.squareup.moshi.JsonClass
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.Payloadable

@JsonClass(generateAdapter = true)
data class ProductUIModel(
    val name: String,
    val size: String,
    val price: Int,
    val mainImage: String,
    val id: Int,
    val isFavorite : Boolean
) : BaseUIModel{

    override fun areItemsTheSame(other: BaseUIModel): Boolean {
        return other is ProductUIModel && other.id == id
    }

    override fun areContentsTheSame(other: BaseUIModel): Boolean {
        return other is ProductUIModel && other.name == name &&
                other.price == price &&
                other.isFavorite == isFavorite &&
                other.mainImage == mainImage &&
                other.size == size

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
                if(other.isFavorite != isFavorite){
                    add(ProductPayload.FavoriteChange(other.isFavorite))
                }
            }
        }
    }
}

sealed interface ProductPayload : Payloadable {
    data class NameChanged(val newName: String) : ProductPayload
    data class PictureChange(val newPicture: String) : ProductPayload
    data class PriceChange(val newPrice: Int) : ProductPayload
    data class FavoriteChange(val newStatus : Boolean) : ProductPayload
}