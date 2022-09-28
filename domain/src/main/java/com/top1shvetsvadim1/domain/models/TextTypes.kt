package com.top1shvetsvadim1.domain.models

import android.widget.TextView
import androidx.annotation.StringRes

//TODO TEST(ItemText)
sealed class TextTypes {
    data class ResText(@StringRes val text: Int) : TextTypes()

    data class StringText(val text: String) : TextTypes()
}

fun TextView.setTextType(resource: TextTypes) {
    when (resource) {
        is TextTypes.ResText -> {
            this.text = context.resources.getString(resource.text)
        }
        is TextTypes.StringText -> {
            this.text = resource.text
        }
    }
}