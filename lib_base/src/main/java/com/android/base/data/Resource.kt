package com.android.base.data

import com.android.base.data.Resource.*
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.annotations.NonNull
import io.reactivex.annotations.Nullable

sealed class Resource<out R> {

    data class Success<out T>(@Nullable val data: T) : Resource<T>()
    data class Error(@NonNull val throwable: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
    object NoChanged : Resource<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[throwable=$throwable]"
            NoChanged -> "Data no change"
            Loading -> "Loading ..."
        }
    }
}

val Resource<*>.isSucceeded
    get() = this is Success

val Resource<*>.isError
    get() = this is Error

val Resource<*>.isLoading
    get() = this is Loading

val Resource<*>.isNoChanged
    get() = this is NoChanged

/** `true` if [Result] is of type [Success] & holds non-null [Success.data]. */
val Resource<*>.isNonNull
    get() = this is Success && data != null

val Resource<*>.getDataSucceededOrException: Any?
    get() = when (this) {
        is Success -> data
        else -> UnsupportedOperationException("This method can only be called when the state success");
    }

fun <T> Resource<T>.get(): T? {
    return when (this) {
        is Success -> data
        else -> null
    }
}

fun <T> Resource<T>.orElse(@Nullable defaultValue: T): T {
    return when (this) {
        is Success -> data ?: defaultValue
        else -> defaultValue
    }
}

fun <T> statusSucceeded(data: T): Success<T> = Success(data)
fun <T> statusSucceeded(data: List<T>): Success<List<T>> = Success(data)
fun <T> statusSucceeded(@NonNull data: Optional<T>): Success<T> = Success(data.get())
fun <T> statusSucceededOptional(data: Optional<T>): Success<T> = Success(data.orElse(null))
fun <T> statusSucceededOptionalList(data: Optional<List<T>>): Success<List<T>> = Success(data.orElse(null))
fun <T> statusSucceededWithOptional(data: Optional<T>): Optional<Success<T>> = Optional.ofNullable(Success(data.orElse(null)))
fun statusError(throwable: Throwable): Resource<Nothing> = Error(throwable)
fun statusLoading(): Resource<Nothing> = Loading
fun statusDataNoChanged(): Resource<Nothing> = NoChanged


/**when in isLoading*/
inline fun <T> Resource<T>.onLoading(onLoading: () -> Unit): Resource<T> {
    if (this.isLoading) {
        onLoading()
    }
    return this
}

/**when error occurred*/
inline fun <T> Resource<T>.onError(onError: (error: Throwable) -> Unit): Resource<T> {
    if (isError) onError((this as Error).throwable)
    return this
}

/**when no change*/
inline fun <T> Resource<T>.onNoChange(onNoChange: () -> Unit): Resource<T> {
    if (isNoChanged) onNoChange()
    return this
}

/**when isSucceeded*/
inline fun <T> Resource<T>.onSuccess(onSuccess: (data: T?) -> Unit): Resource<T> {
    if (this.isSucceeded) onSuccess(orElse(null))
    return this
}

/**when isSucceeded and has data*/
inline fun <T> Resource<T>.onSuccessWithData(onSuccess: (data: T) -> Unit): Resource<T> {
    val data = get()
    if (data != null) onSuccess(data)
    return this
}