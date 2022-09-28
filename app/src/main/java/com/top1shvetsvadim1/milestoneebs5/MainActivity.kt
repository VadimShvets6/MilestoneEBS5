package com.top1shvetsvadim1.milestoneebs5

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.top1shvetsvadim1.coreutils.launchIO
import com.top1shvetsvadim1.data.dataStore
import com.top1shvetsvadim1.data.service.NotificationFirebase
import com.top1shvetsvadim1.domain.models.Parameters
import com.top1shvetsvadim1.milestoneebs5.databinding.ActivityMainBinding
import com.top1shvetsvadim1.presentation.customView.CustomDialogSale
import com.top1shvetsvadim1.presentation.main.MainFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivtyViewModel by viewModels()

    private var navController: NavController? = null
    var isReady = false

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val dialog by lazy {
        CustomDialogSale(this)
    }

    private val pushBroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, intent: Intent?) {
                val extras = intent?.extras
                extras?.keySet()?.firstOrNull { it == "action" }?.let { key ->
                    when (extras.getString(key)) {
                        "show_message" -> {
                            val id = extras.getString("id")
                            val icon = extras.getString("icon")
                            val name = extras.getString("name")
                            val size = extras.getString("size")
                            if (!id.isNullOrBlank() && !icon.isNullOrBlank() && !name.isNullOrBlank() && !size.isNullOrBlank()) {
                                dialog.createDialog(name, size, id.toInt(), icon) {
                                    navController?.navigate(
                                        MainFragmentDirections.actionMainFragmentToDetailFragment(
                                            it
                                        )
                                    )
                                }
                            } else {
                                Log.d("TAG", "Extras null")
                            }
                        }
                        else -> Log.d("TAG", "ERROR")
                    }
                }
            }
        }
    }

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.main_navigation_container) as NavHostFragment
    }

    private val currentGraph by lazy {
        navController?.navInflater?.inflate(com.top1shvetsvadim1.presentation.R.navigation.main_navigation)
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

        registerReceiver(pushBroadcastReceiver, intentFilter)
        registerReceiver(receiver, intentFilter)

        navController = navHostFragment.navController

        getExtras()
        loadInitialData()
        splashScreenDraw()

        if (auth.currentUser != null) {
            currentGraph?.setStartDestination(com.top1shvetsvadim1.presentation.R.id.mainFragment)
            currentGraph?.let {
                navController?.setGraph(it, bundleOf())
            }
        }
    }

    private fun getExtras() {
        intent.extras?.getString("id")?.let { id ->
            currentGraph?.setStartDestination(com.top1shvetsvadim1.presentation.R.id.mainFragment)
            currentGraph?.let {
                navController?.setGraph(it, bundleOf())
            }
            navController?.navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(id.toInt()))
        }

        intent.extras?.getString("Favorites")?.let {
            currentGraph?.setStartDestination(com.top1shvetsvadim1.presentation.R.id.mainFragment)
            currentGraph?.let {
                navController?.setGraph(it, bundleOf())
            }
            navController?.navigate(MainFragmentDirections.actionMainFragmentToFavoriteFragment())
        }

        intent.extras?.getString("FireBase")?.let {
            Toast.makeText(this, "FireBase", Toast.LENGTH_LONG).show()
        }

        intent.extras?.getString("MyCart")?.let {
            currentGraph?.setStartDestination(com.top1shvetsvadim1.presentation.R.id.mainFragment)
            currentGraph?.let {
                navController?.setGraph(it, bundleOf())
            }
            navController?.navigate(MainFragmentDirections.actionMainFragmentToCartFragment())
        }

    }

    private fun splashScreenDraw() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val context = this
                return if (auth.currentUser != null) {
                    content.viewTreeObserver.removeOnPreDrawListener(context)
                    true
                } else {
                    currentGraph?.setStartDestination(com.top1shvetsvadim1.presentation.R.id.loginFragment)
                    currentGraph?.let {
                        navController?.setGraph(it, bundleOf())
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

