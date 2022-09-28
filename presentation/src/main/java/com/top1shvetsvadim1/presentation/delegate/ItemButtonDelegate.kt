package com.top1shvetsvadim1.presentation.delegate

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.flexeiprata.novalles.annotations.AutoBindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.flexeiprata.novalles.interfaces.Novalles
import com.top1shvetsvadim1.coreui.Drawable
import com.top1shvetsvadim1.coreutils.Action
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.BaseViewHolder
import com.top1shvetsvadim1.coreutils.ItemDelegate
import com.top1shvetsvadim1.domain.models.TextTypes
import com.top1shvetsvadim1.domain.models.setTextType
import com.top1shvetsvadim1.presentation.databinding.ItemButtonBinding


@UIModel
data class ItemButtonUIModel(
    @PrimaryTag val tag: Int,
    val text: TextTypes,
    @DimenRes val textSize: Int,
    @ColorRes val textColor: Int,
    @FontRes val textFont: Int,
    @ColorRes val background: Int,
    @DrawableRes val iconStart: Int? = null,
) : BaseUIModel()

@Instruction(ItemButtonUIModel::class)
@AutoBindViewHolder(ItemButtonDelegate.ItemButtonViewHolder::class)
class ItemButtonInstructor : Instructor

class ItemButtonDelegate : ItemDelegate<ItemButtonUIModel, ItemButtonDelegate.ItemButtonViewHolder>(
    ItemButtonUIModel::class
) {

    private val inspector = Novalles.provideInspectorFromUiModel(ItemButtonUIModel::class)

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemButtonViewHolder {
        return ItemButtonViewHolder(ItemButtonBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ItemButtonViewHolder, item: ItemButtonUIModel, payload: MutableList<Any>) {
        val instructor = ItemButtonInstructor()
        inspector.inspectPayloads(payload, instructor, viewHolder = holder) {
            holder.bind(item)
        }
        holder.setOnClickListeners(item)
    }

    //TODO: format
    inner class ItemButtonViewHolder(private val binding: ItemButtonBinding) :
        BaseViewHolder<ItemButtonUIModel>(binding) {
        private val context get() = itemView.context

        override fun bind(item: ItemButtonUIModel) {
            setTextSize(item.textSize)
            setTextColor(item.textColor)
            setTextFont(item.textFont)
            setText(item.text)
            setIconStart(item.iconStart)
            setBackground(item.background)
            setClickListener(item.tag)
        }

        fun setTextSize(@DimenRes size: Int) {
            val textSize = context.resources.getDimension(size)
            binding.buttonText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        }

        fun setTextColor(@ColorRes color: Int) {
            val isColor = ContextCompat.getColor(context, color)
            binding.buttonText.setTextColor(isColor)
        }

        fun setTextFont(@FontRes font: Int) {
            val isFont = ResourcesCompat.getFont(context, font)
            binding.buttonText.typeface = isFont
        }

        fun setText(text: TextTypes) {
            binding.buttonText.setTextType(text)
        }

        fun setIconStart(image: Int?) {
            binding.iconStart.setImageResource(image ?: Drawable.ic_favorites)
        }

        fun setBackground(@ColorRes background: Int) {
            val isColor = ContextCompat.getColor(context, background)
            binding.itemButton.setBackgroundColor(isColor)
        }

        fun setClickListener(itemId: Int) {
            binding.itemButton.setOnClickListener {
                pushAction(ActionOnClick.OnItemClicked(itemId))
            }
        }
    }

    sealed interface ActionOnClick : Action {
        data class OnItemClicked(val id: Int) : ActionOnClick
    }
}