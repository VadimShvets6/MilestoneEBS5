package com.top1shvetsvadim1.presentation.detail

import android.util.Log
import com.top1shvetsvadim1.coreutils.BaseViewModel
import com.top1shvetsvadim1.coreutils.Reducer
import com.top1shvetsvadim1.coreutils.run
import com.top1shvetsvadim1.domain.ChangeStateItemUseCase
import com.top1shvetsvadim1.domain.GetItemByIdUseCase
import com.top1shvetsvadim1.domain.ProductEntity
import com.top1shvetsvadim1.presentation.mvi.DetailEvent
import com.top1shvetsvadim1.presentation.mvi.DetailIntent
import com.top1shvetsvadim1.presentation.mvi.DetailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getItemByIdUseCase: GetItemByIdUseCase,
    private val changeStateItemUseCase: ChangeStateItemUseCase
) : BaseViewModel<DetailIntent, DetailState, DetailEvent>() {


    override val reducer: Reducer<DetailState, DetailEvent> = DetailReducer()

    override fun handleAction(action: DetailIntent) {
        when (action) {
            is DetailIntent.LoadItem -> loadItem(action.id)
            is DetailIntent.ChangeStateItem -> changeStateItem(action.id)
        }
    }

    private fun changeStateItem(id: Int) {
        changeStateItemUseCase.run(summoner = this, params = id)
    }

    private fun loadItem(id: Int) {
        getItemByIdUseCase.run(summoner = this, params = id)
    }

    inner class DetailReducer : Reducer<DetailState, DetailEvent>(DetailState(true, null)) {

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
                    when(error.code()){
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
            is Flow<*> -> sendState(
                currentState.copy(
                    item = payload.first() as ProductEntity,
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