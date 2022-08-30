package com.top1shvetsvadim1.coreutils

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

//TODO: bad code style
abstract class BaseViewHolder<I : BaseUIModel>(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item: I)
    open fun setOnClickListeners(item: I) {}
}