package com.top1shvetsvadim1.coreui

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet

//TODO: TYPO!!
typealias Stylable = R.styleable
typealias Drawable = R.drawable
typealias Colors = R.color
typealias Anim = R.anim
typealias Dimen = R.dimen
typealias Font = R.font
typealias Strings = R.string

fun getTypedArray(
    context: Context,
    attributeSet: AttributeSet,
    attr: IntArray
): TypedArray {
    return context.obtainStyledAttributes(attributeSet, attr, 0, 0)
}