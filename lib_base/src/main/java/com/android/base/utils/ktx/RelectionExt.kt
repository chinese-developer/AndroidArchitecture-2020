package com.android.base.utils.ktx

import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

fun <T> Any.safeAs(): T? {
    @Suppress("UNCHECKED_CAST")
    return this as? T
}

fun <T : Any> T.deepCopy(): T {
    // 如果不是 DataClass 数据类, 则直接返回
    if (!this::class.isData) {
        return this
    }
    return this::class.primaryConstructor!!.let { primaryConstructor ->
        primaryConstructor.parameters.map { parameter ->
            // value = 属性的值
            val value =
                // class 转 KClass<T>后, get(this) 才能拿到 receiver 而不是 Nothing.
                // 分析:
                // 我们可以看到 this::class 的返回值是 KClass<out T>, 此处存在协变, 而当调用 get(receiver: T) 函数时, 参数又是逆变点, 所以 get() 的返回值自然是 Nothing
                // 这里我们将 this::class 强转成 KClass<T>, 因为是 DataClass 类型不存在继承, 自然不会存在协变发生, 强转后 get() 就可以拿到真实的 receiver 了
                this::class.safeAs<KClass<T>>()?.memberProperties?.first { it.name == parameter.name }?.get(this)
            if (parameter.type.classifier?.safeAs<KClass<*>>()?.isData == true) {
                parameter to value?.deepCopy()
            } else {
                parameter to value
            }
        }.toMap().let(primaryConstructor::callBy)
    }
}

inline fun <reified From: Any, reified To: Any> From.mapAs(): To {
    return From::class.memberProperties.map {
        it.name to it.get(this)
    }.toMap().mapAs()
}

inline fun <reified To: Any> Map<String, Any?>.mapAs(): To {
    return To::class.primaryConstructor!!.let {
        it.parameters.map { parameter ->
            parameter to (this[parameter.name] ?: if (parameter.type.isMarkedNullable) null else throw IllegalArgumentException("${parameter.name} is required but missing."))
        }.toMap().let(it::callBy)
    }
}