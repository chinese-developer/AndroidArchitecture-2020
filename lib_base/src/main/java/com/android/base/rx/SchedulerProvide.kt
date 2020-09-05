package com.android.base.rx

import androidx.annotation.NonNull
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Allow providing different types of [Scheduler]s.
 */
interface SchedulerProvider {
    @NonNull
    fun computation(): Scheduler

    @NonNull
    fun io(): Scheduler

    @NonNull
    fun ui(): Scheduler

    @NonNull
    fun database(): Scheduler

    companion object {
        val default: SchedulerProvider
            get() = SchedulerProviderImpl.sDefaultSchedulerProvider
    }
}

class SchedulerProviderImpl @Inject constructor() : SchedulerProvider {
    @NonNull
    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    @NonNull
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    @NonNull
    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @NonNull
    override fun database(): Scheduler {
        return Schedulers.single()
    }

    companion object {
        val sDefaultSchedulerProvider = SchedulerProviderImpl()
    }
}
