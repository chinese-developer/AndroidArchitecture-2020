package com.app.base.utils

import android.database.Cursor
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.app.base.scope.AndroidScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * 方便迭代Cursor, 针对数据库的便携函数
 */
fun Cursor.foreach(block: Cursor.() -> Unit) {
    if (count == 0) {
        close()
        return
    }
    moveToFirst()
    do {
        block()
    } while (moveToNext())
    close()
}

/**
 * 快速创建LiveData的观察者
 */
fun <M> LifecycleOwner.observe(liveData: LiveData<M>?, block: M?.() -> Unit) {
    liveData?.observe(this, Observer { it.block() })
}

/**
 * 监听数据库
 */
@UseExperimental(ExperimentalCoroutinesApi::class)
fun <T> Flow<List<T>>.listen(
    lifecycleOwner: LifecycleOwner? = null,
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    block: (List<T>) -> Unit
) {
    AndroidScope(lifecycleOwner, lifeEvent).launch {
        distinctUntilChanged().collect { data ->
            withMain {
                block(data)
            }
        }
    }
}

/**
 * 继承这个类可以快速创建具备saveInstance的ViewModel
 */
open class SavedViewModel(var saved: SavedStateHandle) : ViewModel()

/**
 * 返回当前组件指定的ViewModel
 */
inline fun <reified M : ViewModel> ViewModelStoreOwner.getViewModel(): M {
    return ViewModelProvider(this).get(M::class.java)
}

/**
 * 返回当前组件指定的SavedViewModel
 */
inline fun <reified M : ViewModel> FragmentActivity.getSavedModel(): M {
    return ViewModelProvider(
        this,
        SavedStateViewModelFactory(application, this)
    ).get(M::class.java)
}

/**
 * 返回当前组件指定的SavedViewModel
 */
inline fun <reified M : ViewModel> Fragment.getSavedModel(): M {
    return ViewModelProvider(
        this,
        SavedStateViewModelFactory(activity!!.application, this)
    ).get(M::class.java)
}
