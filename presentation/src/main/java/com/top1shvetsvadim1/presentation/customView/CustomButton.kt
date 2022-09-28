package com.top1shvetsvadim1.presentation.customView

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.top1shvetsvadim1.coreui.Drawable
import com.top1shvetsvadim1.coreui.Stylable
import com.top1shvetsvadim1.coreui.getTypedArray
import com.top1shvetsvadim1.presentation.databinding.CustomButtonBinding

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
                //TODO: wrong default value. If I do not put any value in left image, there should be no image.
                attr.getResourceId(
                    Stylable.CustomButton_leftImage,
                    Drawable.ic_buschet
                )
            )
            middleText.text = attr.getString(Stylable.CustomButton_centerText)
            //TODO: right image - string?
            cartText.text = attr.getString(Stylable.CustomButton_rightImage)

        }
        attr.recycle()
    }
}