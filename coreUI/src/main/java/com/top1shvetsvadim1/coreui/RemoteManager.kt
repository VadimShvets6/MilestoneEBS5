package com.top1shvetsvadim1.coreui

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.channels.Channel

//TODO: move to app module
object RemoteManager {
    //TODO: insert init function (if necessary)
    //TODO: create fetch function, where you save data to data store, function returns MAP, not values
    val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    suspend fun fetch(): Map<String, FirebaseRemoteConfigValue> {
        val value = Channel<Map<String, FirebaseRemoteConfigValue>>(capacity = Channel.RENDEZVOUS)
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)
            .addOnSuccessListener {
                value.trySend(remoteConfig.all)
                Log.d("Remote", "RemoteAll: ${remoteConfig.all}")
            }
        return value.receive()
    }

    const val KEY_TEXT = "remote_config_test_text"
    const val KEY_BUTTON_COLOR = "button_color_test"

    // fun getButtonText(): String = remoteConfig.getString("remote_config_test_text")
}