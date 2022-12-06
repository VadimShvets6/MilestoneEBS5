package com.top1shvetsvadim1.feature_auth.register

import com.top1shvetsvadim1.coreutils.ViewEvent
import com.top1shvetsvadim1.coreutils.ViewIntent
import com.top1shvetsvadim1.coreutils.ViewState

data class RegisterState(
    val isLoading: Boolean = false,
    val isLogged: Boolean = false
) : ViewState

sealed interface RegisterEvent : ViewEvent {
    object NoEvents : RegisterEvent
}

sealed interface RegisterIntent : ViewIntent {
    data class PostRegister(val fullName: String, val email: String, val password: String) : RegisterIntent
}