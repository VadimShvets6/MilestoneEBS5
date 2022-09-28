package com.top1shvetsvadim1.milestoneebs5

import android.app.Application
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            val token = task.result
            Log.d("Token", token)
        }
    }
}

//TODO: reduce time of screen-to-screen transitions
//TODO: block input fields while user logging or signing in
//TODO: pagination on main screen
//TODO: scroll to top on filter applying
//TODO: cart screen - shadows of FABs are cut
//TODO: cart screen - when content is not scrollable enough cant see summary again
//TODO: remove +/- buttons or add functionality
//TODO: add sort options to cart and favourites
//TODO: cart button in feed - counter show wrong data
//TODO: profile picture - add placeholder
//TODO: profile navigation is without animation
//TODO: grid-list switch is not working
//TODO: details - buy now button should navigate to cart + add to cart (navigate with pop inclusive to feed)