package com.top1shvetsvadim1.presentation.delegate

import android.view.LayoutInflater
import android.view.ViewGroup
import coil.load
import com.top1shvetsvadim1.coreui.Action
import com.top1shvetsvadim1.coreui.ItemDelegate
import com.top1shvetsvadim1.coreutils.BaseViewHolder
import com.top1shvetsvadim1.domain.uimodels.ProductPayload
import com.top1shvetsvadim1.domain.uimodels.ProductUIModel
import com.top1shvetsvadim1.presentation.databinding.ProductItemBinding

class ProductDelegate : ItemDelegate<ProductUIModel, ProductDelegate.ProductViewHolder>(
    ProductUIModel::class
) {
    override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): ProductViewHolder {
        return ProductViewHolder(ProductItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
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
                    is ProductPayload.SizeChange -> holder.setSize(it.newSize)
                    is ProductPayload.CartChange -> holder.setCart(it.newStatus)
                }
            }
        }
        holder.setOnClickListeners(item)
    }

    inner class ProductViewHolder(private val binding: ProductItemBinding) :
        BaseViewHolder<ProductUIModel>(binding) {
        override fun bind(item: ProductUIModel) {
            setName(item.name)
            setPrice(item.price)
            setPicture(item.mainImage)
            setFavorite(item.isFavorite)
            setCart(item.inCart)
            setSize(item.sizes)
            setOnClickListeners(item)
        }

        override fun setOnClickListeners(item: ProductUIModel) {
            binding.buttonHeart.setOnClickListener {
                pushAction(ActionProductAdapter.OnProductFavoriteClicked(item.id, !item.isFavorite))
            }
            binding.buttonBuschet.setOnClickListener {
                pushAction(ActionProductAdapter.OnProductCartClicked(item.id, !item.inCart))
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
            binding.buttonHeart.isActivated = favorite
        }

        fun setCart(cart: Boolean){
            binding.buttonBuschet.isActivated = cart
        }

        fun setSize(size: String) {
            binding.tvSize.text = size
        }
    }

    sealed interface ActionProductAdapter : Action {
        data class OnProductFavoriteClicked(val id: Int, val setFavorite: Boolean) : ActionProductAdapter
        data class OnProductClicked(val productItem: ProductUIModel) : ActionProductAdapter
        data class OnProductCartClicked(val id: Int, val setCart: Boolean) : ActionProductAdapter
    }
}