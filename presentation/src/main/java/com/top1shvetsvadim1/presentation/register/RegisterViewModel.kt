package com.top1shvetsvadim1.presentation.register

import com.top1shvetsvadim1.coreutils.BaseViewModel
import com.top1shvetsvadim1.coreutils.Reducer
import com.top1shvetsvadim1.coreutils.run
import com.top1shvetsvadim1.domain.models.LoginResponse
import com.top1shvetsvadim1.domain.useCase.PostRegisterUseCase
import com.top1shvetsvadim1.domain.useCase.RegisterData
import com.top1shvetsvadim1.presentation.mvi.RegisterEvent
import com.top1shvetsvadim1.presentation.mvi.RegisterIntent
import com.top1shvetsvadim1.presentation.mvi.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val postRegisterUseCase: PostRegisterUseCase
) : BaseViewModel<RegisterIntent, RegisterState, RegisterEvent>() {

    override val reducer: Reducer<RegisterState, RegisterEvent> = RegisterReducer()

    override fun handleAction(action: RegisterIntent) {
        when (action) {
            is RegisterIntent.PostRegister -> postRegisterUseCase.run(
                summoner = this,
                RegisterData(action.fullName, action.email, action.password),
                true
            )
        }
    }

    inner class RegisterReducer : Reducer<RegisterState, RegisterEvent>(RegisterState()) {
        override suspend fun onLoading() {
            sendState(
                currentState.copy(
                    isLoading = true
                )
            )
        }

        override suspend fun onError(error: Exception) {
            // TODO error
        }

        override suspend fun handlePayload(payload: Any) {
            when (payload) {
                is LoginResponse -> sendState(
                    currentState.copy(
                        isLogged = true
                    )
                )
            }
        }
    }
}