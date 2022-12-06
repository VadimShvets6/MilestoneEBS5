package com.top1shvetsvadim1.milestoneebs5

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeActivity
import com.google.firebase.auth.FirebaseAuth
import com.top1shvetsvadim1.coreui.FeatureAuthContract
import com.top1shvetsvadim1.coreui.FeatureMainContract
import com.top1shvetsvadim1.coreui.LightTheme
import com.top1shvetsvadim1.coreui.MyAppTheme
import com.top1shvetsvadim1.coreutils.launchIO
import com.top1shvetsvadim1.data.impl.dataStore
import com.top1shvetsvadim1.data.service.NotificationFirebase
import com.top1shvetsvadim1.domain.models.Parameters
import com.top1shvetsvadim1.feature_main.main.MainFragmentDirections
import com.top1shvetsvadim1.milestoneebs5.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ThemeActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    private var navMainController: NavController? = null

    var isReady = false

    private val auth = FirebaseAuth.getInstance()

    override fun getStartTheme(): AppTheme {
        return LightTheme()
    }

    override fun syncTheme(appTheme: AppTheme) {
        val myAppTheme = appTheme as MyAppTheme
        // set background color
        binding.root.setBackgroundColor(myAppTheme.firstActivityBackgroundColor(this))
    }

    private val pushBroadcastReceiver by lazy {
        PushBroadcastReceiver(this) {
            navMainController?.navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(it))
        }
    }

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.main_navigation_container) as NavHostFragment
    }

    private val currentGraph by lazy {
        navMainController?.navInflater?.inflate(R.navigation.nav_main)
    }

    private val receiver by lazy {
        MyReceiver()
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        ContextCompat.startForegroundService(this, NotificationForeground.newIntent(this))

        val intentFilter = IntentFilter().apply {
            addAction(NotificationFirebase.INTENT_FILTER)
        }


        navMainController = navHostFragment.navController

        registerReceiver(pushBroadcastReceiver, intentFilter)
        registerReceiver(receiver, intentFilter)

        getExtras()
        loadInitialData()
        splashScreenDraw()

        if (auth.currentUser != null) {
            currentGraph?.setStartDestination(com.top1shvetsvadim1.feature_main.R.id.main_navigation)
            Log.d("Navigation", "${currentGraph?.id}")
            currentGraph?.let {
                navMainController?.setGraph(it, bundleOf())
            }
        }
    }

    private fun getExtras() {
        intent.extras?.getString("id")?.let { id ->
            currentGraph?.setStartDestination(com.top1shvetsvadim1.feature_main.R.id.main_navigation)
            currentGraph?.let {
                navMainController?.setGraph(it, bundleOf())
            }
            navMainController?.navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(id.toInt()))
        }

        intent.extras?.getString("Favorites")?.let {
            currentGraph?.setStartDestination(com.top1shvetsvadim1.feature_main.R.id.mainFragment)
            currentGraph?.let {
                navMainController?.setGraph(it, bundleOf())
            }
            navMainController?.navigate(MainFragmentDirections.actionMainFragmentToFavoriteFragment())
        }

        intent.extras?.getString("FireBase")?.let {
            Toast.makeText(this, "FireBase", Toast.LENGTH_LONG).show()
        }

        intent.extras?.getString("MyCart")?.let {
            currentGraph?.setStartDestination(com.top1shvetsvadim1.feature_main.R.id.mainFragment)
            currentGraph?.let {
                navMainController?.setGraph(it, bundleOf())
            }
            navMainController?.navigate(MainFragmentDirections.actionMainFragmentToCartFragment())
        }

    }

    private fun splashScreenDraw() {
        Log.d("Navigation", "Current")
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val context = this
                return if (auth.currentUser != null) {
                    Log.d("Navigation", "Current jjj ")
                    //featureARouteContractImpl.show("as", control)
                    content.viewTreeObserver.removeOnPreDrawListener(context)
                    true
                } else {
                     currentGraph?.setStartDestination(com.top1shvetsvadim1.feature_auth.R.id.auth_nav)
                    Log.d("Navigation", "Current auth ${currentGraph?.startDestDisplayName}")
                    currentGraph?.let {
                        navMainController?.setGraph(it, bundleOf())
                    }
                    content.viewTreeObserver.removeOnPreDrawListener(context)
                    false
                }
            }
        })
    }

    private fun loadInitialData() {
        lifecycleScope.launchIO {
            viewModel.save(Parameters.Text)
            val pref = dataStore.data
            pref.map {
                it[booleanPreferencesKey("isLogged")] ?: false
            }.collectLatest {
                isReady = it
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        unregisterReceiver(pushBroadcastReceiver)
        super.onDestroy()
    }
}

