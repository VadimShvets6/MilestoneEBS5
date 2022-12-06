package com.top1shvetsvadim1.domain.models

enum class Filters {
    PRICE, SIZE, COLOR, RESET
}
fun Filters.filtersToString() : String{
    return when(this){
        Filters.PRICE -> "price"
        Filters.SIZE -> "size"
        Filters.COLOR -> "colour"
        Filters.RESET -> ""
    }
}