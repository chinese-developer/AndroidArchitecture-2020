/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 */

package com.android.cache;

import android.text.TextUtils;
import android.util.Log;

import com.github.dmstocking.optional.java.util.Optional;

import java.lang.reflect.Type;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import timber.log.Timber;

import static com.android.cache.CacheConstantKt.TAG;

final class CommonImpl {

    static void putEntity(String key, Object entity, long cacheTime, Storage storage) {
        if (entity == null) {
            storage.remove(key);
            return;
        }
        CacheEntity cacheEntity = new CacheEntity(StorageContext.provideSerializer().toJson(entity), cacheTime);
        storage.putString(key, StorageContext.provideSerializer().toJson(cacheEntity));
    }

    private static String getCacheEntity(String key, Storage storage) {
        String cacheStr = storage.getString(key, "");
        if (TextUtils.isEmpty(cacheStr)) {
            return null;
        }

        CacheEntity cacheEntity = StorageContext.provideSerializer().fromJson(cacheStr, CacheEntity.class);

        if (cacheEntity == null) {
            return null;
        }

        if (cacheEntity.cacheTime == 0) {
            return cacheEntity.jsonData;
        }

        long currentTimeMillis = System.currentTimeMillis();
        long expiryTimeMillis = (currentTimeMillis - cacheEntity.storeTime) / 1000;
        Timber.tag(TAG).d("expiryDate:%s < %s", String.valueOf(expiryTimeMillis), cacheEntity.cacheTime);

        if (expiryTimeMillis < cacheEntity.cacheTime) {
            return cacheEntity.jsonData;
        } else {
            storage.remove(key);
        }
        return null;
    }

    static <T> T getEntity(String key, Type clazz, Storage storage) {

        String cacheEntity = getCacheEntity(key, storage);
        Timber.tag(CacheConstantKt.TAG).d("cacheEntity = %s", cacheEntity);
        if (cacheEntity != null) {
            return StorageContext.provideSerializer().fromJson(cacheEntity, clazz);
        }
        return null;
    }

    static <T> Flowable<T> flowableEntity(String key, Type clazz, Storage storage) {
        return Flowable.defer(() -> {
            T entity = storage.getEntity(key, clazz);
            if (entity == null) {
                return Flowable.empty();
            } else {
                return Flowable.just(entity);
            }
        });
    }

    static <T> Observable<T> observableEntity(String key, Type clazz, Storage storage) {
        return Observable.defer(() -> {
            T entity = storage.getEntity(key, clazz);
            if (entity == null) {
                return Observable.empty();
            } else {
                return Observable.just(entity);
            }
        });
    }

    static <T> Flowable<Optional<T>> flowableOptionalEntity(String key, Type clazz, Storage storage) {
        return Flowable.fromCallable(() -> {
            T entity = storage.getEntity(key, clazz);
            return Optional.ofNullable(entity);
        });
    }

    static <T> Observable<Optional<T>> observableOptionalEntity(String key, Type clazz, Storage storage) {
        return Observable.fromCallable(() -> {
            T entity = storage.getEntity(key, clazz);
            return Optional.ofNullable(entity);
        });
    }

}