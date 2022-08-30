package com.top1shvetsvadim1.coreutils

interface BaseUIModel {

    fun areItemsTheSame(other: BaseUIModel): Boolean

    fun areContentsTheSame(other: BaseUIModel): Boolean

    fun changePayload(other: BaseUIModel): Any
}

interface Payloadable