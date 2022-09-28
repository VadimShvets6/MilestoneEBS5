package com.top1shvetsvadim1.presentation.mvi

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.ViewEvent
import com.top1shvetsvadim1.coreutils.ViewIntent
import com.top1shvetsvadim1.coreutils.ViewState
import com.top1shvetsvadim1.domain.models.Parameters

//TODO: move MVI components in corresponding packages.
data class DetailState(
    val isLoading: Boolean = true,
    val item: List<BaseUIModel> = emptyList(),
    val isFavorite: Boolean = false,
    val text: String = "",
    val color: String = "#FFFFFFFF"
) : ViewState

sealed interface DetailEvent : ViewEvent {
    object GeneralException : DetailEvent
    object ShowNoInternet : DetailEvent
}

sealed interface DetailIntent : ViewIntent {
    data class LoadItem(val id: Int) : DetailIntent
    data class Remote(val param: Parameters, val key: String) : DetailIntent
    data class ChangeStateItem(val id: Int) : DetailIntent
    data class AddItemToCart(val id: Int) : DetailIntent
}