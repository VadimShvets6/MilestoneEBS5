package com.top1shvetsvadim1.presentation.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.top1shvetsvadim1.coreutils.launchIO
import com.top1shvetsvadim1.data.dataStore
import com.top1shvetsvadim1.presentation.databinding.FragmentProfileBinding
import com.top1shvetsvadim1.presentation.login.LoginFragment.Companion.PHOTO_USER
import com.top1shvetsvadim1.presentation.login.LoginFragment.Companion.USER_EMAIL
import com.top1shvetsvadim1.presentation.login.LoginFragment.Companion.USER_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentProfileBinding == null")

    private val preferences by lazy {
        requireContext().dataStore.data
    }

    private val name by lazy {
        preferences.map {
            it[stringPreferencesKey(USER_NAME)] ?: "None"
        }
    }

    private val email by lazy {
        preferences.map {
            it[stringPreferencesKey(USER_EMAIL)] ?: "None"
        }
    }
    private val photo by lazy {
        preferences.map {
            it[stringPreferencesKey(PHOTO_USER)] ?: "None"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch(Dispatchers.Main) {
            binding.userName.text = name.first()
            binding.photoUser.load(photo.first())
            binding.userEmail.text = email.first()
        }
        binding.toolbar.setClickOnLeftImage {
            findNavController().popBackStack()
        }
        binding.logOut.setOnClickListener {
            Log.d("Login", "${auth.currentUser?.email}")
            lifecycleScope.launchIO {
                requireContext().dataStore.edit {
                    it[booleanPreferencesKey("isLogged")] = false
                }
            }
            auth.signOut()
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToLoginFragment())
        }
    }
}