package com.top1shvetsvadim1.presentation.mvi

import com.top1shvetsvadim1.coreutils.ViewEvent
import com.top1shvetsvadim1.coreutils.ViewIntent
import com.top1shvetsvadim1.coreutils.ViewState
import com.top1shvetsvadim1.domain.ProductUIModel
import kotlinx.coroutines.flow.Flow

data class FavoriteState(
    val isLoading: Boolean,
    val items: Flow<List<ProductUIModel>>
): ViewState

//TODO: If there are no events, consider using some generic object like NoEvents: ViewEvent
sealed interface FavoriteEvent: ViewEvent {

}

sealed interface FavoriteIntent: ViewIntent {
    object LoadItems: FavoriteIntent
    data class RemoveItemFromFavorite(val id: Int) : FavoriteIntent
}