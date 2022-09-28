package com.top1shvetsvadim1.presentation.mvi

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.ViewEvent
import com.top1shvetsvadim1.coreutils.ViewIntent
import com.top1shvetsvadim1.coreutils.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class MainState(
    val isLoading: Boolean,
    val item: Flow<List<BaseUIModel>> = flowOf(),
    val itemsFilter: List<BaseUIModel> = emptyList(),
) : ViewState

sealed interface MainEvent : ViewEvent {
    object GeneralException : MainEvent
    object ShowNoInternet : MainEvent
}

sealed interface MainIntent : ViewIntent {
    data class LoadItems(val filter: String = "") : MainIntent
    data class AddItemToFavorite(val id: Int) : MainIntent
    data class RemoveItemFromFavorite(val id: Int) : MainIntent
    data class AddItemToCart(val id: Int) : MainIntent
    data class RemoveItemFromCart(val id: Int) : MainIntent
}

object NoEvent : ViewEvent