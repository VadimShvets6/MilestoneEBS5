package com.top1shvetsvadim1.presentation.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.top1shvetsvadim1.coreui.Colors
import com.top1shvetsvadim1.coreui.Strings
import com.top1shvetsvadim1.coreutils.Action
import com.top1shvetsvadim1.coreutils.BaseAdapter
import com.top1shvetsvadim1.coreutils.BaseFragment
import com.top1shvetsvadim1.coreutils.launchIO
import com.top1shvetsvadim1.data.dataStore
import com.top1shvetsvadim1.presentation.databinding.FragmentLoginBinding
import com.top1shvetsvadim1.presentation.delegate.ItemButtonDelegate
import com.top1shvetsvadim1.presentation.delegate.ItemSeparatorDelegate
import com.top1shvetsvadim1.presentation.delegate.ItemTextDelegate
import com.top1shvetsvadim1.presentation.mvi.LoginEvent
import com.top1shvetsvadim1.presentation.mvi.LoginIntent
import com.top1shvetsvadim1.presentation.mvi.LoginState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginState, LoginEvent, LoginViewModel, FragmentLoginBinding>() {

    override val viewModel: LoginViewModel by viewModels()

    private val loginAdapter by BaseAdapter.Builder()
        .setDelegates(ItemTextDelegate(), ItemButtonDelegate(), ItemSeparatorDelegate())
        .setActionProcessor(::onLoginClick)
        .buildIn()

    private val preferences by lazy {
        requireContext().dataStore.data
    }

    private val gso by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(Strings.client_id))
            .requestEmail()
            .build()
    }

    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val mGoogleSignInClient by lazy {
        GoogleSignIn.getClient(requireActivity(), gso)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d("Login", "Req: ${result.resultCode}")
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handlerResult(task)
        }
    }

    private fun handlerResult(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(requireContext(), "Error", Toast.LENGTH_LONG).show()
        }
    }

    override fun handleEffect(effect: LoginEvent) {
        when (effect) {
            LoginEvent.PostLogin -> {
                lifecycleScope.launchIO {
                    val email = preferences.map {
                        it[stringPreferencesKey(USER_EMAIL)]
                    }.first() ?: "None"
                    val password = preferences.map {
                        it[stringPreferencesKey(USER_PASSWORD)]
                    }.first() ?: ""
                    val fullName = preferences.map {
                        it[stringPreferencesKey(USER_NAME)]
                    }.first() ?: "None"
                    viewModel.handleAction(LoginIntent.PostRegister(fullName, email, password))
                }
            }
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
                viewModel.handleAction(data)
            }
            lifecycleScope.launchIO {
                requireContext().dataStore.edit {
                    it[stringPreferencesKey(USER_NAME)] = account.displayName.toString()
                    it[stringPreferencesKey(PHOTO_USER)] = account.photoUrl.toString()
                    it[stringPreferencesKey(USER_EMAIL)] = account.email.toString()
                    it[stringPreferencesKey(USER_PASSWORD)] = account.idToken?.substring(0, 16).toString()
                }
            }
        }
    }

    private fun signIn() {
        val intent = mGoogleSignInClient.signInIntent
        launcher.launch(intent)
    }

    private fun onLoginClick(action: Action) {
        when (action) {
            is ItemButtonDelegate.ActionOnClick.OnItemClicked -> {
                when (action.id) {
                    3 -> {
                        signIn()
                    }
                    5 -> {
                        navigateTo(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
                    }
                }
            }
        }
    }

    override fun setupViews() {
        binding.rvListLogin.adapter = loginAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.handleAction(LoginIntent.LoadItems)
    }

    override fun render(state: LoginState) {
        binding.progressBar.isVisible = state.isLoading
        lifecycleScope.launchWhenResumed {
            loginAdapter.submitList(state.items)
        }
        if (state.isLogged) {
            lifecycleScope.launch(Dispatchers.Main) {
                requireContext().dataStore.edit {
                    navigateTo(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                    createSnackBar("Successful login", Colors.bg_snack_green, Colors.white)
                }
            }
        }
        binding.haveAccount.setOnClickListener {
            navigateTo(LoginFragmentDirections.actionLoginFragmentToSignInFragment())
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater)
    }

    companion object {
        const val USER_NAME = "userName"
        const val PHOTO_USER = "photoUser"
        const val USER_EMAIL = "userEmail"
        const val USER_PASSWORD = "userPassword"
    }
}