/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 */

package com.android.cache;

@SuppressWarnings("JavadocReference")
class CacheEntity {

    Object obj; // 数据
    /**
     * {@link CacheConstantKt.TIME_HOUR * 3 保存3小时}
     * {@link CacheConstantKt.TIME_DAY * 2 保存2天}
     */
    long cacheTime;
    long storeTime; // 存储时间戳

    CacheEntity(Object obj, long cacheTime) {
        this.obj = obj;
        this.cacheTime = cacheTime;
        storeTime = System.currentTimeMillis();
    }

}