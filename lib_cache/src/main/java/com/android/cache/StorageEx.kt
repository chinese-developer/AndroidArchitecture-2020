package com.android.cache

import com.github.dmstocking.optional.java.util.Optional
import io.reactivex.Flowable
import io.reactivex.Observable

inline fun <reified T> Storage.getEntity(key: String): T? {
    return this.getEntity(key, object : TypeFlag<T>() {}.type)
}

inline fun <reified T> Storage.toFlowable(key: String): Flowable<T> {
    return this.flowable(key, object : TypeFlag<T>() {}.type)
}

inline fun <reified T> Storage.toFlowableOptional(key: String): Flowable<Optional<T>> {
    return this.flowableOptional(key, object : TypeFlag<T>() {}.type)
}

inline fun <reified T> Storage.toObservableOptional(key: String): Observable<Optional<T>> {
    return this.observableOptional(key, object : TypeFlag<T>() {}.type)
}

inline fun <reified T> Storage.toObservable(key: String): Observable<T> {
    return this.observable(key, object : TypeFlag<T>() {}.type)
}