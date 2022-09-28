package com.top1shvetsvadim1.data.service

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationFirebase : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent = Intent(INTENT_FILTER)
        message.data.forEach {
            intent.putExtra(it.key, it.value)
        }
        sendBroadcast(intent)
    }

    companion object {
        const val INTENT_FILTER = "PUSH_EVENT"
    }
}