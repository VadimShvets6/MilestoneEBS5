package com.top1shvetsvadim1.presentation.register

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.top1shvetsvadim1.coreui.Colors
import com.top1shvetsvadim1.coreutils.*
import com.top1shvetsvadim1.data.dataStore
import com.top1shvetsvadim1.presentation.customView.CustomDialog
import com.top1shvetsvadim1.presentation.databinding.FragmentRegisterBinding
import com.top1shvetsvadim1.presentation.login.LoginFragment
import com.top1shvetsvadim1.presentation.mvi.RegisterEvent
import com.top1shvetsvadim1.presentation.mvi.RegisterIntent
import com.top1shvetsvadim1.presentation.mvi.RegisterState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<RegisterState, RegisterEvent, RegisterViewModel, FragmentRegisterBinding>() {

    override val viewModel: RegisterViewModel by viewModels()

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val dialog by lazy {
        CustomDialog(requireContext())
    }

    override fun setupViews() {
        binding.passwordInfo.setOnClickListener {
            dialog.showDialog()
        }
    }

    override fun render(state: RegisterState) {
        binding.progressBar.isVisible = state.isLoading
        resetErrors()
        binding.signUp.setOnClickListener {
            checkError()
            if (binding.email.text.isValidEmail() && binding.password.text.isMediumPassword()) {
                registerAccount(
                    binding.fullName.text.toString(),
                    binding.email.text.toString(),
                    binding.password.text.toString()
                )
            }
        }
        binding.toolbar.setClickOnLeftImage {
            popBack()
        }
        if (state.isLogged) {
            navigateTo(RegisterFragmentDirections.actionRegisterFragmentToMainFragment())
        }
    }

    private fun registerAccount(fullName: String, email: String, password: String) {
        if (fullName.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        viewModel.handleAction(RegisterIntent.PostRegister(fullName, email, password))
                        lifecycleScope.launchIO {
                            requireContext().dataStore.edit {
                                it[stringPreferencesKey(LoginFragment.USER_NAME)] = fullName
                                it[stringPreferencesKey(LoginFragment.USER_EMAIL)] = email
                                it[stringPreferencesKey(LoginFragment.USER_PASSWORD)] = password
                            }
                        }
                    } else {
                        createSnackBar(
                            "Email account '$email' already exists",
                            Colors.snack_bar_no_internet,
                            Colors.white
                        )
                    }
                }
        }
    }

    private fun checkError() {
        if (!binding.email.text.isValidEmail()) {
            binding.emailLayout.error = "Invalid email"
        }
        if (!binding.fullName.text.isValidFullName()) {
            binding.fullNameLayout.error = "Full name must be at least 2 words"
        }
        if (!binding.password.text.isMediumPassword() || binding.password.text.isNullOrBlank()) {
            binding.passwrodLayout.error = "One capital letter, One number, One symbol (@,\$,%,&,#,)"
        }
    }

    private fun resetErrors() {
        binding.fullName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.fullNameLayout.error = ""
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
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
                binding.passwrodLayout.error = ""
                binding.linear.isVisible = true
                if (p0.isNullOrBlank()) {
                    binding.linear.isVisible = false
                }
                if (p0.isLowPassword()) {
                    binding.levelPassword.apply {
                        progress = 30
                        progressDrawable.setTint(ContextCompat.getColor(requireContext(), Colors.snack_bar_no_internet))
                    }
                }
                if (p0.isMediumPassword()) {
                    binding.levelPassword.apply {
                        progress = 50
                        progressDrawable.setTint(ContextCompat.getColor(requireContext(), Colors.yellow_FFC107))
                    }
                }
                if (p0.isHardPassword()) {
                    binding.levelPassword.apply {
                        progress = 80
                        progressDrawable.setTint(ContextCompat.getColor(requireContext(), Colors.bg_snack_green))
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater)
    }
}