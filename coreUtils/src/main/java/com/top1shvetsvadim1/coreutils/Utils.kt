package com.top1shvetsvadim1.coreutils

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern


inline fun <reified T, reified R> Collection<T>.safeCast(): List<R> {
    return mapNotNull { it as? R }
}

interface BaseUseCase

interface UseCaseNoParams<RETURN> : BaseUseCase {
    suspend operator fun invoke(): RETURN
}

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun CharSequence?.isHardPassword(): Boolean {
    val pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$")
    val matcher: Matcher? = this?.let { pattern.matcher(it) }
    return matcher?.matches() ?: false
}

fun CharSequence?.isMediumPassword(): Boolean {
    val pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$")
    val matcher: Matcher? = this?.let { pattern.matcher(it) }
    return matcher?.matches() ?: false
}

fun CharSequence?.isLowPassword(): Boolean {
    val pattern = Pattern.compile("^(?=.*[0-9])|(?=.*[a-z])(?=\\S+$).{4,}$")
    val matcher: Matcher? = this?.let { pattern.matcher(it) }
    return matcher?.matches() ?: false
}

fun CharSequence?.isValidFullName(): Boolean {
    val pattern = Pattern.compile("^[\\w]+\\s[\\w]+$")
    val matcher: Matcher? = this?.let { pattern.matcher(it) }
    return matcher?.matches() ?: false
}

interface UseCase<PARAM : Any, RETURN : Any> {
    suspend operator fun invoke(params: PARAM): RETURN
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
            summoner.reducer.manageResult(EventResult.Error(ex))
        }
    }
}

inline fun <reified RETURN : Any, reified TO : Any> UseCaseNoParams<RETURN>.run(
    summoner: BaseViewModel<*, *, *>,
    emitLoading: Boolean = true,
    crossinline transform: suspend (RETURN) -> TO
) {
    summoner.viewModelScope.launchIO {
        if (emitLoading) {
            summoner.reducer.manageResult(EventResult.Loading)
        }
        try {
            val payload = EventResult.Success(transform(invoke()))
            summoner.reducer.manageResult(payload)
        } catch (ex: Exception) {
            Log.d("MainFrag", "$ex")
            summoner.reducer.manageResult(EventResult.Error(ex))
        }
    }
}

inline fun <reified T> tryNull(tryAction: () -> T?): T? {
    return try {
        tryAction()
    } catch (ignored: Exception) {
        null
    }
}

