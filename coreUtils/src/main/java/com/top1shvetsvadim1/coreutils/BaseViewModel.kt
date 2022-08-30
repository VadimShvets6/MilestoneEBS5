package com.top1shvetsvadim1.coreutils

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<A : ViewIntent, S : ViewState, E : ViewEvent> : ViewModel() {

    abstract val reducer: Reducer<S, E>
    abstract fun handleAction(action: A)
}