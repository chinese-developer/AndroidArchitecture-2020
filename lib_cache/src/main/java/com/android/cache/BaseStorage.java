/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 */

package com.android.cache;

import com.github.dmstocking.optional.java.util.Optional;

import java.lang.reflect.Type;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

abstract class BaseStorage implements Storage {

    @Override
    public void putEntity(@NonNull String key, Object entity, long cacheTime) {
        CommonImpl.putEntity(key, entity, cacheTime, this);
    }

    @Override
    public void putEntity(@NonNull String key, Object entity) {
        CommonImpl.putEntity(key, entity, 0, this);
    }

    @Override
    public <T> T getEntity(@NonNull String key, @NonNull Type type) {
        return CommonImpl.getEntity(key, type, this);
    }

    @Override
    public <T> Flowable<T> flowable(@NonNull String key, @NonNull Type type) {
        return CommonImpl.flowableEntity(key, type, this);
    }

    @Override
    public <T> Flowable<Optional<T>> flowableOptional(@NonNull String key, @NonNull Type type) {
        return CommonImpl.flowableOptionalEntity(key, type, this);
    }

    @Override
    public <T> Observable<T> observable(String key, Type type) {
        return CommonImpl.observableEntity(key, type, this);
    }

    @Override
    public <T> Observable<Optional<T>> observableOptional(String key, Type type) {
        return CommonImpl.observableOptionalEntity(key, type, this);
    }

}
