package com.top1shvetsvadim1.presentation.login

import android.util.Log
import com.top1shvetsvadim1.coreui.*
import com.top1shvetsvadim1.coreutils.*
import com.top1shvetsvadim1.domain.models.LoginResponse
import com.top1shvetsvadim1.domain.models.TextTypes
import com.top1shvetsvadim1.domain.useCase.*
import com.top1shvetsvadim1.presentation.delegate.ItemButtonUIModel
import com.top1shvetsvadim1.presentation.delegate.ItemSeparatorUIModel
import com.top1shvetsvadim1.presentation.delegate.ItemText
import com.top1shvetsvadim1.presentation.mvi.LoginEvent
import com.top1shvetsvadim1.presentation.mvi.LoginIntent
import com.top1shvetsvadim1.presentation.mvi.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getLoginItemListUseCase: GetBaseUIItemListUseCase,
    private val postLoginUseCase: PostLoginUseCase,
    private val postRegisterUseCase: PostRegisterUseCase
) : BaseViewModel<LoginIntent, LoginState, LoginEvent>() {

    override val reducer: Reducer<LoginState, LoginEvent> = LoginReducer()

    override fun handleAction(action: LoginIntent) {
        when (action) {
            LoginIntent.LoadItems -> loadItems()
            is LoginIntent.PostLogin -> postLogin(action.email, action.password)
            is LoginIntent.PostRegister -> postRegisterUseCase.run(
                summoner = this,
                params = RegisterData(action.fullName, action.email, action.password),
                true
            )
        }
    }

    private fun postLogin(email: String, password: String) {
        postLoginUseCase.collect(summoner = this, params = LoginData(email, password), true)
    }

    private fun loadItems() {
        getLoginItemListUseCase.run(summoner = this) {
            it.map {
                mutableListOf<BaseUIModel>().apply {
                    add(
                        ItemText(
                            tag = 1,
                            size = Dimen.text_size_28_ssp,
                            font = Font.open_sans_extrabold,
                            color = Colors.color_main_07195C,
                            text = TextTypes.ResText(Strings.welcome_to_wow)
                        )
                    )
                    add(
                        ItemText(
                            tag = 2,
                            size = Dimen.text_size_14_ssp,
                            font = Font.open_sans_regular,
                            color = Colors.text_color_252C32,
                            text = TextTypes.ResText(Strings.login_to_begin)
                        )
                    )
                    add(
                        ItemButtonUIModel(
                            tag = 3,
                            text = TextTypes.ResText(Strings.login_with_google),
                            textSize = Dimen.text_size_small_11_ssp,
                            textColor = Colors.color_main_07195C,
                            textFont = Font.open_sans_extrabold,
                            background = Colors.bg_button_E5EBE9,
                            iconStart = Drawable.ic_google
                        )
                    )
                    add(
                        ItemSeparatorUIModel(
                            tag = 4,
                            text = TextTypes.ResText(Strings.login_or),
                            textSize = Dimen.text_size_small_11_ssp,
                            textColor = Colors.text_description_424A56,
                            textFont = Font.open_sans_regular
                        )
                    )
                    add(
                        ItemButtonUIModel(
                            tag = 5,
                            text = TextTypes.ResText(Strings.login_create_new_acc),
                            textSize = Dimen.text_size_small_11_ssp,
                            textColor = Colors.color_main_07195C,
                            textFont = Font.open_sans_extrabold,
                            background = Colors.bg_button_E5EBE9,
                            iconStart = Drawable.ic_add_user
                        )
                    )
                }
            }.let { flow ->
                BaseUIResponse(flow)
            }
        }
    }

    inner class LoginReducer : Reducer<LoginState, LoginEvent>(LoginState(true)) {
        override suspend fun onLoading() {
            sendState(
                currentState.copy(
                    isLoading = true
                )
            )
        }

        override suspend fun onError(error: Exception) {
            when (error) {
                is HttpException -> {
                    when (error.code()) {
                        400 -> pushEvent(LoginEvent.PostLogin)
                    }
                }
            }
        }

        override suspend fun handlePayload(payload: Any) {
            Log.d("Login", "Payload: $payload")
            when (payload) {
                is BaseUIResponse -> sendState(
                    currentState.copy(
                        items = payload.list.first(),
                        isLoading = false,
                    )
                )
                is LoginResponse -> sendState(
                    currentState.copy(
                        isLogged = true
                    )
                )
            }
        }
    }
}