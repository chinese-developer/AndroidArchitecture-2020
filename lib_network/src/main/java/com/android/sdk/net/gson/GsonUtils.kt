/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.android.sdk.net.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.lang.reflect.Modifier

/**
 * registerTypeAdapter: 为某特定对象设置固定的序列或反序列方式，自定义Adapter需实现JsonSerializer或者JsonDeserializer接口
 */
object GsonUtils {
    private val GSON = GsonBuilder()
        .excludeFieldsWithModifiers(Modifier.TRANSIENT)
        /** 容错处理 */
        .excludeFieldsWithModifiers(Modifier.STATIC)
        .registerTypeAdapter(
            Int::class.javaPrimitiveType,
            JsonDeserializers.IntegerJsonDeserializer()
        )
        .registerTypeAdapter(Int::class.java, JsonDeserializers.IntegerJsonDeserializer())
        .registerTypeAdapter(
            Double::class.javaPrimitiveType,
            JsonDeserializers.DoubleJsonDeserializer()
        )
        .registerTypeAdapter(Double::class.java, JsonDeserializers.DoubleJsonDeserializer())
        .registerTypeAdapter(
            Float::class.javaPrimitiveType,
            JsonDeserializers.FloatJsonDeserializer()
        )
        .registerTypeAdapter(Float::class.java, JsonDeserializers.FloatJsonDeserializer())
        .registerTypeAdapter(String::class.java, JsonDeserializers.StringJsonDeserializer())
        .registerTypeAdapter(Void::class.java, JsonDeserializers.VoidJsonDeserializer())
        .registerTypeAdapter(
            Unit::class.java,
            JsonDeserializers.UnitJsonDeserializer()
        )
        /** 根据注解反序列化抽象类或接口 */
        .registerTypeAdapterFactory(AutoGenTypeAdapterFactory())
        .create()

    fun gson(): Gson {
        return GSON
    }
}
