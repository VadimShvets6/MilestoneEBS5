package com.top1shvetsvadim1.presentation.delegate

import android.view.View
import coil.load
import com.top1shvetsvadim1.coreutils.BaseViewHolder
import com.top1shvetsvadim1.coreutils.ItemDelegate
import com.top1shvetsvadim1.coreutils.Payloadable
import com.top1shvetsvadim1.domain.DescriptionUIModel
import com.top1shvetsvadim1.domain.DetailProductUIModel
import com.top1shvetsvadim1.domain.ImageUIModel
import com.top1shvetsvadim1.presentation.R
import com.top1shvetsvadim1.presentation.databinding.DescriptionItemBinding
import com.top1shvetsvadim1.presentation.databinding.DetailProductItemBinding
import com.top1shvetsvadim1.presentation.databinding.ImageDetailItemBinding
import java.util.*

class ImageDelegate :
    ItemDelegate<ImageUIModel, ImageDelegate.ImageDetailViewHolder>(
        ImageUIModel::class,
        R.layout.image_detail_item
    ) {

    override fun createViewHolder(view: View): ImageDetailViewHolder {
        return ImageDetailViewHolder(ImageDetailItemBinding.bind(view))
    }

    override fun onBindViewHolder(
        holder: ImageDetailViewHolder,
        item: ImageUIModel,
        payload: MutableList<Any>
    ) {
        val payloads = payload.firstOrNull() as List<Payloadable>?

        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, item, payload)
        } else {
            //TODO: in delegates you should explicitly cast payloads
            payloads.forEach {
                when (it) {
                    is ImageUIModel.ImagePayloads -> when (it) {
                        is ImageUIModel.ImagePayloads.ImageChanged -> holder.setImage(it.newImage)
                    }
                }
            }
        }
    }

    //TODO: bad code style
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
//TODO: delegates should be in separate classes with their UI models
class DetailProductDelegate :
    ItemDelegate<DetailProductUIModel, DetailProductDelegate.DetailProductViewHolder>(
        DetailProductUIModel::class,
        R.layout.detail_product_item
    ) {
    override fun createViewHolder(view: View): DetailProductDelegate.DetailProductViewHolder {
        return DetailProductViewHolder(DetailProductItemBinding.bind(view))
    }

    override fun onBindViewHolder(
        holder: DetailProductViewHolder,
        item: DetailProductUIModel,
        payload: MutableList<Any>
    ) {
        //TODO: suppress warning
        val payloads = payload.firstOrNull() as List<Payloadable>?

        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, item, payload)
        } else {
            payloads.forEach {
                when (it) {
                    is DetailProductUIModel.DetailProductPayloads -> when (it) {
                        is DetailProductUIModel.DetailProductPayloads.PriceChanged -> holder.setPrice(
                            it.newPrice
                        )
                    }
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
class DescriptionDelegate :
    ItemDelegate<DescriptionUIModel, DescriptionDelegate.DescriptionViewHolder>(
        DescriptionUIModel::class,
        R.layout.description_item
    ) {

    override fun createViewHolder(view: View): DescriptionViewHolder {
        return DescriptionViewHolder(DescriptionItemBinding.bind(view))
    }

    override fun onBindViewHolder(
        holder: DescriptionViewHolder,
        item: DescriptionUIModel,
        payload: MutableList<Any>
    ) {
        val payloads = payload.firstOrNull() as List<Payloadable>?

        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, item, payload)
        } else {
            payloads.forEach {
                when (it) {
                    is DescriptionUIModel.DescriptionPayloads -> when (it) {
                        is DescriptionUIModel.DescriptionPayloads.DescriptionChanged -> holder.setDescription(
                            it.newDescription
                        )
                    }
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