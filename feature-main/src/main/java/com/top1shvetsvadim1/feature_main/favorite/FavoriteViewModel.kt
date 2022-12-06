package com.top1shvetsvadim1.feature_main.favorite

import com.top1shvetsvadim1.coreui.*
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.BaseViewModel
import com.top1shvetsvadim1.coreutils.Reducer
import com.top1shvetsvadim1.coreutils.run
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.domain.models.TextTypes
import com.top1shvetsvadim1.domain.useCase.*
import com.top1shvetsvadim1.feature_main.delegate.ItemTexts
import com.top1shvetsvadim1.feature_main.delegate.ProductUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteProductListUseCase: GetFavoriteProductListUseCase,
    private val getBaseUIItemListUseCase: GetBaseUIItemListUseCase,
    private val removeProductFromFavoriteUseCase: RemoveProductFromFavoriteUseCase
) : BaseViewModel<FavoriteIntent, FavoriteState, FavoriteEvent>() {

    override val reducer: Reducer<FavoriteState, FavoriteEvent> = FavoriteReducer()

    override fun handleAction(action: FavoriteIntent) {
        when (action) {
            is FavoriteIntent.LoadItems -> {
                loadItems(action.filter)
                loadFiltersItem()
            }
            is FavoriteIntent.RemoveItemFromFavorite -> removeProductFromFavoriteUseCase.run(
                summoner = this,
                params = action.id,
                emitLoading = false
            )
        }
    }

    private fun loadFiltersItem() {
        getBaseUIItemListUseCase.run(summoner = this) {
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
        getFavoriteProductListUseCase.run(summoner = this, params = filters) {
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
        Reducer<FavoriteState, FavoriteEvent>(FavoriteState()) {

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
                is BaseUIResponse -> sendState(
                    currentState.copy(
                        itemsFilter = payload.list.first(),
                        isLoading = false
                    )
                )
            }
        }
    }
}