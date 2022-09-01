package com.top1shvetsvadim1.domain.models

import com.squareup.moshi.JsonClass
import com.top1shvetsvadim1.domain.uimodels.ProductUIModel
import kotlinx.coroutines.flow.Flow

@JsonClass(generateAdapter = true)
data class ProductResponse(
    val result: Flow<List<ProductUIModel>>
)