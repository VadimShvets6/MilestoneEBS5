package com.top1shvetsvadim1.presentation.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.top1shvetsvadim1.coreui.ItemDelegate
import com.top1shvetsvadim1.coreutils.BaseViewHolder
import com.top1shvetsvadim1.domain.uimodels.DetailProductUIModel
import com.top1shvetsvadim1.presentation.R
import com.top1shvetsvadim1.presentation.databinding.DetailProductItemBinding
import java.util.*

class DetailProductDelegate :
    ItemDelegate<DetailProductUIModel, DetailProductDelegate.DetailProductViewHolder>(
        DetailProductUIModel::class
    ) {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): DetailProductDelegate.DetailProductViewHolder {
        return DetailProductViewHolder(DetailProductItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: DetailProductViewHolder,
        item: DetailProductUIModel,
        payload: MutableList<Any>
    ) {
        val payloads = payload.firstOrNull() as List<DetailProductUIModel.DetailProductPayloads>?

        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, item, payload)
        } else {
            payloads.forEach {
                when (it) {
                    is DetailProductUIModel.DetailProductPayloads.PriceChanged -> holder.setPrice(
                        it.newPrice
                    )
                }
            }
        }
    }

    inner class DetailProductViewHolder(private val binding: DetailProductItemBinding) :
        BaseViewHolder<DetailProductUIModel>(binding) {
        override fun bind(item: DetailProductUIModel) {
            setName(item.name)
            setPrice(item.price)
            setSize(item.size)
        }

        fun setName(name: String) {
            binding.tvProductName.text = name
        }

        fun setSize(size: String) {
            binding.tvSize.text = size
        }

        fun setPrice(price: Int) {
            binding.tvPrice.text = String.format(Locale.getDefault(), "$%d,-", price)
            binding.tvSmallPrice.text = String.format(Locale.getDefault(), "$%d,-", price)
        }
    }
}

//TODO: Create ItemText (it is a big task). There will be text, margin, fontSize, color, style and etc. You can use Novalles, if you think you understand payloding.


