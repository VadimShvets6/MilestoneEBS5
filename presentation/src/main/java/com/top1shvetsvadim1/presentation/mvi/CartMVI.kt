package com.top1shvetsvadim1.presentation.mvi

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.ViewEvent
import com.top1shvetsvadim1.coreutils.ViewIntent
import com.top1shvetsvadim1.coreutils.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

//TODO: MVI classes should be in the same packages as their target
data class CartState(
    val isLoading: Boolean = true,
    val items: Flow<List<BaseUIModel>> = flowOf()
) : ViewState

sealed interface CartEvent : ViewEvent {
    object GeneralException : CartEvent
    object ShowNoInternet : CartEvent
}

sealed interface CartIntent : ViewIntent {
    object LoadItem : CartIntent
    data class RemoveItemFromCart(val id: Int) : CartIntent
}
