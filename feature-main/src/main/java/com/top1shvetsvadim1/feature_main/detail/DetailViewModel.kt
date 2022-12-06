package com.top1shvetsvadim1.feature_main.detail

import android.util.Log
import com.top1shvetsvadim1.coreui.Colors
import com.top1shvetsvadim1.coreui.Dimen
import com.top1shvetsvadim1.coreui.Font
import com.top1shvetsvadim1.coreui.Strings
import com.top1shvetsvadim1.coreutils.*
import com.top1shvetsvadim1.domain.models.Parameters
import com.top1shvetsvadim1.domain.models.TextTypes
import com.top1shvetsvadim1.domain.useCase.*
import com.top1shvetsvadim1.feature_main.delegate.DetailProductUIModel
import com.top1shvetsvadim1.feature_main.delegate.ImageUIModel
import com.top1shvetsvadim1.feature_main.delegate.ItemTexts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getItemByIdUseCase: GetItemByIdUseCase,
    private val changeStateItemUseCase: ChangeStateItemUseCase,
    private val checkIfElementIsFavoriteUseCase: CheckIfElementIsFavoriteUseCase,
    private val addItemToCartUseCase: AddItemToCartUseCase,
    private val getDataFormDatastoreUseCase: GetDataFormDatastoreUseCase
) : BaseViewModel<DetailIntent, DetailState, DetailEvent>() {


    override val reducer: Reducer<DetailState, DetailEvent> = DetailReducer()

    override fun handleAction(action: DetailIntent) {
        when (action) {
            is DetailIntent.LoadItem -> {
                loadItem(action.id)
                checkIfItemIsFavorite(action.id)
            }
            is DetailIntent.ChangeStateItem -> {
                changeStateItem(action.id)
            }
            is DetailIntent.AddItemToCart -> addItemToCart(action.id)
            is DetailIntent.Remote -> getString(action.param, action.key)
        }
    }

    private fun addItemToCart(id: Int) {
        addItemToCartUseCase.run(summoner = this, params = id, emitLoading = false)
    }

    private fun checkIfItemIsFavorite(id: Int) {
        checkIfElementIsFavoriteUseCase.collect(summoner = this, params = id, emitLoading = false)
    }

    private fun changeStateItem(id: Int) {
        changeStateItemUseCase.run(summoner = this, params = id, emitLoading = false)
    }

    private fun getString(param: Parameters, key: String) {
        Log.d("Remote", "RemoteKey: $key")
        getDataFormDatastoreUseCase.collect(summoner = this, params = ParametersRemote(param, key), emitLoading = false)
    }

    private fun loadItem(id: Int) {
        getItemByIdUseCase.run(
            summoner = this,
            params = id,
        ) {
            it.map { item ->
                mutableListOf<BaseUIModel>().apply {
                    add(ImageUIModel(id = item.id, image = item.mainImage))
                    add(
                        DetailProductUIModel(
                            id = item.id,
                            name = item.name,
                            size = item.size,
                            price = item.price
                        )
                    )
                    add(
                        ItemTexts(
                            tag = item.id,
                            size = Dimen.text_size_big,
                            font = Font.open_sans_extrabold,
                            color = Colors.color_main_07195C,
                            text = TextTypes.ResText(Strings.information_text_view)
                        )
                    )
                    add(
                        ItemTexts(
                            tag = item.id,
                            size = Dimen.text_size_small_11_ssp,
                            font = Font.open_sans_regular,
                            color = Colors.text_description_424A56,
                            text = TextTypes.StringText(item.details)
                        )
                    )
                }
            }.let { flow ->
                DetailsResponse(flow)
            }
        }
    }

    inner class DetailReducer : Reducer<DetailState, DetailEvent>(DetailState()) {

        override suspend fun onLoading() {
            sendState(
                currentState.copy(
                    isLoading = true
                )
            )
        }

        //TODO: try to create generic error events.
        override suspend fun onError(error: Exception) {
            Log.d("OnError", "$error")
            when (error) {
                is UnknownHostException -> pushEvent(
                    DetailEvent.ShowNoInternet
                )
                is HttpException -> {
                    when (error.code()) {
                        404 -> {
                            Log.d("Error", "Error 404")
                        }
                    }
                }
                else -> pushEvent(
                    DetailEvent.GeneralException
                )
            }
        }

        override suspend fun handlePayload(payload: Any) {
            when (payload) {
                is DetailsResponse -> sendState(
                    currentState.copy(
                        item = payload.flow.first(),
                        isLoading = false,
                    )
                )
                //TODO: consider to use inline classes for primitives in responses.
                is Boolean -> sendState(
                    currentState.copy(
                        isFavorite = payload
                    )
                )
                is String -> sendState(
                    currentState.copy(
                        text = payload,
                        color = payload
                    )
                )
            }
        }
    }
}
