package com.example.architecture.home.repository

import org.joor.Reflect
import java.util.concurrent.ConcurrentHashMap

object HomeModel {

    private val modelMap = ConcurrentHashMap<String, AbsHomeModel>()

    fun AbsHomeModel.register(name: String = this.javaClass.simpleName) {
        modelMap[name] = this
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : AbsHomeModel> String.get(): T {
        return modelMap[this] as? T
            ?: when (this) {
                NetworkModel::class.java.simpleName -> {
                    Reflect.on(NetworkModel::class.java).create().get<NetworkModel>()
                    modelMap[this] as T
                }
                else -> {
                    Reflect.on(LocalCacheModel::class.java).create().get<LocalCacheModel>()
                    modelMap[this] as T
                }
            }

    }
}