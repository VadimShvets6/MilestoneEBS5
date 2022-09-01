package com.top1shvetsvadim1.domain.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductList(
    @Json(name = "current_page")
    val currentPage: Int,
    @Json(name = "results")
    val results: List<ProductItem>
)