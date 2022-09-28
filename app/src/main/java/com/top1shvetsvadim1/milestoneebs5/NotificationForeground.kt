package com.top1shvetsvadim1.milestoneebs5

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.top1shvetsvadim1.coreui.Drawable
import com.top1shvetsvadim1.milestoneebs5.MyReceiver.Companion.ACTION_FAVROITE
import com.top1shvetsvadim1.milestoneebs5.MyReceiver.Companion.ACTION_MY_CART


class NotificationForeground : Service() {

    //TODO: unnecessary lazy!!
    private val remoteViews by lazy {
        RemoteViews(packageName, com.top1shvetsvadim1.coreui.R.layout.notification_custom)
    }

    private val mainIntent by lazy {
        Intent(this, MainActivity::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    private val mainPIntent by lazy {
        PendingIntent.getActivity(
            this,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }


    private val favoriteIntent by lazy {
        Intent(this, MyReceiver::class.java).apply {
            action = ACTION_FAVROITE
            flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("Favorites", "")
        }
    }

    private val cartIntent by lazy {
        Intent(this, MyReceiver::class.java).apply {
            action = ACTION_MY_CART
            flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("MyCart", "")
        }
    }

    private val cartPIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            1,
            cartIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private val favoritePIntent by lazy {
        PendingIntent.getBroadcast(
            this,
            1,
            favoriteIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
    //TODO: remove lazy there and ask me for rules to create lazy objects

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(2, createNotification())
        return START_NOT_STICKY
    }

    private fun createNotification() = NotificationCompat.Builder(this, "channel_id")
        .setSmallIcon(Drawable.ic_favorite_main)
        .setCustomContentView(remoteViews)
        .setContentIntent(mainPIntent)
        .setStyle(NotificationCompat.DecoratedCustomViewStyle()).apply {
            remoteViews.setOnClickPendingIntent(com.top1shvetsvadim1.coreui.R.id.favorite, favoritePIntent)
            remoteViews.setOnClickPendingIntent(com.top1shvetsvadim1.coreui.R.id.myCart, cartPIntent)
        }
        .setOnlyAlertOnce(true)
        .setAutoCancel(true)
        .build()

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                "channel_id",
                "channel_name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        } else {
            TODO()
        }
        notificationManager.createNotificationChannel(notificationChannel)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, NotificationForeground::class.java)
        }
    }
}

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        val intent = Intent(p0, MainActivity::class.java).apply {
            flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        when (p1?.action) {
            ACTION_FAVROITE -> {
                intent.putExtra("Favorites", "").apply {
                    flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                p0?.startActivity(intent)
            }
            ACTION_MY_CART -> {
                Toast.makeText(p0, "CART", Toast.LENGTH_LONG).show()
                intent.putExtra("MyCart", "").apply {
                    flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                p0?.startActivity(intent)
            }
        }
    }

    companion object {
        const val ACTION_FAVROITE = "Favorites"
        const val ACTION_MY_CART = "MyCart"

    }
}