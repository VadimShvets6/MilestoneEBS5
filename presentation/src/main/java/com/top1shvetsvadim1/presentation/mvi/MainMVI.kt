package com.top1shvetsvadim1.presentation.mvi

import com.top1shvetsvadim1.coreutils.ViewEvent
import com.top1shvetsvadim1.coreutils.ViewIntent
import com.top1shvetsvadim1.coreutils.ViewState
import com.top1shvetsvadim1.domain.ProductUIModel
import kotlinx.coroutines.flow.Flow

data class MainState(
    val isLoading: Boolean,
    val items: Flow<List<ProductUIModel>>
) : ViewState

sealed interface MainEvent : ViewEvent {
    object GeneralException : MainEvent
    object ShowNoInternet : MainEvent
}

sealed interface MainIntent : ViewIntent {
    object LoadItems : MainIntent
    data class AddItemToFavorite(val id: Int) : MainIntent
    data class RemoveItemFromFavorite(val id: Int) : MainIntent
}

object NoEvent : ViewEvent