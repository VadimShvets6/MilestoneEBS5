package com.top1shvetsvadim1.domain

import com.squareup.moshi.JsonClass
import com.top1shvetsvadim1.coreutils.UseCaseNoParams
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class GetProductListUseCase @Inject constructor(
    private val repository: ProductRepository
): UseCaseNoParams<ProductResponse> {

    override suspend operator fun invoke() = repository.getProductList().map {
        it.map { entity ->
            ProductUIModel(
                name = entity.name,
                size = entity.size,
                price = entity.price,
                mainImage = entity.mainImage,
                id = entity.id,
                isFavorite = entity.isFavorite
            )
        }
    }.let {
        ProductResponse(it)
    }
}

//@JsonClass(generateAdapter = true)
//data class ProductResponse(
//    val result: Flow<List<ProductUIModel>>
//)

//interface UseCase<PARAM: Any, RETURN: Any> {
//    suspend operator fun invoke(params: PARAM): RETURN
//}

//fun <PARAM: Any, RETURN: Any> UseCase<PARAM, RETURN>.launchIn(coroutineScope: CoroutineScope, params: PARAM, onEmit: (State<RETURN>) -> Unit) {
//    coroutineScope.launch(Dispatchers.IO) {
//        onEmit(Response.Loading)
//        try {
//            onEmit(invoke(params))
//        } catch (ex: Exception) {
//            onEmit(Response.Failure(ex))
//        }
//    }
//}

//interface UseCaseNoParams<RETURN: Any> {
//    suspend operator fun invoke(): RETURN
//}

