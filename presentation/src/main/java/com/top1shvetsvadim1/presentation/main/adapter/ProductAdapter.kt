package com.top1shvetsvadim1.presentation.main.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.top1shvetsvadim1.coreui.Drawable
import com.top1shvetsvadim1.domain.ProductPayload
import com.top1shvetsvadim1.domain.ProductUIModel
import com.top1shvetsvadim1.presentation.databinding.ProductItemBinding

class ProductAdapter(
    val onAction: (ActionProductAdapter) -> Unit
) : ListAdapter<ProductUIModel, ProductAdapter.ProductViewHolder>(ProductDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val productItem = getItem(position)
        holder.bind(productItem)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val payload = payloads.firstOrNull() as List<ProductPayload>?

        if (payload.isNullOrEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payload.forEach {
                when (it) {
                    is ProductPayload.NameChanged -> holder.setName(it.newName)
                    is ProductPayload.PictureChange -> holder.setPicture(it.newPicture)
                    is ProductPayload.PriceChange -> holder.setPrice(it.newPrice)
                    is ProductPayload.FavoriteChange -> holder.setFavorite(it.newStatus)
                }
            }
        }
        holder.setOnclickListeners(currentList[position])
    }

    inner class ProductViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(productItem: ProductUIModel) {
            setName(productItem.name)
            setPrice(productItem.price)
            setPicture(productItem.mainImage)
            setSize(productItem.size)
            setFavorite(productItem.isFavorite)
            setOnclickListeners(productItem)
        }

        fun setOnclickListeners(productItem: ProductUIModel) {
            binding.buttonHeart.setOnClickListener {
                Log.d("Test", "Adapter: ${productItem.isFavorite}")
                onAction(ActionProductAdapter.OnProductFavoriteClicked(productItem.id, !productItem.isFavorite))
            }
            binding.root.setOnClickListener {
                onAction(ActionProductAdapter.OnProductClicked(productItem))
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

        fun setSize(size: String) {
            binding.tvSize.text = size
        }
    }

    sealed interface ActionProductAdapter {
        data class OnProductFavoriteClicked(val id: Int, val setFavorite: Boolean) : ActionProductAdapter
        data class OnProductClicked(val productItem: ProductUIModel) : ActionProductAdapter
    }
}