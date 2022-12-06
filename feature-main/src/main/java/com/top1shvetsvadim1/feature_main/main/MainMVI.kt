package com.top1shvetsvadim1.feature_main.main

import com.top1shvetsvadim1.coreutils.*
import com.top1shvetsvadim1.domain.models.Filters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class MainState(
    val isLoading: Boolean = false,
    val item: Flow<List<BaseUIModel>> = flowOf(),
    val itemsFilter: List<BaseUIModel> = emptyList(),
    val cartSize: Int = 0
) : ViewState

sealed interface MainEvent : ViewEvent {
    object GeneralException : MainEvent
    object ShowNoInternet : MainEvent
}

sealed interface MainIntent : ViewIntent {
    data class LoadItems(val filter: Filters = Filters.RESET) : MainIntent
    data class AddItemToFavorite(val id: Int) : MainIntent
    data class RemoveItemFromFavorite(val id: Int) : MainIntent
    data class AddItemToCart(val id: Int) : MainIntent
    data class RemoveItemFromCart(val id: Int) : MainIntent
}

object NoEvent : ViewEvent