package com.top1shvetsvadim1.domain

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.flow.Flow

@JsonClass(generateAdapter = true)
data class ProductListDTO(
    @Json(name = "current_page")
    val currentPage: Int,
    @Json(name = "results")
    val results: List<ProductItemDTO>
)