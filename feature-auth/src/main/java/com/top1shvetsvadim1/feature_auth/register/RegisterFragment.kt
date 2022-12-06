package com.top1shvetsvadim1.feature_auth.register

import android.net.Uri
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.top1shvetsvadim1.coreui.Colors
import com.top1shvetsvadim1.coreutils.*
import com.top1shvetsvadim1.data.impl.dataStore
import com.top1shvetsvadim1.feature_auth.databinding.FragmentRegisterBinding
import com.top1shvetsvadim1.feature_auth.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<RegisterState, RegisterEvent, RegisterViewModel, FragmentRegisterBinding>() {

    override val viewModel: RegisterViewModel by viewModels()

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

//    private val dialog by lazy {
//        CustomDialog(requireContext())
//    }

    override fun setupViews() {
//        binding.passwordInfo.setOnClickListener {
//            dialog.showDialog()
//        }
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
//        binding.toolbar.setClickOnLeftImage {
//            popBack()
//        }
        if (state.isLogged) {
            val uri = Uri.parse("myApp://mainFragment")
            findNavController().navigate(uri)
        }
    }

    //TODO: same as in login
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
                        binding.fullName.isEnabled = false
                        binding.email.isEnabled = false
                        binding.password.isEnabled = false
                    } else {
                        createSnackBar(
                            "Email account '$email' already exists",
                            Colors.snack_bar_no_internet,
                            Colors.white
                        )
                        binding.fullName.isEnabled = true
                        binding.email.isEnabled = true
                        binding.password.isEnabled = true
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
        binding.fullName.doAfterTextChanged {
            binding.fullNameLayout.error = ""
        }
        binding.email.doAfterTextChanged {
            binding.emailLayout.error = ""
        }
        binding.password.doAfterTextChanged {
            binding.passwrodLayout.error = ""
            binding.linear.isVisible = true
            if (it.isNullOrBlank()) {
                binding.linear.isVisible = false
            }
            if (it.isLowPassword()) {
                binding.levelPassword.apply {
                    progress = 30
                    progressDrawable.setTint(ContextCompat.getColor(requireContext(), Colors.snack_bar_no_internet))
                }
            }
            if (it.isMediumPassword()) {
                binding.levelPassword.apply {
                    progress = 50
                    progressDrawable.setTint(ContextCompat.getColor(requireContext(), Colors.yellow_FFC107))
                }
            }
            if (it.isHardPassword()) {
                binding.levelPassword.apply {
                    progress = 80
                    progressDrawable.setTint(ContextCompat.getColor(requireContext(), Colors.bg_snack_green))
                }
            }
        }
    }

    override fun getViewBinding(inflater: LayoutInflater): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater)
    }
}