package com.top1shvetsvadim1.presentation.cart

import android.util.Log
import com.top1shvetsvadim1.coreutils.BaseViewModel
import com.top1shvetsvadim1.coreutils.Reducer
import com.top1shvetsvadim1.coreutils.run
import com.top1shvetsvadim1.domain.useCase.GetCartProductListUseCase
import com.top1shvetsvadim1.domain.useCase.ProductUIResponse
import com.top1shvetsvadim1.domain.useCase.RemoveProductFromCartUseCase
import com.top1shvetsvadim1.presentation.delegate.ProductCartUIModel
import com.top1shvetsvadim1.presentation.mvi.CartEvent
import com.top1shvetsvadim1.presentation.mvi.CartIntent
import com.top1shvetsvadim1.presentation.mvi.CartState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartProductListUseCase: GetCartProductListUseCase,
    private val removeProductFromCartUseCase: RemoveProductFromCartUseCase
) : BaseViewModel<CartIntent, CartState, CartEvent>() {

    override val reducer: Reducer<CartState, CartEvent> = CartReducer()

    override fun handleAction(action: CartIntent) {
        when (action) {
            CartIntent.LoadItem -> loadItems()
            is CartIntent.RemoveItemFromCart -> removeProductFromCartUseCase.run(
                summoner = this,
                params = action.id,
                emitLoading = false
            )
        }
    }

    private fun loadItems() {
        getCartProductListUseCase.run(summoner = this) {
            it.result.map { list ->
                list.map { item ->
                    ProductCartUIModel(
                        name = item.name,
                        sizes = item.size,
                        price = item.price,
                        mainImage = item.mainImage,
                        id = item.id,
                        inCart = item.inCart,
                        count = item.count
                    )
                }
            }.let { flow ->
                ProductUIResponse(flow)
            }
        }
    }

    inner class CartReducer : Reducer<CartState, CartEvent>(CartState()) {

        override suspend fun onLoading() {
            sendState(
                currentState.copy(
                    isLoading = true
                )
            )
        }

        override suspend fun onError(error: Exception) {
            Log.d("OnError", "$error")
            when (error) {
                is UnknownHostException -> pushEvent(
                    CartEvent.ShowNoInternet
                )
                is HttpException -> {
                    when (error.code()) {
                        404 -> {
                            Log.d("Error", "Error 404")
                        }
                    }
                }
                else -> pushEvent(
                    CartEvent.GeneralException
                )
            }
        }

        override suspend fun handlePayload(payload: Any) {
            when (payload) {
                is ProductUIResponse -> sendState(
                    currentState.copy(
                        items = payload.flow,
                        isLoading = false,
                    )
                )
                Unit -> {
                    sendState(
                        currentState.copy(
                            isLoading = false
                        )
                    )
                }
            }
        }
    }
}