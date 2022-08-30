package com.top1shvetsvadim1.presentation.customView

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.top1shvetsvadim1.coreui.Stylable
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
            //TODO: check case: if there are no value for field, does it work correct.
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
        //TODO: typed array is not recycled
    }

    //TODO: code formatting
    fun setRightImage(image : Int){
        viewBinding.rightIcon.setImageResource(image)
    }

    fun setClickOnLeftImage(click: () -> Unit) {
        viewBinding.leftIcon.setOnClickListener {
            click()
        }
    }

    fun setClickOnRightImage(click: () -> Unit){
        viewBinding.rightIcon.setOnClickListener {
            click()
        }
    }

    fun setActivatedRightImage(click: Boolean){
        viewBinding.rightIcon.isActivated = click
    }

    fun isRightImageActivated() : Boolean{
        return viewBinding.rightIcon.isActivated
    }

    //TODO: this function can be moved to core UI
    private fun getTypedArray(
        context: Context,
        attributeSet: AttributeSet,
        attr: IntArray
    ): TypedArray {
        return context.obtainStyledAttributes(attributeSet, attr, 0, 0)
    }
}