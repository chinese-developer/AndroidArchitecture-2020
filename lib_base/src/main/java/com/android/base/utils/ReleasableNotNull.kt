package com.android.base.utils

import com.android.base.utils.ktx.safeAs
import kotlin.jvm.internal.CallableReference
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible


/**
 * 可释放对象引用不可空类型
 *
 * private lateinit var bitmap: Bitmap
 *
 * private var bitmap by releasableNotNull<Bitmap>()
 *
 * fun onDestroy() {
 *   // bitmap = null // error
 *    ::bitmap.release() // bitmap need init first
 * }
 */
class ReleasableNotNull<T : Any> : ReadWriteProperty<Any, T> {
    private var value: T? = null

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if (!isReflectionEnabled && this.value == null) {
            var map = releasableRefs[thisRef]
            if (map == null) {
                map = HashMap()
                releasableRefs[thisRef] = map
            }
            map[property.name] = this
        }
        this.value = value
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return value ?: throw IllegalStateException("Not Initialized or released already.")
    }

    fun isInitialized() = value != null

    fun release() {
        value = null
    }
}

val <R> KProperty0<R>.isInitialized: Boolean
    get() {
        if (isReflectionEnabled) {
            isAccessible = true
            return getDelegate()?.safeAs<ReleasableNotNull<*>>()?.isInitialized()
                ?: throw IllegalAccessException("Delegate is null or is not an instance of ReleasableNotNull.")
        }
        return this.safeAs<CallableReference>()?.let {
            releasableRefs[it.boundReceiver]?.get(this.name)?.isInitialized()
        } ?: false
    }

fun <R> KProperty0<R>.release() {
    if (isReflectionEnabled) {
        isAccessible = true
        return (getDelegate() as? ReleasableNotNull<*>)?.release()
            ?: throw IllegalAccessException("Delegate is null or is not an instance of ReleasableNotNull.")
    }
    (this as? CallableReference)?.let {
        releasableRefs[it.boundReceiver]?.get(this.name)?.release()
    }
}

internal lateinit var releasableRefs: WeakIdentityMap<Any, MutableMap<String, ReleasableNotNull<*>>>

private val isReflectionEnabled by lazy {
    try {
        Class.forName("kotlin.reflect.jvm.internal.KClassImpl")
        true
    } catch (e: Exception) {
        releasableRefs = WeakIdentityMap()
        false
    }
}