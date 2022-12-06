package com.top1shvetsvadim1.feature_main.cart

import android.util.Log
import com.top1shvetsvadim1.coreui.*
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.BaseViewModel
import com.top1shvetsvadim1.coreutils.Reducer
import com.top1shvetsvadim1.coreutils.run
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.domain.models.TextTypes
import com.top1shvetsvadim1.domain.useCase.*
import com.top1shvetsvadim1.feature_main.delegate.ItemTexts
import com.top1shvetsvadim1.feature_main.delegate.ProductCartUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getCartProductListUseCase: GetCartProductListUseCase,
    private val removeProductFromCartUseCase: RemoveProductFromCartUseCase,
    private val getBaseUIItemListUseCase: GetBaseUIItemListUseCase
) : BaseViewModel<CartIntent, CartState, CartEvent>() {

    override val reducer: Reducer<CartState, CartEvent> = CartReducer()

    override fun handleAction(action: CartIntent) {
        when (action) {
            is CartIntent.LoadItem -> {
                loadItems(action.filters)
                loadFiltersItem()
            }
            is CartIntent.RemoveItemFromCart -> removeProductFromCartUseCase.run(
                summoner = this,
                params = action.id,
                emitLoading = false
            )
        }
    }

    private fun loadItems(filters: Filters) {
        getCartProductListUseCase.run(summoner = this, params = filters) {
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

    private fun loadFiltersItem() {
        getBaseUIItemListUseCase.run(summoner = this, emitLoading = false) {
            it.map {
                mutableListOf<BaseUIModel>().apply {
                    add(
                        ItemTexts(
                            tag = 1,
                            size = Dimen.text_size_14_ssp,
                            font = Font.open_sans_regular,
                            color = Colors.text_color_252C32,
                            text = TextTypes.ResText(Strings.price),
                            iconStart = Drawable.ic_money
                        )
                    )
                    add(
                        ItemTexts(
                            tag = 2,
                            size = Dimen.text_size_14_ssp,
                            font = Font.open_sans_regular,
                            color = Colors.text_color_252C32,
                            text = TextTypes.ResText(Strings.size),
                            iconStart = Drawable.ic_pinch
                        )
                    )
                    add(
                        ItemTexts(
                            tag = 3,
                            size = Dimen.text_size_14_ssp,
                            font = Font.open_sans_regular,
                            color = Colors.text_color_252C32,
                            text = TextTypes.ResText(Strings.color),
                            iconStart = Drawable.ic_color
                        )
                    )
                    add(
                        ItemTexts(
                            tag = 4,
                            size = Dimen.text_size_14_ssp,
                            font = Font.open_sans_regular,
                            color = Colors.text_color_252C32,
                            text = TextTypes.ResText(Strings.reset_filters),
                            iconStart = Drawable.ic_filter_alt_off
                        )
                    )
                }
            }.let { flow ->
                BaseUIResponse(flow)
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
                is BaseUIResponse -> sendState(
                    currentState.copy(
                        itemsFilter = payload.list.first()
                    )
                )
            }
        }
    }
}