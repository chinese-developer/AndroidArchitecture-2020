package com.android.base.app.aac

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.base.data.*
import com.android.base.rx.subscribeIgnoreError
import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.*


// -----------------------------------------------------------------------------------------

fun <T> Observable<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<T>>) {
    liveData.postValue(statusLoading())
    this.subscribe(
            {
                liveData.postValue(statusSucceeded(it))
            },
            {
                liveData.postValue(statusError(it))
            }
    )
}

fun <T, R> Observable<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
    liveData.postValue(statusLoading())
    this.subscribe(
            {
                liveData.postValue(statusSucceeded(map(it)))
            },
            {
                liveData.postValue(statusError(it))
            }
    )
}

fun <T> Observable<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<T>>) {
    liveData.postValue(statusLoading())
    this.subscribe(
            {
                liveData.postValue(statusSucceededOptional(it))
            },
            {
                liveData.postValue(statusError(it))
            }
    )
}

fun <T, R> Observable<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T?) -> R) {
    liveData.postValue(statusLoading())
    this.subscribe(
            {
                liveData.postValue(statusSucceeded(map(it.orElse(null))))
            },
            {
                liveData.postValue(statusError(it))
            }
    )
}

fun <T> Flowable<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<T>>) {
    liveData.postValue(statusLoading())
    this.subscribe(
            {
                liveData.postValue(statusSucceeded(it))
            },
            {
                liveData.postValue(statusError(it))
            }
    )
}

fun <T, R> Flowable<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
    liveData.postValue(statusLoading())
    this.subscribe(
            {
                liveData.postValue(statusSucceeded((map(it))))
            },
            {
                liveData.postValue(statusError(it))
            }
    )
}

fun <T> Flowable<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<T>>) {
    liveData.postValue(statusLoading())
    this.subscribe(
            {
                liveData.postValue(statusSucceededOptional(it))
            },
            {
                liveData.postValue(statusError(it))
            }
    )
}

fun <T, R> Flowable<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T?) -> R) {
    liveData.postValue(statusLoading())
    this.subscribe(
            {
                liveData.postValue(statusSucceeded(map(it.orElse(null))))
            },
            {
                liveData.postValue(statusError(it))
            }
    )
}

fun Completable.subscribeWithLiveData(liveData: MutableLiveData<Resource<Any>>) {
    liveData.postValue(statusLoading())
    this.subscribe(
            {
                liveData.postValue(statusSucceeded(""))
            },
            {
                liveData.postValue(statusError(it))
            }
    )
}

fun <T> Completable.subscribeWithLiveData(liveData: MutableLiveData<Resource<T>>, provider: () -> T) {
    liveData.postValue(statusLoading())
    this.subscribe(
            {
                liveData.postValue(statusSucceeded(provider()))
            },
            {
                liveData.postValue(statusError(it))
            }
    )
}

// -----------------------------------------------------------------------------------------

fun <T> Observable<T>.toResourceLiveData(): LiveData<Resource<T>> {
    val mutableLiveData = MutableLiveData<Resource<T>>()
    mutableLiveData.value = statusLoading()
    subscribe(
            {
                mutableLiveData.postValue(statusSucceeded(it))
            },
            {
                mutableLiveData.postValue(statusError(it))
            }
    )
    return mutableLiveData
}

fun <T> Observable<Optional<T>>.optionalToResourceLiveData(): LiveData<Resource<T>> {
    val mutableLiveData = MutableLiveData<Resource<T>>()
    mutableLiveData.value = statusLoading()
    subscribe(
            {
                mutableLiveData.postValue(statusSucceededOptional(it))
            },
            {
                mutableLiveData.postValue(statusError(it))
            }
    )
    return mutableLiveData
}

fun <T> Flowable<T>.toResourceLiveData(): LiveData<Resource<T>> {
    val mutableLiveData = MutableLiveData<Resource<T>>()
    mutableLiveData.value = statusLoading()
    subscribe(
            {
                mutableLiveData.postValue(statusSucceeded(it))
            },
            {
                mutableLiveData.postValue(statusError(it))
            }
    )
    return mutableLiveData
}

fun <T> Flowable<Optional<T>>.optionalToResourceLiveData(): LiveData<Resource<T>> {
    val mutableLiveData = MutableLiveData<Resource<T>>()
    mutableLiveData.value = statusLoading()
    subscribe(
            {
                mutableLiveData.postValue(statusSucceededOptional(it))
            },
            {
                mutableLiveData.postValue(statusError(it))
            }
    )
    return mutableLiveData
}

fun Completable.toResourceLiveData(): LiveData<Resource<Any>> {
    val mutableLiveData = MutableLiveData<Resource<Any>>()
    mutableLiveData.value = statusLoading()
    subscribe(
            {
                mutableLiveData.postValue(statusSucceeded(""))
            },
            {
                mutableLiveData.postValue(statusError(it))
            }
    )
    return mutableLiveData
}

// -----------------------------------------------------------------------------------------

fun <T> Observable<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}

fun <T> Flowable<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}

fun <T> Single<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}

fun <T> Maybe<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}