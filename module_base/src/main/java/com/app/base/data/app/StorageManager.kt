package com.app.base.data.app

import android.content.Context
import com.android.cache.MMKVStorageFactoryImpl
import com.android.cache.Storage
import com.android.cache.TypeFlag
import timber.log.Timber
import java.lang.ref.WeakReference

class StorageManager internal constructor(private val context: Context, private val appDataSource: AppDataSource) {

    companion object {
        // data/data/package/files/mmkv/mvvm-stable-storage-id
        private const val STABLE_CACHE_ID = "mvvm-stable-storage-id"
        private const val USER_STORAGE_ID = "mvvm-user-storage-id"
        private const val ALL_USER_STORAGE_ID_KEY = "all_user_storage_id_key"
    }

    private val storageFactory = MMKVStorageFactoryImpl()

    private val userAssociated: Storage = storageFactory
        .newBuilder(context)
        .storageId(USER_STORAGE_ID)
        .build()

    private val stable: Storage = storageFactory
        .newBuilder(context)
        .storageId(STABLE_CACHE_ID)
        .build()

    private val userStorageIdsList by lazy {
        stable.getEntity<MutableList<String>>(
            ALL_USER_STORAGE_ID_KEY,
            object : TypeFlag<MutableList<String>>() {}.rawType)
            ?: mutableListOf()
    }

    private val storageCache = HashMap<String, WeakReference<Storage>>()

    /**  全局默认用户相关缓存实现，不支持跨进程，用户退出后缓存也会被清理。*/
    fun userStorage() = userAssociated

    /** 全局默认永久缓存实现，不支持跨进程，用户退出后缓存不会被清理。*/
    fun stableStorage() = stable

    @Synchronized
    fun newStorage(storageId: String, userAssociated: Boolean = false): Storage {
        if (userAssociated) {
            if (!userStorageIdsList.contains(storageId)) {
                userStorageIdsList.add(storageId)
                stableStorage().putEntity(ALL_USER_STORAGE_ID_KEY, userStorageIdsList)
            }
        }

        val weakReference = storageCache[storageId]

        if (weakReference != null) {
            val storage = weakReference.get()
            if (storage != null) {
                return storage
            }
        }

        val storage = storageFactory.newBuilder(context)
            .storageId(storageId)
            .build()

        storageCache[storageId] = WeakReference(storage)

        return storage
    }

    /**仅由[AppDataSource.logout]在退出登录时调用,当用户退出后，清除[userStorage]存储的该用户缓存数据*/
    internal fun clearUserAssociated() {
        userStorage().clearAll()

        if (userStorageIdsList.isEmpty()) {
            return
        }
        for (cacheId in userStorageIdsList) {
            if (cacheId.isEmpty()) {
                continue
            }
            storageFactory.newBuilder(context).storageId(cacheId).build().clearAll()
            Timber.d("clear user associated cache：$cacheId")
        }
        userStorageIdsList.clear()
        stableStorage().remove(ALL_USER_STORAGE_ID_KEY)
    }

}

/**
 * 针对设备的扩展的 [Storage]
 *
 */
class DeviceStorage internal constructor(real: Storage) : Storage by real {

    var setupAppRules: Boolean
        get() = getBoolean("isDeviceSetupAppRulesKey", false)
        set(value) = putBoolean("isDeviceSetupAppRulesKey", value)

}

