package com.top1shvetsvadim1.domain

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.Payloadable

data class ImageUIModel(
    val id: Int,
    val image: String
) : BaseUIModel {


    override fun areItemsTheSame(other: BaseUIModel): Boolean {
        return other is ImageUIModel && other.id == id
    }

    override fun areContentsTheSame(other: BaseUIModel): Boolean {
        return other is ImageUIModel && other.image == image
    }

    override fun changePayload(other: BaseUIModel): Any {
        return mutableListOf<Payloadable>().apply {
            //TODO: not exhaustive payloads. There are many cases when this lead to misbehaviour.
            //TODO: you can try Novalles lib here
            if (other is ImageUIModel) {
                if (other.image != image) {
                    add(ImagePayloads.ImageChanged(other.image))
                }
            }
        }
    }

    sealed interface ImagePayloads : Payloadable {
        data class ImageChanged(val newImage: String) : ImagePayloads
    }
}