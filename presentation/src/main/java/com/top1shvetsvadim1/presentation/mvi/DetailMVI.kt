package com.top1shvetsvadim1.presentation.mvi

import com.top1shvetsvadim1.coreutils.*

//TODO: move MVI components in corresponding packages.
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

//TODO: unused object
object NoEvents : ViewEvent