package com.top1shvetsvadim1.feature_auth.signIn

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.top1shvetsvadim1.coreui.Colors
import com.top1shvetsvadim1.coreui.FeatureAuthContract
import com.top1shvetsvadim1.coreui.FeatureMainContract
import com.top1shvetsvadim1.coreutils.BaseFragment
import com.top1shvetsvadim1.coreutils.isValidEmail
import com.top1shvetsvadim1.coreutils.launchIO
import com.top1shvetsvadim1.data.impl.dataStore
import com.top1shvetsvadim1.feature_auth.databinding.FragmentSignInBinding
import com.top1shvetsvadim1.feature_auth.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment<SignInState, SignInEvent, SignInViewModel, FragmentSignInBinding>() {

    override val viewModel: SignInViewModel by viewModels()
    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    @Inject lateinit var featureMainContract: FeatureMainContract

    override fun setupViews() {

    }

    private fun signIn(email: String, password: String) {
        if (email.isNotBlank() && password.isNotBlank()) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        viewModel.handleAction(SignInIntent.PostLogin(email, password))
                        lifecycleScope.launchIO {
                            requireContext().dataStore.edit {
                                it[stringPreferencesKey(LoginFragment.USER_EMAIL)] = email
                                it[stringPreferencesKey(LoginFragment.USER_PASSWORD)] = password
                            }
                        }
                    } else {
                        createSnackBar(
                            "Account '$email' not found",
                            Colors.snack_bar_no_internet,
                            Colors.white
                        )
                        binding.email.isEnabled = true
                        binding.password.isEnabled = true
                    }
                }
        }

    }

    override fun render(state: SignInState) {
        binding.progressBar.isVisible = state.isLoading
        resetErrors()
        binding.signUp.setOnClickListener {
            checkError()
            if (binding.email.text.isValidEmail() && !binding.password.text.isNullOrBlank()) {
                binding.email.isEnabled = false
                binding.password.isEnabled = false
                signIn(binding.email.text.toString(), binding.password.text.toString())
            }
        }
        binding.toolbar.setClickOnLeftImage {
            popBack()
        }
        if (state.isLogged) {
            createSnackBar("Successful login", Colors.bg_snack_green, Colors.white)
            featureMainContract.show(findNavController())
            //  navigateTo(SignInFragmentDirections.actionSignInFragmentToMainFragment())
        }
    }

    private fun checkError() {
        if (!binding.email.text.isValidEmail()) {
            binding.emailLayout.error = "Invalid email"
        }
        if (binding.password.text.isNullOrBlank()) {
            binding.passwordLayout.error = "One capital letter, One number, One symbol (@,\$,%,&,#,)"
        }
    }

    private fun resetErrors() {
        binding.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.emailLayout.error = ""
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.passwordLayout.error = ""
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentSignInBinding {
        return FragmentSignInBinding.inflate(inflater)
    }
}