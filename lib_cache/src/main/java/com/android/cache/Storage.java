/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 */

package com.android.cache;

import com.github.dmstocking.optional.java.util.Optional;

import java.lang.reflect.Type;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * 缓存接口
 */
public interface Storage {

    /**
     * @param key  缓存的 key
     * @param type 缓存实体类型，如果是泛型类型，请使用 {@link TypeFlag}标识
     * @param <T>  缓存实体类型
     * @return 缓存
     */
    @Nullable
    <T> T getEntity(@NonNull String key, @NonNull Type type);

    /**
     * 如果没有获取到缓存，那么 Flowable 将不会发送任何数据，默认在调用线程加载缓存。
     *
     * @param key  缓存的 key
     * @param type 缓存实体类型，如果是泛型类型，请使用 {@link TypeFlag}标识
     * @param <T>  缓存实体类型
     * @return 缓存
     */
    <T> Flowable<T> flowable(@NonNull String key, @NonNull Type type);

    <T> Observable<T> observable(@NonNull String key, @NonNull Type type);

    /**
     * 默认在调用线程加载缓存。
     *
     * @param key  缓存的 key
     * @param type 缓存实体类型，如果是泛型类型，请使用 {@link TypeFlag}标识
     * @param <T>  缓存实体类型
     * @return 缓存
     */
    <T> Flowable<Optional<T>> flowableOptional(@NonNull String key, @NonNull Type type);

    <T> Observable<Optional<T>> observableOptional(@NonNull String key, @NonNull Type type);

    /**
     * 时间规则 参考 {@link com.android.cache.CacheConstantKt}
     * @param key 缓存的 key
     * @param entity 缓存实体类型，如果是泛型类型，请使用 {@link TypeFlag}标识
     * @param cacheTime 60 * 60 有效期一小时
     */
    void putEntity(@NonNull String key, @Nullable Object entity, long cacheTime);

    void putEntity(@NonNull String key, @Nullable Object entity);

    void putString(@NonNull String key, @Nullable String value);

    @NonNull
    String getString(@NonNull String key, @NonNull String defaultValue);

    @Nullable
    String getString(@NonNull String key);

    void putLong(@NonNull String key, long value);

    long getLong(@NonNull String key, long defaultValue);

    void putInt(@NonNull String key, int value);

    int getInt(@NonNull String key, int defaultValue);

    void putBoolean(@NonNull String key, boolean value);

    boolean getBoolean(@NonNull String key, boolean defaultValue);

    /**
     * 根据Key值，删除当前key的本地缓存文件
     */
    void remove(@NonNull String key);

    /**
     * 清理本地缓存文件（根据具体业务场景使用）
     */
    void clearAll();
}
