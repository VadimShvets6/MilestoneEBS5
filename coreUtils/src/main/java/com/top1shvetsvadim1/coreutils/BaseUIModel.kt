package com.top1shvetsvadim1.coreutils

import com.flexeiprata.novalles.interfaces.UIModelHelper

abstract class BaseUIModel {

    fun areItemsTheSame(other: BaseUIModel, helper: UIModelHelper<BaseUIModel>): Boolean {
        return helper.areItemsTheSame(this, other)
    }

    fun areContentTheSame(other: BaseUIModel, helper: UIModelHelper<BaseUIModel>): Boolean {
        return helper.areContentsTheSame(this, other)
    }

    fun changePayload(other: BaseUIModel, helper: UIModelHelper<BaseUIModel>): List<Any> {
        return helper.changePayloads(this, other)
    }
}