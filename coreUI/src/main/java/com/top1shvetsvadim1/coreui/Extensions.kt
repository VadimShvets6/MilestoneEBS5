package com.top1shvetsvadim1.coreui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet

typealias Stylable = com.top1shvetsvadim1.coreui.R.styleable
typealias Drawable = com.top1shvetsvadim1.coreui.R.drawable
typealias Colors = com.top1shvetsvadim1.coreui.R.color
typealias Anim = com.top1shvetsvadim1.coreui.R.anim

fun getTypedArray(
    context: Context,
    attributeSet: AttributeSet,
    attr: IntArray
): TypedArray {
    return context.obtainStyledAttributes(attributeSet, attr, 0, 0)
}