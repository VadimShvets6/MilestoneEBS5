package com.top1shvetsvadim1.feature_main.favorite

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.ViewEvent
import com.top1shvetsvadim1.coreutils.ViewIntent
import com.top1shvetsvadim1.coreutils.ViewState
import com.top1shvetsvadim1.domain.models.Filters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class FavoriteState(
    val isLoading: Boolean = false,
    val items: Flow<List<BaseUIModel>> = flowOf(),
    val itemsFilter: List<BaseUIModel> = emptyList(),
) : ViewState

sealed interface FavoriteEvent : ViewEvent {
    object NoEvents : ViewEvent
}

sealed interface FavoriteIntent : ViewIntent {
    data class LoadItems(val filter: Filters = Filters.RESET) : FavoriteIntent
    data class RemoveItemFromFavorite(val id: Int) : FavoriteIntent
}