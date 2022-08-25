package com.top1shvetsvadim1.presentation.favorite.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.top1shvetsvadim1.coreui.Drawable
import com.top1shvetsvadim1.domain.ProductPayload
import com.top1shvetsvadim1.domain.ProductUIModel
import com.top1shvetsvadim1.presentation.databinding.ProductItemBinding

class FavoriteAdapter(
    val onAction: (FavoriteAdapter.ActionFavoriteAdapter) -> Unit
) : ListAdapter<ProductUIModel, FavoriteAdapter.FavoriteViewHolder>(FavoriteDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ProductItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteItem = getItem(position)
        holder.bind(favoriteItem)
    }

    override fun onBindViewHolder(
        holder: FavoriteViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val payload = payloads.firstOrNull() as List<ProductPayload>?

        if (payload.isNullOrEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payload.forEach {
                when (it) {
                    is ProductPayload.FavoriteChange -> holder.setFavorite(it.newStatus)
                    is ProductPayload.NameChanged -> holder.setName(it.newName)
                    is ProductPayload.PictureChange -> holder.setPicture(it.newPicture)
                    is ProductPayload.PriceChange -> holder.setPrice(it.newPrice)
                }
            }
        }
        holder.setOnclickListeners(currentList[position])
    }

    inner class FavoriteViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteItem: ProductUIModel) {
            setName(favoriteItem.name)
            setPrice(favoriteItem.price)
            setPicture(favoriteItem.mainImage)
            setFavorite(favoriteItem.isFavorite)
            setSize(favoriteItem.size)
            setOnclickListeners(favoriteItem)
        }

        fun setOnclickListeners(productItem: ProductUIModel) {
            binding.buttonHeart.setOnClickListener {
                onAction(
                    ActionFavoriteAdapter.OnProductFavoriteClicked(
                        productItem.id,
                        !productItem.isFavorite
                    )
                )
            }
            binding.root.setOnClickListener {
                onAction(ActionFavoriteAdapter.OnProductClicked(productItem))
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

    sealed interface ActionFavoriteAdapter {
        data class OnProductFavoriteClicked(val id: Int, val setFavorite: Boolean) :
            ActionFavoriteAdapter

        data class OnProductClicked(val productItem: ProductUIModel) : ActionFavoriteAdapter
    }
}