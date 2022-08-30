package com.top1shvetsvadim1.presentation.mvi

import com.top1shvetsvadim1.coreutils.*

data class DetailState(
    val isLoading: Boolean = true,
    val item: List<BaseUIModel> = emptyList(),
    val isFavorite: Boolean = false
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