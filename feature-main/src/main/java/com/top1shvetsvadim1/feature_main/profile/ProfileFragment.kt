package com.top1shvetsvadim1.feature_main.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeManager
import com.google.firebase.auth.FirebaseAuth
import com.top1shvetsvadim1.coreui.*
import com.top1shvetsvadim1.coreutils.BaseThemeFragment
import com.top1shvetsvadim1.coreutils.launchIO
import com.top1shvetsvadim1.data.impl.dataStore
import com.top1shvetsvadim1.feature_main.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseThemeFragment<FragmentProfileBinding>() {

    override fun getViewBinding(inflater: LayoutInflater): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater)
    }


    @Inject
    lateinit var featureAuthContract: FeatureAuthContract

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
            it[stringPreferencesKey(PHOTO_USER)] ?: Drawable.icon_profile
        }
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun syncTheme(appTheme: AppTheme) {
        val myAppTheme = appTheme as MyAppTheme
        // set background color
        binding.root.setBackgroundColor(myAppTheme.firstActivityBackgroundColor(requireContext()))

        //set text color
        binding.userName.setTextColor(myAppTheme.firstActivityTextColor(requireContext()))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.darck.setOnClickListener {
            ThemeManager.instance.changeTheme(NightTheme(), it, 300)
        }
        binding.light.setOnClickListener {
            ThemeManager.instance.reverseChangeTheme(LightTheme(), it, 300)
        }
        binding.toolbar.setClickOnLeftImage {
            findNavController().popBackStack()
        }
        lifecycleScope.launch(Dispatchers.Main) {
            binding.userName.text = name.first()
            binding.photoUser.load(photo.first()) {
                allowHardware(false)
            }
            binding.userEmail.text = email.first()
        }
        binding.logOut.setOnClickListener {
            Log.d("Login", "${auth.currentUser?.email}")
            lifecycleScope.launchIO {
                requireContext().dataStore.edit {
                    it[booleanPreferencesKey("isLogged")] = false
                }
            }
            auth.signOut()
            featureAuthContract.show(findNavController())
        }
    }

    companion object {
        const val USER_NAME = "userName"
        const val PHOTO_USER = "photoUser"
        const val USER_EMAIL = "userEmail"
        const val USER_PASSWORD = "userPassword"
    }
}

