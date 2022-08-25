package com.top1shvetsvadim1.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
@Entity(tableName = "items")
data class ProductEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val details: String,
    val size: String,
    val colour: String,
    val price: Int,
    val mainImage: String,
    val isFavorite : Boolean
)