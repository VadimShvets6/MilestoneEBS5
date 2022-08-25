package com.top1shvetsvadim1.presentation.mvi

import com.top1shvetsvadim1.coreutils.ViewEvent
import com.top1shvetsvadim1.coreutils.ViewIntent
import com.top1shvetsvadim1.coreutils.ViewState
import com.top1shvetsvadim1.domain.ProductEntity

data class DetailState(
    val isLoading: Boolean,
    val item: ProductEntity?
) : ViewState

sealed interface DetailEvent : ViewEvent {
    object GeneralException : DetailEvent
    object ShowNoInternet : DetailEvent
}

sealed interface DetailIntent : ViewIntent {
    data class LoadItem(val id: Int) : DetailIntent
    data class ChangeStateItem(val id: Int) : DetailIntent
}

object NoEvents : ViewEvent