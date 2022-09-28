package com.top1shvetsvadim1.presentation.customView

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.top1shvetsvadim1.coreui.Stylable
import com.top1shvetsvadim1.coreui.getTypedArray
import com.top1shvetsvadim1.presentation.databinding.ToolbarBinding

class ToolbarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {


    private val viewBinding =
        ToolbarBinding.inflate(
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
            getTypedArray(context, attrs, Stylable.Toolbar)

        viewBinding.apply {
            leftIcon.setImageResource(
                attr.getResourceId(
                    com.top1shvetsvadim1.coreui.R.styleable.Toolbar_toolbarLeftImage,
                    0
                )
            )
            iconLogo.setImageResource(
                attr.getResourceId(
                    com.top1shvetsvadim1.coreui.R.styleable.Toolbar_toolbarCenterImage,
                    0
                )
            )
            rightIcon.setImageResource(
                attr.getResourceId(
                    com.top1shvetsvadim1.coreui.R.styleable.Toolbar_toolbarRightImage,
                    0
                )
            )

        }
        attr.recycle()
    }

    fun setRightImage(image: Int) {
        viewBinding.rightIcon.setImageResource(image)
    }

    fun setClickOnRightImage(click: () -> Unit) {
        viewBinding.rightIcon.setOnClickListener {
            click()
        }
    }

    fun setClickOnLeftImage(click: () -> Unit) {
        viewBinding.leftIcon.setOnClickListener {
            click()
        }
    }

    fun setActivatedRightImage(click: Boolean) {
        viewBinding.rightIcon.isActivated = click
    }

    fun isRightImageActivated(): Boolean {
        return viewBinding.rightIcon.isActivated
    }
}