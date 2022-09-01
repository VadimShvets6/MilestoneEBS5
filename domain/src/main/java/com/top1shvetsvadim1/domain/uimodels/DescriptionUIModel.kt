package com.top1shvetsvadim1.domain.uimodels

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.Payloadable

data class DescriptionUIModel(
    val id: Int,
    val description: String
) : BaseUIModel {


    override fun areItemsTheSame(other: BaseUIModel): Boolean {
        return other is DescriptionUIModel && other.id == id
    }

    override fun areContentsTheSame(other: BaseUIModel): Boolean {
        return other is DescriptionUIModel && other.description == description
    }

    override fun changePayload(other: BaseUIModel): Any {
        return mutableListOf<Payloadable>().apply {
            if (other is DescriptionUIModel) {
                if (other.description != description) {
                    add(DescriptionPayloads.DescriptionChanged(other.description))
                }
            }
        }
    }

    sealed interface DescriptionPayloads : Payloadable {
        data class DescriptionChanged(val newDescription: String) : DescriptionPayloads
    }
}

