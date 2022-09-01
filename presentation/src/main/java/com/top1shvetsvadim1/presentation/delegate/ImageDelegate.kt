package com.top1shvetsvadim1.presentation.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.load
import com.top1shvetsvadim1.coreui.ItemDelegate
import com.top1shvetsvadim1.coreutils.BaseViewHolder
import com.top1shvetsvadim1.domain.uimodels.ImageUIModel
import com.top1shvetsvadim1.presentation.R
import com.top1shvetsvadim1.presentation.databinding.ImageDetailItemBinding

class ImageDelegate :
    ItemDelegate<ImageUIModel, ImageDelegate.ImageDetailViewHolder>(
        ImageUIModel::class
    ) {

    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ImageDetailViewHolder {
        return ImageDetailViewHolder(ImageDetailItemBinding.inflate(inflater,parent,false))
    }

    override fun onBindViewHolder(
        holder: ImageDetailViewHolder,
        item: ImageUIModel,
        payload: MutableList<Any>
    ) {
        val payloads = payload.firstOrNull() as List<ImageUIModel.ImagePayloads>?

        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, item, payload)
        } else {
            payloads.forEach {
                when (it) {
                    is ImageUIModel.ImagePayloads.ImageChanged -> holder.setImage(it.newImage)
                }
            }
        }
    }

    inner class ImageDetailViewHolder(private val binding: ImageDetailItemBinding) :
        BaseViewHolder<ImageUIModel>(binding) {
        override fun bind(item: ImageUIModel) {
            setImage(item.image)
        }

        fun setImage(image: String) {
            binding.ivLogo.load(image)
        }
    }
}