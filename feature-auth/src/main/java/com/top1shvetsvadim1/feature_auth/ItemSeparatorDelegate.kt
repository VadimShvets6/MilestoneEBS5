package com.top1shvetsvadim1.feature_auth

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.FontRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.flexeiprata.novalles.annotations.AutoBindViewHolder
import com.flexeiprata.novalles.annotations.Instruction
import com.flexeiprata.novalles.annotations.PrimaryTag
import com.flexeiprata.novalles.annotations.UIModel
import com.flexeiprata.novalles.interfaces.Instructor
import com.flexeiprata.novalles.interfaces.Novalles
import com.top1shvetsvadim1.coreutils.BaseUIModel
import com.top1shvetsvadim1.coreutils.BaseViewHolder
import com.top1shvetsvadim1.coreutils.ItemDelegate
import com.top1shvetsvadim1.domain.models.TextTypes
import com.top1shvetsvadim1.domain.models.setTextType
import com.top1shvetsvadim1.feature_auth.databinding.ItemSeparatorBinding

@UIModel
data class ItemSeparatorUIModel(
    @PrimaryTag val tag: Int,
    val text: TextTypes,
    @DimenRes val textSize: Int,
    @ColorRes val textColor: Int,
    @FontRes val textFont: Int,
) : BaseUIModel()

@Instruction(ItemSeparatorUIModel::class)
@AutoBindViewHolder(ItemSeparatorDelegate.ItemSeparatorViewHolder::class)
class ItemSeparatorInstructor : Instructor

class ItemSeparatorDelegate : ItemDelegate<ItemSeparatorUIModel, ItemSeparatorDelegate.ItemSeparatorViewHolder>(
    ItemSeparatorUIModel::class
) {

    private val inspector = Novalles.provideInspectorFromUiModel(ItemSeparatorUIModel::class)

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ItemSeparatorViewHolder {
        return ItemSeparatorViewHolder(ItemSeparatorBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: ItemSeparatorViewHolder,
        item: ItemSeparatorUIModel,
        payload: MutableList<Any>
    ) {
        val instructor = ItemSeparatorInstructor()
        inspector.inspectPayloads(payload, instructor, viewHolder = holder) {
            holder.bind(item)
        }
    }

    inner class ItemSeparatorViewHolder(
        private val binding: ItemSeparatorBinding
    ) : BaseViewHolder<ItemSeparatorUIModel>(binding) {

        private val context get() = itemView.context

        override fun bind(item: ItemSeparatorUIModel) {
            setText(item.text)
            setTextSize(item.textSize)
            setTextColor(item.textColor)
            setTextFont(item.textFont)
        }

        fun setText(text: TextTypes) {
            binding.separatorText.setTextType(text)
        }

        fun setTextSize(@DimenRes size: Int) {
            val textSize = context.resources.getDimension(size)
            binding.separatorText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        }

        fun setTextFont(@FontRes font: Int) {
            val isFont = ResourcesCompat.getFont(context, font)
            binding.separatorText.typeface = isFont
        }

        fun setTextColor(@ColorRes color: Int) {
            val isColor = ContextCompat.getColor(context, color)
            binding.separatorText.setTextColor(isColor)
        }
    }
}