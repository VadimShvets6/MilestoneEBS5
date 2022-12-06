package com.top1shvetsvadim1.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.top1shvetsvadim1.data.network.ApiService
import com.top1shvetsvadim1.domain.models.Filters
import com.top1shvetsvadim1.domain.models.ProductItem

class ProductPagingSource constructor(
    private val apiService: ApiService,
    private val filters: Filters
) : PagingSource<Int, ProductItem>() {

    override fun getRefreshKey(state: PagingState<Int, ProductItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductItem> {
        return try {
            val currentPage = params.key ?: START_PAGE
            val result = if (filters == Filters.RESET) {
                apiService.getProductList(page = currentPage).results
            } else {
                apiService.getProductList(ordering = filters.name.lowercase(), page = currentPage).results
            }

            LoadResult.Page(
                data = result,
                prevKey = if (currentPage == START_PAGE) null else -1,
                nextKey = if (result.isNotEmpty()) currentPage.inc() else null
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        const val START_PAGE = 1
    }
}