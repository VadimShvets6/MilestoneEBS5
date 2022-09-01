package com.top1shvetsvadim1.presentation.cart

import android.util.Log
import com.top1shvetsvadim1.coreutils.BaseViewModel
import com.top1shvetsvadim1.coreutils.Reducer
import com.top1shvetsvadim1.domain.models.ProductResponse
import com.top1shvetsvadim1.presentation.mvi.CartEvent
import com.top1shvetsvadim1.presentation.mvi.CartIntent
import com.top1shvetsvadim1.presentation.mvi.CartState
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(

) : BaseViewModel<CartIntent, CartState, CartEvent>() {

    override val reducer: Reducer<CartState, CartEvent> = CartReducer()

    override fun handleAction(action: CartIntent) {
        when (action) {
            is CartIntent.ChangeStateItem -> TODO()
            is CartIntent.LoadItem -> TODO()
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
                is ProductResponse -> sendState(
                    currentState.copy(
                        items = payload.result,
                        isLoading = false,
                    )
                )
                else -> {
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