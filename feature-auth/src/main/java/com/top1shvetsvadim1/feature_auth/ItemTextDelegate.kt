package com.top1shvetsvadim1.feature_auth

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
import com.top1shvetsvadim1.coreutils.Action
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.BaseViewHolder
import com.top1shvetsvadim1.coreutils.ItemDelegate
import com.top1shvetsvadim1.domain.models.TextTypes
import com.top1shvetsvadim1.domain.models.setTextType
import com.top1shvetsvadim1.feature_auth.databinding.TextItemBinding

@UIModel
data class ItemText(
    @PrimaryTag val tag: Int,
    @DimenRes val size: Int,
    @FontRes val font: Int,
    @ColorRes val color: Int,
    @DrawableRes val iconStart: Int? = null,
    val text: TextTypes
) : BaseUIModel()

@Instruction(ItemText::class)
@AutoBindViewHolder(ItemTextDelegate.ItemTextViewHolder::class)
class ItemTextInstructor : Instructor

class ItemTextDelegate : ItemDelegate<ItemText, ItemTextDelegate.ItemTextViewHolder>(
    ItemText::class
) {

    private val inspector = Novalles.provideInspectorFromUiModel(ItemText::class)

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemTextDelegate.ItemTextViewHolder {
        return ItemTextViewHolder(TextItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ItemTextViewHolder, item: ItemText, payload: MutableList<Any>) {
        val instructor = ItemTextInstructor()
        inspector.inspectPayloads(payload, instructor, viewHolder = holder) {
            holder.bind(item)
        }
        holder.setOnClickListeners(item)
    }

    inner class ItemTextViewHolder(
        private val binding: TextItemBinding
    ) : BaseViewHolder<ItemText>(binding) {

        override fun bind(item: ItemText) {
            setText(item.text)
            setSize(item.size)
            setColor(item.color)
            setFont(item.font)
            setOnClickListeners(item)
            setIconStart(item.iconStart)
        }

        private val context get() = itemView.context

        fun setSize(@DimenRes size: Int) {
            val textSize = context.resources.getDimension(size)
            binding.informationText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        }

        fun setColor(@ColorRes color: Int) {
            val colorValue = ContextCompat.getColor(context, color)
            binding.informationText.setTextColor(colorValue)
        }

        fun setIconStart(image: Int?) {
            image?.let { binding.iconStart.setImageResource(it) }
        }

        fun setText(text: TextTypes) {
            binding.informationText.setTextType(text)
        }

        fun setFont(fontFamily: Int) {
            val typeface = ResourcesCompat.getFont(context, fontFamily)
            binding.informationText.typeface = typeface
        }

        override fun setOnClickListeners(item: ItemText) {
            binding.root.setOnClickListener {
                pushAction(ItemActionClick.OnItemClicked(item.tag))
            }
            super.setOnClickListeners(item)
        }
    }

    sealed interface ItemActionClick : Action {
        data class OnItemClicked(val id: Int) : ItemActionClick
    }
}