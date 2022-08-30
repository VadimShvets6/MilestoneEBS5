package com.top1shvetsvadim1.presentation.customView

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.top1shvetsvadim1.presentation.R
import com.top1shvetsvadim1.presentation.databinding.CustomButtonBinding

class CustomButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val viewBinding =
    CustomButtonBinding.inflate(
    LayoutInflater.from(context), this,
    true
    )

    init {
        attrs?.let {
            initAttributes(context, attrs)
        }
    }

    private fun initAttributes(context: Context, attrs: AttributeSet) {
        val attr: TypedArray =
            getTypedArray(context, attrs, com.top1shvetsvadim1.coreui.R.styleable.CustomButton)

        viewBinding.apply {
            cartButtonInBottomBar.setImageResource(
                attr.getResourceId(
                    com.top1shvetsvadim1.coreui.R.styleable.CustomButton_leftImage,
                    com.top1shvetsvadim1.coreui.R.drawable.ic_buschet
                )
            )
            middleText.text = attr.getString(com.top1shvetsvadim1.coreui.R.styleable.CustomButton_centerText)
            cartText.text = attr.getString(com.top1shvetsvadim1.coreui.R.styleable.CustomButton_rightImage)

        }
    }

    private fun getTypedArray(
        context: Context,
        attributeSet: AttributeSet,
        attr: IntArray
    ): TypedArray {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0)
    }
}