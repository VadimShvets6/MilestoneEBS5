package com.top1shvetsvadim1.coreutils

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

inline fun <reified T, reified R> Collection<T>.safeCast(): List<R> {
    return mapNotNull { it as? R }
}

interface BaseUseCase

interface UseCaseNoParams<RETURN> : BaseUseCase {
    suspend operator fun invoke(): RETURN
}

interface UseCase<PARAM : Any, RETURN : Any> {
    suspend operator fun invoke(params: PARAM): RETURN
}

//TODO: unused
abstract class ItemViewHolder<I : BaseUIModel>(binding: ViewBinding) :
    RecyclerView.ViewHolder(binding.root) {
    abstract fun bind(item: I)
}

fun CoroutineScope.launchIO(action: suspend () -> Unit): Job {
    return launch(Dispatchers.IO) {
        action()
    }
}

inline fun <reified PARAM : Any, reified RETURN : Any> UseCase<PARAM, RETURN>.run(
    summoner: BaseViewModel<*, *, *>,
    params: PARAM,
    emitLoading: Boolean = true
) {
    summoner.viewModelScope.launchIO {
        if (emitLoading) {
            summoner.reducer.manageResult(EventResult.Loading)
        }
        try {
            val payload = EventResult.Success(invoke(params))
            summoner.reducer.manageResult(payload)
        } catch (ex: Exception) {
            Log.d("MainFrag", "$ex")
            summoner.reducer.manageResult(EventResult.Error(ex))
        }
    }
}

inline fun <reified PARAM : Any, reified RETURN : Any, reified TO : Any> UseCase<PARAM, RETURN>.run(
    summoner: BaseViewModel<*, *, *>,
    params: PARAM,
    emitLoading: Boolean = true,
    crossinline transform: suspend (RETURN) -> TO
) {
    summoner.viewModelScope.launchIO {
        if (emitLoading) {
            summoner.reducer.manageResult(EventResult.Loading)
        }
        try {
            val payload = EventResult.Success(transform(invoke(params)))
            summoner.reducer.manageResult(payload)
        } catch (ex: Exception) {
            Log.d("MainFrag", "$ex")
            summoner.reducer.manageResult(EventResult.Error(ex))
        }
    }
}

inline fun <reified PARAM : Any, reified RETURN : Any> UseCase<PARAM, Flow<RETURN>>.collect(
    summoner: BaseViewModel<*, *, *>,
    params: PARAM,
    emitLoading: Boolean = true
) {
    summoner.viewModelScope.launchIO {
        if (emitLoading) {
            summoner.reducer.manageResult(EventResult.Loading)
        }
        try {
            invoke(params).collectLatest {
                summoner.reducer.manageResult(EventResult.Success(it))
            }
        } catch (ex: Exception) {
            Log.d("MainFrag", "$ex")
            summoner.reducer.manageResult(EventResult.Error(ex))
        }
    }
}

inline fun <reified PARAM : Any, reified RETURN : Any, reified TO : Any> UseCase<PARAM, Flow<RETURN>>.collect(
    summoner: BaseViewModel<*, *, *>,
    params: PARAM,
    emitLoading: Boolean = true,
    crossinline transform: suspend (Flow<RETURN>) -> Flow<TO>
) {
    summoner.viewModelScope.launchIO {
        if (emitLoading) {
            summoner.reducer.manageResult(EventResult.Loading)
        }
        try {
            transform(invoke(params)).collectLatest {
                summoner.reducer.manageResult(EventResult.Success(it))
            }
        } catch (ex: Exception) {
            Log.d("MainFrag", "$ex")
            summoner.reducer.manageResult(EventResult.Error(ex))
        }
    }
}

inline fun <reified B> UseCaseNoParams<B>.run(
    summoner: BaseViewModel<*, *, *>,
    emitLoading: Boolean = true
) {
    summoner.viewModelScope.launchIO {
        if (emitLoading) {
            summoner.reducer.manageResult(EventResult.Loading)
        }
        try {
            val payload = EventResult.Success(invoke())
            summoner.reducer.manageResult(payload)
        } catch (ex: Exception) {
            ex.printStackTrace()
            Log.d("MainFrag", "$ex")
            summoner.reducer.manageResult(EventResult.Error(ex))
        }
    }
}