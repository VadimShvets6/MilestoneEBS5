package com.top1shvetsvadim1.coreutils

import android.util.Log
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class Reducer<S : ViewState, E : ViewEvent>(initialState: S) {

    private val _state = MutableStateFlow(initialState)
    val state = _state.asStateFlow()

    val currentState = state.value

    private val effectChannel = Channel<E>(Channel.RENDEZVOUS)
    val effects = effectChannel.receiveAsFlow()

    suspend fun manageResult(result: EventResult) {
        Log.d("Result", "$result")
        when (result) {
            is EventResult.Error -> onError(result.error)
            is EventResult.Loading -> onLoading()
            is EventResult.Success<*> -> handlePayload(result.payload.also {
                Log.d("Result", "class: ${it!!::class}")
            } ?: return)
        }
    }

    protected suspend fun sendState(newState: S) {
        _state.emit(newState)
    }

    protected suspend fun pushEvent(event: E) {
        effectChannel.send(event)
    }

    abstract suspend fun onLoading()
    abstract suspend fun onError(error: Exception)
    abstract suspend fun handlePayload(payload: Any)
}