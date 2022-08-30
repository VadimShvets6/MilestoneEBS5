package com.top1shvetsvadim1.presentation.delegate

import android.view.View
import coil.load
import com.top1shvetsvadim1.coreui.Drawable
import com.top1shvetsvadim1.coreutils.Action
import com.top1shvetsvadim1.coreutils.BaseViewHolder
import com.top1shvetsvadim1.coreutils.ItemDelegate
import com.top1shvetsvadim1.domain.ProductPayload
import com.top1shvetsvadim1.domain.ProductUIModel
import com.top1shvetsvadim1.presentation.R
import com.top1shvetsvadim1.presentation.databinding.ProductItemBinding

class ProductDelegate : ItemDelegate<ProductUIModel, ProductDelegate.FavoriteViewHolder>(
    ProductUIModel::class,
    R.layout.product_item
) {
    override fun createViewHolder(view: View): FavoriteViewHolder {
        return FavoriteViewHolder(ProductItemBinding.bind(view))
    }

    override fun onBindViewHolder(
        holder: FavoriteViewHolder,
        item: ProductUIModel,
        payload: MutableList<Any>
    ) {
        val payloads = payload.firstOrNull() as List<ProductPayload>?

        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, item, payload)
        } else {
            payloads.forEach {
                when (it) {
                    is ProductPayload.FavoriteChange -> holder.setFavorite(it.newStatus)
                    is ProductPayload.NameChanged -> holder.setName(it.newName)
                    is ProductPayload.PictureChange -> holder.setPicture(it.newPicture)
                    is ProductPayload.PriceChange -> holder.setPrice(it.newPrice)
                }
            }
        }

    }

    inner class FavoriteViewHolder(private val binding: ProductItemBinding) :
        BaseViewHolder<ProductUIModel>(binding) {
        override fun bind(item: ProductUIModel) {
            setName(item.name)
            setPrice(item.price)
            setPicture(item.mainImage)
            setFavorite(item.isFavorite)
            setSize(item.size)
            setOnClickListeners(item)
        }

        override fun setOnClickListeners(item: ProductUIModel) {
            binding.buttonHeart.setOnClickListener {
                pushAction(ActionProductAdapter.OnProductFavoriteClicked(item.id, !item.isFavorite))
            }
            binding.root.setOnClickListener {
                pushAction(ActionProductAdapter.OnProductClicked(item))
            }
        }

        fun setName(name: String) {
            binding.tvProductName.text = name
        }

        fun setPrice(price: Int) {
            binding.tvPrice.text = "$price"
            binding.tvSmallPrice.text = "$price"
        }

        fun setPicture(picture: String) {
            binding.ivProduct.load(picture)
        }

        fun setFavorite(favorite: Boolean) {
            if (favorite) {
                binding.buttonHeart.load(Drawable.ic_favorite_main)
            } else {
                binding.buttonHeart.load(Drawable.ic_icon_heart)
            }
        }

        private fun setSize(size: String) {
            binding.tvSize.text = size
        }
    }

    sealed interface ActionProductAdapter : Action {
        data class OnProductFavoriteClicked(val id: Int, val setFavorite: Boolean) :
            ActionProductAdapter

        data class OnProductClicked(val productItem: ProductUIModel) : ActionProductAdapter
    }
}