package com.top1shvetsvadim1.coreutils


interface ViewState
interface ViewIntent
interface ViewEvent

sealed interface EventResult{
    data class Error(val error: Exception) : EventResult
    object Loading : EventResult
    data class Success <T> (val payload : T) : EventResult
}