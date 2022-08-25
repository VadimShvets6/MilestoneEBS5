package com.top1shvetsvadim1.domain

import com.squareup.moshi.JsonClass
import kotlinx.coroutines.flow.Flow

@JsonClass(generateAdapter = true)
data class ProductResponse(
    val result: Flow<List<ProductUIModel>>
)
