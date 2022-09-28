package com.top1shvetsvadim1.presentation.mvi

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.ViewEvent
import com.top1shvetsvadim1.coreutils.ViewIntent
import com.top1shvetsvadim1.coreutils.ViewState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class FavoriteState(
    val isLoading: Boolean,
    val items: Flow<List<BaseUIModel>> = flowOf()
) : ViewState

sealed interface FavoriteEvent : ViewEvent {
    object NoEvents : ViewEvent
}

sealed interface FavoriteIntent : ViewIntent {
    object LoadItems : FavoriteIntent
    data class RemoveItemFromFavorite(val id: Int) : FavoriteIntent
}