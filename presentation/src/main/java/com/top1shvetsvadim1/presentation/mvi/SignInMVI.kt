package com.top1shvetsvadim1.presentation.mvi

import com.top1shvetsvadim1.coreutils.ViewEvent
import com.top1shvetsvadim1.coreutils.ViewIntent
import com.top1shvetsvadim1.coreutils.ViewState

data class SignInState(
    val isLoading: Boolean = false,
    val isLogged: Boolean = false
) : ViewState

sealed interface SignInEvent : ViewEvent {
    object NoEvents : SignInEvent
}

sealed interface SignInIntent : ViewIntent {
    data class PostLogin(val email: String, val password: String) : SignInIntent
}