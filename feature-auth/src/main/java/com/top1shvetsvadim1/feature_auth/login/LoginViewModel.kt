package com.top1shvetsvadim1.feature_auth.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.top1shvetsvadim1.coreui.*
import com.top1shvetsvadim1.coreutils.*
import com.top1shvetsvadim1.data.impl.dataStore
import com.top1shvetsvadim1.domain.models.LoginResponse
import com.top1shvetsvadim1.domain.models.TextTypes
import com.top1shvetsvadim1.domain.useCase.*
import com.top1shvetsvadim1.feature_auth.ItemButtonUIModel
import com.top1shvetsvadim1.feature_auth.ItemSeparatorUIModel
import com.top1shvetsvadim1.feature_auth.ItemText
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getLoginItemListUseCase: GetBaseUIItemListUseCase,
    private val postLoginUseCase: PostLoginUseCase,
    private val postRegisterUseCase: PostRegisterUseCase
) : BaseViewModel<LoginIntent, LoginState, LoginEvent>() {

    override val reducer: Reducer<LoginState, LoginEvent> = LoginReducer()

    private val firebaseAuth = FirebaseAuth.getInstance()

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

    fun handlerResult(task: Task<GoogleSignInAccount>, context: Context) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnSuccessListener {
            account.email?.let { email ->
                account.idToken?.substring(0, 16)?.let { password ->
                    LoginIntent.PostLogin(email, password)
                }
            }?.let { data ->
                handleAction(data)
            }
            viewModelScope.launchIO {
                context.dataStore.edit {
                    it[stringPreferencesKey(LoginFragment.USER_NAME)] = account.displayName.toString()
                    it[stringPreferencesKey(LoginFragment.PHOTO_USER)] = account.photoUrl.toString()
                    it[stringPreferencesKey(LoginFragment.USER_EMAIL)] = account.email.toString()
                    it[stringPreferencesKey(LoginFragment.USER_PASSWORD)] = account.idToken?.substring(0, 16).toString()
                }
            }
        }
    }

    private fun postLogin(email: String, password: String) {
        postLoginUseCase.collect(summoner = this, params = LoginData(email, password), true)
    }

    private fun loadItems() {
        //TODO: strange Use Case. You should get scenario or necessary info, not UIModels
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

    inner class LoginReducer : Reducer<LoginState, LoginEvent>(LoginState()) {
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