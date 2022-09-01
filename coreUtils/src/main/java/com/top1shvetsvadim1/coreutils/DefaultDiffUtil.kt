package com.top1shvetsvadim1.coreutils

import androidx.recyclerview.widget.DiffUtil


class DefaultDiffUtil : DiffUtil.ItemCallback<BaseUIModel>() {
    override fun areItemsTheSame(oldItem: BaseUIModel, newItem: BaseUIModel): Boolean {
        return oldItem.areItemsTheSame(newItem)
    }

    override fun areContentsTheSame(oldItem: BaseUIModel, newItem: BaseUIModel): Boolean {
        return oldItem.areContentsTheSame(newItem)
    }

    override fun getChangePayload(oldItem: BaseUIModel, newItem: BaseUIModel): Any {
        return oldItem.changePayload(newItem)
    }
}