package com.top1shvetsvadim1.presentation.favorite

import com.top1shvetsvadim1.coreutils.BaseViewModel
import com.top1shvetsvadim1.coreutils.Reducer
import com.top1shvetsvadim1.coreutils.run
import com.top1shvetsvadim1.domain.useCase.GetFavoriteProductListUseCase
import com.top1shvetsvadim1.domain.useCase.ProductUIResponse
import com.top1shvetsvadim1.domain.useCase.RemoveProductFromFavoriteUseCase
import com.top1shvetsvadim1.presentation.delegate.ProductUIModel
import com.top1shvetsvadim1.presentation.mvi.FavoriteEvent
import com.top1shvetsvadim1.presentation.mvi.FavoriteIntent
import com.top1shvetsvadim1.presentation.mvi.FavoriteState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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
            is FavoriteIntent.RemoveItemFromFavorite -> removeProductFromFavoriteUseCase.run(
                summoner = this,
                params = action.id
            )
        }
    }

    private fun loadItems() {
        getFavoriteProductListUseCase.run(this) {
            it.result.map { list ->
                list.map { item ->
                    ProductUIModel(
                        name = item.name,
                        sizes = item.size,
                        price = item.price,
                        mainImage = item.mainImage,
                        id = item.id,
                        isFavorite = item.isFavorite,
                        inCart = item.inCart
                    )
                }
            }.let { flow ->
                ProductUIResponse(flow)
            }
        }
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
            //TODO: do something :D
            //Do nothing
        }

        override suspend fun handlePayload(payload: Any) {
            when (payload) {
                is ProductUIResponse -> sendState(
                    currentState.copy(
                        items = payload.flow,
                        isLoading = false
                    )
                )
                //TODO: Unit types should not be in payload
                Unit -> sendState(
                    currentState.copy(isLoading = false)
                )
            }
        }
    }
}