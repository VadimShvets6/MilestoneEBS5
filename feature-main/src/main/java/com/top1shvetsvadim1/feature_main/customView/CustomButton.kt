package com.top1shvetsvadim1.feature_main.customView

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.top1shvetsvadim1.coreui.Drawable
import com.top1shvetsvadim1.coreui.Stylable
import com.top1shvetsvadim1.coreui.getTypedArray
import com.top1shvetsvadim1.feature_main.databinding.CustomButtonBinding

class CustomButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val viewBinding = CustomButtonBinding.inflate(LayoutInflater.from(context), this, true)


    init {
        attrs?.let {
            initAttributes(context, attrs)
        }
    }

    private fun initAttributes(context: Context, attrs: AttributeSet) {
        val attr: TypedArray = getTypedArray(context, attrs, Stylable.CustomButton)

        viewBinding.apply {
            cartButtonInBottomBar.setImageResource(
                attr.getResourceId(
                    Stylable.CustomButton_leftImage,
                    Drawable.ic_buschet
                )
            )
            middleText.text = attr.getString(Stylable.CustomButton_centerText)
            cartTextSize.text = attr.getString(Stylable.CustomButton_rightImage)

        }
        attr.recycle()
    }
    fun setRightImage(size: Int) {
        viewBinding.cartTextSize.text = size.toString()
    }
}