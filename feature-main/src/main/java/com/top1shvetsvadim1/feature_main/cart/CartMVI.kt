package com.top1shvetsvadim1.feature_main.cart

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.ViewEvent
import com.top1shvetsvadim1.coreutils.ViewIntent
import com.top1shvetsvadim1.coreutils.ViewState
import com.top1shvetsvadim1.domain.models.Filters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class CartState(
    val isLoading: Boolean = false,
    val items: Flow<List<BaseUIModel>> = flowOf(),
    val itemsFilter: List<BaseUIModel> = emptyList(),
) : ViewState

sealed interface CartEvent : ViewEvent {
    object GeneralException : CartEvent
    object ShowNoInternet : CartEvent
}

sealed interface CartIntent : ViewIntent {
    data class LoadItem(val filters: Filters = Filters.RESET) : CartIntent
    data class RemoveItemFromCart(val id: Int) : CartIntent
}
