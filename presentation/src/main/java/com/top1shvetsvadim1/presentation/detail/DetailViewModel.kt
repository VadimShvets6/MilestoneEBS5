package com.top1shvetsvadim1.presentation.detail

import android.util.Log
import com.top1shvetsvadim1.coreutils.*
import com.top1shvetsvadim1.domain.*
import com.top1shvetsvadim1.presentation.mvi.DetailEvent
import com.top1shvetsvadim1.presentation.mvi.DetailIntent
import com.top1shvetsvadim1.presentation.mvi.DetailState
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
    private val checkIfElementIsFavoriteUseCase: CheckIfElementIsFavoriteUseCase
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
                checkIfItemIsFavorite(action.id)
            }
        }
    }

    private fun checkIfItemIsFavorite(id: Int) {
        checkIfElementIsFavoriteUseCase.collect(summoner = this, params = id)
    }

    private fun changeStateItem(id: Int) {
        changeStateItemUseCase.run(summoner = this, params = id)
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
                    add(DescriptionUIModel(id = item.id, description = item.details))
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
                is Boolean -> sendState(
                    currentState.copy(
                        isFavorite = payload
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
