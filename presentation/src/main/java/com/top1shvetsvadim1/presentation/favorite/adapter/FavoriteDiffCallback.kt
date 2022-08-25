package com.top1shvetsvadim1.presentation.favorite.adapter

import androidx.recyclerview.widget.DiffUtil
import com.top1shvetsvadim1.domain.ProductPayload
import com.top1shvetsvadim1.domain.ProductUIModel

object FavoriteDiffCallback : DiffUtil.ItemCallback<ProductUIModel>() {
    override fun areItemsTheSame(oldItem: ProductUIModel, newItem: ProductUIModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductUIModel, newItem: ProductUIModel): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: ProductUIModel, newItem: ProductUIModel): Any? {
        val payloads = mutableListOf<ProductPayload>()

        if (oldItem.name != newItem.name) {
            payloads.add(ProductPayload.NameChanged(newName = newItem.name))
        }

        if(oldItem.price != newItem.price){
            payloads.add(ProductPayload.PriceChange(newPrice = newItem.price))
        }

        if(oldItem.mainImage != newItem.mainImage){
            payloads.add(ProductPayload.PictureChange(newPicture = newItem.mainImage))
        }

        if(oldItem.isFavorite != newItem.isFavorite){
            payloads.add(ProductPayload.FavoriteChange(newStatus = newItem.isFavorite))
        }

        return payloads
    }
}