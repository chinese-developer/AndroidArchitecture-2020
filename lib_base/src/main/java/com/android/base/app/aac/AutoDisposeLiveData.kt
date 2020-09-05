package com.android.base.app.aac

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.base.data.*
import com.android.base.rx.subscribeIgnoreError
import com.github.dmstocking.optional.java.util.Optional
import com.uber.autodispose.*

// -----------------------------------------------------------------------------------------

fun <T> ObservableSubscribeProxy<List<T>>.subscribeWithListLiveData(liveData: MutableLiveData<Resource<List<T>>>) {
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

fun <T, R> ObservableSubscribeProxy<List<T>>.subscribeWithListLiveData(liveData: MutableLiveData<Resource<List<R>>>, map: (List<T>) -> List<R>) {
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

fun <T> ObservableSubscribeProxy<Optional<List<T>>>.subscribeOptionalWithListLiveData(liveData: MutableLiveData<Resource<List<T>>>) {
    liveData.postValue(statusLoading())
    this.subscribe(
            {
                liveData.postValue(statusSucceededOptionalList(it))
            },
            {
                liveData.postValue(statusError(it))
            }
    )
}

fun <T, R> ObservableSubscribeProxy<Optional<List<T>>>.subscribeOptionalWithListLiveData(liveData: MutableLiveData<Resource<List<R>>>, map: (List<T>) -> List<R>) {
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

fun <T> FlowableSubscribeProxy<List<T>>.subscribeWithListLiveData(liveData: MutableLiveData<Resource<List<T>>>) {
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

fun <T, R> FlowableSubscribeProxy<List<T>>.subscribeWithListLiveData(liveData: MutableLiveData<Resource<List<R>>>, map: (List<T>) -> List<R>) {
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

fun <T> FlowableSubscribeProxy<Optional<List<T>>>.subscribeOptionalWithListLiveData(liveData: MutableLiveData<Resource<List<T>>>) {
    liveData.postValue(statusLoading())
    this.subscribe(
            {
                liveData.postValue(statusSucceededOptionalList(it))
            },
            {
                liveData.postValue(statusError(it))
            }
    )
}

fun <T, R> FlowableSubscribeProxy<Optional<List<T>>>.subscribeOptionalWithListLiveData(liveData: MutableLiveData<Resource<List<R>>>, map: (List<T>) -> List<R>) {
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

fun CompletableSubscribeProxy.subscribeWithListLiveData(liveData: MutableLiveData<Resource<Any>>) {
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

fun <T> CompletableSubscribeProxy.subscribeWithListLiveData(liveData: MutableLiveData<Resource<List<T>>>, provider: () -> List<T>) {
    liveData.postValue(statusLoading())
    this.subscribe(
            {
                liveData.postValue(statusSucceeded((provider())))
            },
            {
                liveData.postValue(statusError(it))
            }
    )
}

// -----------------------------------------------------------------------------------------

fun <T> ObservableSubscribeProxy<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> ObservableSubscribeProxy<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
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

fun <T> ObservableSubscribeProxy<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> ObservableSubscribeProxy<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
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

fun <T> FlowableSubscribeProxy<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> FlowableSubscribeProxy<T>.subscribeWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
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

fun <T> FlowableSubscribeProxy<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<T>>) {
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

fun <T, R> FlowableSubscribeProxy<Optional<T>>.subscribeOptionalWithLiveData(liveData: MutableLiveData<Resource<R>>, map: (T) -> R) {
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

fun CompletableSubscribeProxy.subscribeWithLiveData(liveData: MutableLiveData<Resource<Any>>) {
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

fun <T> CompletableSubscribeProxy.subscribeWithLiveData(liveData: MutableLiveData<Resource<T>>, provider: () -> T) {
    liveData.postValue(statusLoading())
    this.subscribe(
            {
                liveData.postValue(statusSucceeded((provider())))
            },
            {
                liveData.postValue(statusError(it))
            }
    )
}

// -----------------------------------------------------------------------------------------

fun <T> ObservableSubscribeProxy<T>.toResourceLiveData(): LiveData<Resource<T>> {
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

fun <T> ObservableSubscribeProxy<Optional<T>>.optionalToResourceLiveData(): LiveData<Resource<T>> {
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

fun <T> FlowableSubscribeProxy<T>.toResourceLiveData(): LiveData<Resource<T>> {
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

fun <T> FlowableSubscribeProxy<Optional<T>>.optionalToResourceLiveData(): LiveData<Resource<T>> {
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

// -----------------------------------------------------------------------------------------

fun <T> ObservableSubscribeProxy<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}

fun <T> FlowableSubscribeProxy<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}

fun <T> SingleSubscribeProxy<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}

fun <T> MaybeSubscribeProxy<T>.toLiveData(): LiveData<T> {
    val liveData = MutableLiveData<T>()
    this.subscribeIgnoreError {
        liveData.postValue(it)
    }
    return liveData
}