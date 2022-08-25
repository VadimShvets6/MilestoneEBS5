package com.top1shvetsvadim1.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductItemDTO(
    @Json(name = "name")
    val name: String,
    @Json(name = "details")
    val details: String,
    @Json(name = "size")
    val size: String,
    @Json(name = "colour")
    val colour: String,
    @Json(name = "price")
    val price: Int,
    @Json(name = "main_image")
    val mainImage: String,
    @Json(name = "id")
    val id: Int,
    val isFavorite : Boolean = false
)