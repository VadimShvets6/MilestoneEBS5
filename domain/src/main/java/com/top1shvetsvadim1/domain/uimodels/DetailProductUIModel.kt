package com.top1shvetsvadim1.domain.uimodels

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.Payloadable

data class DetailProductUIModel(
    val id: Int,
    val name: String,
    val size: String,
    val price: Int,
) : BaseUIModel {

    override fun areItemsTheSame(other: BaseUIModel): Boolean {
        return other is DetailProductUIModel && other.id == id
    }

    override fun areContentsTheSame(other: BaseUIModel): Boolean {
        return other is DetailProductUIModel && other.name == name && other.price == price && other.size == size
    }

    override fun changePayload(other: BaseUIModel): Any {
        return mutableListOf<Payloadable>().apply {
            if (other is DetailProductUIModel) {
                if (other.price != price) {
                    add(DetailProductPayloads.PriceChanged(other.price))
                }
            }
        }
    }

    sealed interface DetailProductPayloads : Payloadable {
        data class PriceChanged(val newPrice: Int) : DetailProductPayloads
    }
}
