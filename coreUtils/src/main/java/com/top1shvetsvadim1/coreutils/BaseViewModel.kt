package com.top1shvetsvadim1.coreutils

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<A: ViewIntent, S: ViewState, E: ViewEvent>: ViewModel() {

    abstract val reducer : Reducer<S, E>
    abstract fun handleAction(action: A)
}