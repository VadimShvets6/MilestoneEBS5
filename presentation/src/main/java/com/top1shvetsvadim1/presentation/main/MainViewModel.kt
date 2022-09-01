package com.top1shvetsvadim1.presentation.main

import android.util.Log
import com.top1shvetsvadim1.coreutils.BaseViewModel
import com.top1shvetsvadim1.coreutils.Reducer
import com.top1shvetsvadim1.coreutils.run
import com.top1shvetsvadim1.domain.AddItemToFavoriteUseCase
import com.top1shvetsvadim1.domain.GetProductListUseCase
import com.top1shvetsvadim1.domain.ProductResponse
import com.top1shvetsvadim1.domain.RemoveProductFromFavoriteUseCase
import com.top1shvetsvadim1.presentation.mvi.MainEvent
import com.top1shvetsvadim1.presentation.mvi.MainIntent
import com.top1shvetsvadim1.presentation.mvi.MainState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getProductListUseCase: GetProductListUseCase,
    private val addItemToFavoriteUseCase: AddItemToFavoriteUseCase,
    private val removeProductFromFavoriteUseCase: RemoveProductFromFavoriteUseCase
) : BaseViewModel<MainIntent, MainState, MainEvent>() {

    override val reducer: Reducer<MainState, MainEvent> = MainReducer()

    override fun handleAction(action: MainIntent) {
        when (action) {
            MainIntent.LoadItems -> loadItems()
            is MainIntent.AddItemToFavorite -> addItemToFavorite(action.id)
            is MainIntent.RemoveItemFromFavorite -> removeItemFromFavorite(action.id)
        }
    }

    //TODO: you can move this functions directly in handleActions, because they are one-row-sized.
    private fun removeItemFromFavorite(id : Int) {
        removeProductFromFavoriteUseCase.run(summoner = this, params = id)
    }

    private fun addItemToFavorite(id: Int) {
        addItemToFavoriteUseCase.run(summoner = this, params = id)
    }

    private fun loadItems() {
        getProductListUseCase.run(this)
    }

    inner class MainReducer : Reducer<MainState, MainEvent>(MainState(false, flowOf())) {

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
                    MainEvent.ShowNoInternet
                )
                is HttpException -> {
                    when(error.code()){
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
            when (payload) {
                is ProductResponse -> sendState(
                    currentState.copy(
                        items = payload.result,
                        isLoading = false
                    )
                )
                else -> sendState(
                    currentState.copy(isLoading = false)
                )
            }
        }
    }
}