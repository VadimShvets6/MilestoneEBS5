package com.top1shvetsvadim1.presentation.mvi

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.ViewEvent
import com.top1shvetsvadim1.coreutils.ViewIntent
import com.top1shvetsvadim1.coreutils.ViewState
import com.top1shvetsvadim1.domain.uimodels.ProductUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class CartState(
    val isLoading: Boolean = true,
    val items: Flow<List<ProductUIModel>> = flowOf()
) : ViewState

sealed interface CartEvent : ViewEvent {
    object GeneralException : CartEvent
    object ShowNoInternet : CartEvent
}

sealed interface CartIntent : ViewIntent {
    data class LoadItem(val id: Int) : CartIntent
    data class ChangeStateItem(val id: Int) : CartIntent
}
