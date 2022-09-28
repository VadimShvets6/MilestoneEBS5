package com.top1shvetsvadim1.presentation.mvi

import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.ViewEvent
import com.top1shvetsvadim1.coreutils.ViewIntent
import com.top1shvetsvadim1.coreutils.ViewState

data class LoginState(
    val isLoading: Boolean = false,
    val items: List<BaseUIModel> = emptyList(),
    val isLogged: Boolean = false
) : ViewState

sealed interface LoginEvent : ViewEvent {
    object PostLogin : LoginEvent
}

sealed interface LoginIntent : ViewIntent {
    object LoadItems : LoginIntent
    data class PostLogin(val email: String, val password: String) : LoginIntent
    data class PostRegister(val fullName: String, val email: String, val password: String) : LoginIntent
}