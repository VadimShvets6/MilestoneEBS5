package com.top1shvetsvadim1.coreutils

import androidx.recyclerview.widget.DiffUtil


class DefaultDiffUtil(delegates: List<ItemDelegate<BaseUIModel, BaseViewHolder<BaseUIModel>>>) :
    DiffUtil.ItemCallback<BaseUIModel>() {

    private val uiModelHelpers = delegates.associate { it.clazz to it.uiModelHelper }

    override fun areItemsTheSame(oldItem: BaseUIModel, newItem: BaseUIModel): Boolean {
        return oldItem.areItemsTheSame(newItem, uiModelHelpers[oldItem::class] ?: return false)
    }

    override fun areContentsTheSame(oldItem: BaseUIModel, newItem: BaseUIModel): Boolean {
        return oldItem.areContentTheSame(newItem, uiModelHelpers[oldItem::class] ?: return false)
    }

    override fun getChangePayload(oldItem: BaseUIModel, newItem: BaseUIModel): Any {
        return oldItem.changePayload(newItem, uiModelHelpers[oldItem::class] ?: return listOf<Any>())
    }
}