package com.top1shvetsvadim1.coreui

import android.content.Context
import androidx.core.content.ContextCompat
import com.dolatkia.animatedThemeManager.AppTheme
import com.top1shvetsvadim1.coreui.Colors

interface MyAppTheme : AppTheme {
    fun firstActivityBackgroundColor(context: Context): Int
    fun firstActivityTextColor(context: Context): Int
}

class LightTheme : MyAppTheme {

    override fun id(): Int { // set unique iD for each theme
        return 156464
    }

    override fun firstActivityBackgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, Colors.white)
    }

    override fun firstActivityTextColor(context: Context): Int {
        return ContextCompat.getColor(context, Colors.black)
    }
}

class NightTheme : MyAppTheme {

    override fun id(): Int { // set unique iD for each theme
        return 256565
    }

    override fun firstActivityBackgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.teal_200)
    }

    override fun firstActivityTextColor(context: Context): Int {
        return ContextCompat.getColor(context, Colors.white)
    }
}