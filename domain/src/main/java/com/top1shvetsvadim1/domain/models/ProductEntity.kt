package com.top1shvetsvadim1.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductEntity(
    val id: Int,
    val name: String,
    val details: String,
    val size: String,
    val colour: String,
    val price: Int,
    val count: Int,
    val mainImage: String,
    val isFavorite: Boolean,
    val inCart: Boolean
)

@JsonClass(generateAdapter = true)
@Entity(tableName = "items")
data class ProductFavorite(
    @PrimaryKey val id: Int,
    val name: String,
    val details: String,
    val size: String,
    val colour: String,
    val price: Int,
    val mainImage: String,
    val isFavorite: Boolean,
    val inCart: Boolean,
    val count: Int
)

@JsonClass(generateAdapter = true)
@Entity(tableName = "cart_items")
data class ProductsInCarts(
    @PrimaryKey val id: Int,
    val name: String,
    val details: String,
    val size: String,
    val colour: String,
    val price: Int,
    val count: Int,
    val mainImage: String,
    val isFavorite: Boolean,
    val inCart: Boolean,
)