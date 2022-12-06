package com.top1shvetsvadim1.feature_auth.signIn

import com.top1shvetsvadim1.coreutils.BaseViewModel
import com.top1shvetsvadim1.coreutils.Reducer
import com.top1shvetsvadim1.coreutils.collect
import com.top1shvetsvadim1.domain.models.LoginResponse
import com.top1shvetsvadim1.domain.useCase.LoginData
import com.top1shvetsvadim1.domain.useCase.PostLoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val postLoginUseCase: PostLoginUseCase
) : BaseViewModel<SignInIntent, SignInState, SignInEvent>() {

    override val reducer: Reducer<SignInState, SignInEvent> = SignInReducer()

    override fun handleAction(action: SignInIntent) {
        when (action) {
            is SignInIntent.PostLogin -> postLoginUseCase.collect(
                summoner = this,
                LoginData(action.email, action.password),
                true
            )
        }
    }

    inner class SignInReducer : Reducer<SignInState, SignInEvent>(SignInState()) {
        override suspend fun onLoading() {
            sendState(
                currentState.copy(
                    isLoading = true
                )
            )
        }

        override suspend fun onError(error: Exception) {
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