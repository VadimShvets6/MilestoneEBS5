package com.top1shvetsvadim1.presentation.favorite

import com.top1shvetsvadim1.coreutils.BaseViewModel
import com.top1shvetsvadim1.coreutils.Reducer
import com.top1shvetsvadim1.coreutils.run
import com.top1shvetsvadim1.domain.models.ProductResponse
import com.top1shvetsvadim1.domain.useCase.GetFavoriteProductListUseCase
import com.top1shvetsvadim1.domain.useCase.RemoveProductFromFavoriteUseCase
import com.top1shvetsvadim1.presentation.mvi.FavoriteEvent
import com.top1shvetsvadim1.presentation.mvi.FavoriteIntent
import com.top1shvetsvadim1.presentation.mvi.FavoriteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteProductListUseCase: GetFavoriteProductListUseCase,
    private val removeProductFromFavoriteUseCase: RemoveProductFromFavoriteUseCase
) : BaseViewModel<FavoriteIntent, FavoriteState, FavoriteEvent>() {

    override val reducer: Reducer<FavoriteState, FavoriteEvent> = FavoriteReducer()

    override fun handleAction(action: FavoriteIntent) {
        when (action) {
            FavoriteIntent.LoadItems -> loadItems()
            is FavoriteIntent.RemoveItemFromFavorite -> removeItemFromFavorite(action.id)
        }
    }

    private fun removeItemFromFavorite(id: Int) {
        removeProductFromFavoriteUseCase.run(summoner = this, params = id)
    }

    private fun loadItems() {
        getFavoriteProductListUseCase.run(this)
    }

    inner class FavoriteReducer :
        Reducer<FavoriteState, FavoriteEvent>(FavoriteState(true, flowOf())) {

        override suspend fun onLoading() {
            sendState(
                currentState.copy(
                    isLoading = true
                )
            )
        }

        override suspend fun onError(error: Exception) {
            //Do nothing
        }

        override suspend fun handlePayload(payload: Any) {
            when (payload) {
                is ProductResponse -> sendState(
                    currentState.copy(
                        items = payload.result,
                        isLoading = false
                    )
                )
                //TODO: else
                else -> sendState(
                    currentState.copy(isLoading = false)
                )
            }
        }
    }
}