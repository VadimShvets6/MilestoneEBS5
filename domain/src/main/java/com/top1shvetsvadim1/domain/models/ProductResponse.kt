package com.top1shvetsvadim1.domain.models

import androidx.paging.PagingData
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.flow.Flow

@JsonClass(generateAdapter = true)
data class ProductResponse(
    val result: Flow<List<ProductEntity>>
)

@JsonClass(generateAdapter = true)
data class ProductPagingResponse(
    val result : Flow<PagingData<ProductEntity>>
)

enum class Parameters {
    Text, Color
}
