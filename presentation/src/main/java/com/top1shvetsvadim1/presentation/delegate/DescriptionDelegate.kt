package com.top1shvetsvadim1.presentation.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.top1shvetsvadim1.coreui.ItemDelegate
import com.top1shvetsvadim1.coreutils.BaseViewHolder
import com.top1shvetsvadim1.domain.uimodels.DescriptionUIModel
import com.top1shvetsvadim1.presentation.databinding.DescriptionItemBinding

class DescriptionDelegate :
    ItemDelegate<DescriptionUIModel, DescriptionDelegate.DescriptionViewHolder>(
        DescriptionUIModel::class
    ) {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): DescriptionViewHolder {
        return DescriptionViewHolder(DescriptionItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: DescriptionViewHolder,
        item: DescriptionUIModel,
        payload: MutableList<Any>
    ) {
        val payloads = payload.firstOrNull() as List<DescriptionUIModel.DescriptionPayloads>?

        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, item, payload)
        } else {
            payloads.forEach {
                when (it) {
                    is DescriptionUIModel.DescriptionPayloads.DescriptionChanged -> holder.setDescription(
                        it.newDescription
                    )
                }
            }
        }
    }

    inner class DescriptionViewHolder(private val binding: DescriptionItemBinding) :
        BaseViewHolder<DescriptionUIModel>(binding) {
        override fun bind(item: DescriptionUIModel) {
            setDescription(item.description)
        }

        fun setDescription(description: String) {
            binding.tvDescription.text = description
        }
    }
}