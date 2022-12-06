package com.top1shvetsvadim1.milestoneebs5

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.top1shvetsvadim1.feature_main.customView.CustomDialogSale

class PushBroadcastReceiver(
     val context: Context,
    val navigateTo: (Int) -> Unit
) : BroadcastReceiver() {

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
                       CustomDialogSale(context,name, size, id.toInt(), icon){
                            navigateTo(id.toInt())
                        }.createDialog()
                    } else {
                        Log.d("TAG", "Extras null")
                    }
                }
                else -> Log.d("TAG", "ERROR")
            }
        }
    }
}