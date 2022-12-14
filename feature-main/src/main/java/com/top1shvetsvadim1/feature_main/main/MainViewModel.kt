package com.top1shvetsvadim1.feature_main.main

import android.util.Log
import com.top1shvetsvadim1.coreui.*
import com.top1shvetsvadim1.coreutils.*
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.domain.models.TextTypes
import com.top1shvetsvadim1.domain.useCase.*
import com.top1shvetsvadim1.feature_main.delegate.ItemTexts
import com.top1shvetsvadim1.feature_main.delegate.ProductUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProductListUseCase: GetProductListUseCase,
    private val addItemToFavoriteUseCase: AddItemToFavoriteUseCase,
    private val removeProductFromFavoriteUseCase: RemoveProductFromFavoriteUseCase,
    private val addItemToCartUseCase: AddItemToCartUseCase,
    private val getCartListSizeUseCase: GetCartListSizeUseCase,
    private val getBaseUIItemListUseCase: GetBaseUIItemListUseCase,
    private val removeProductFromCartUseCase: RemoveProductFromCartUseCase
) : BaseViewModel<MainIntent, MainState, MainEvent>() {

    override val reducer: Reducer<MainState, MainEvent> = MainReducer()

    override fun handleAction(action: MainIntent) {
        when (action) {
            is MainIntent.LoadItems -> {
                loadItems(action.filter)
                loadFiltersItem()
                getCartListSize()
            }
            is MainIntent.AddItemToFavorite -> addItemToFavoriteUseCase.run(
                summoner = this,
                params = action.id,
                emitLoading = false
            )
            is MainIntent.RemoveItemFromFavorite -> removeProductFromFavoriteUseCase.run(
                summoner = this,
                params = action.id,
                emitLoading = false
            )
            is MainIntent.AddItemToCart -> {
                addItemToCartUseCase.run(
                    summoner = this,
                    params = action.id,
                    emitLoading = false
                )
                getCartListSize()
            }
            is MainIntent.RemoveItemFromCart -> {
                removeProductFromCartUseCase.run(
                    summoner = this,
                    params = action.id,
                    emitLoading = false
                )
                getCartListSize()
            }
        }
    }

    private fun getCartListSize() {
        getCartListSizeUseCase.run(summoner = this, emitLoading = false) {
            CartSize(it)
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

    private fun loadItems(filters: Filters) {
        getProductListUseCase.run(summoner = this, params = filters, emitLoading = true) {
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

    inner class MainReducer : Reducer<MainState, MainEvent>(MainState()) {

        override suspend fun onLoading() {
            sendState(
                currentState.copy(
                    isLoading = true
                )
            )
        }

        override suspend fun onError(error: Exception) {
            when (error) {
                is UnknownHostException -> pushEvent(
                    // GeneralEvent.GeneralException
                    MainEvent.ShowNoInternet
                )
                is HttpException -> {
                    when (error.code()) {
                        404 -> {
                            Log.d("Error", "Error 404")
                        }
                    }
                }
                else -> pushEvent(
                    MainEvent.GeneralException
                )
            }
        }

        override suspend fun handlePayload(payload: Any) {
            Log.d("Test", "Payload: $payload")
            when (payload) {
                is ProductUIResponse -> {
                    sendState(
                        currentState.copy(
                            item = payload.flow,
                            // item = payload.flow.cachedIn(viewModelScope) as Flow<PagingData<BaseUIModel>>,
                            isLoading = false
                        )
                    )
                }
                is BaseUIResponse -> sendState(
                    currentState.copy(
                        itemsFilter = payload.list.first()
                    )
                )
                is CartSize -> sendState(
                    currentState.copy(
                        cartSize = payload.size.first()
                    )
                )
            }
        }
    }
}
