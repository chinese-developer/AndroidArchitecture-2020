/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 */

package com.android.cache;

@SuppressWarnings("JavadocReference")
class CacheEntity {

    String jsonData; // 数据
    /**
     * {@link CacheConstantKt.TIME_HOUR * 3 保存3小时}
     * {@link CacheConstantKt.TIME_DAY * 2 保存2天}
     */
    long cacheTime;
    long storeTime; // 存储时间戳

    CacheEntity(String jsonData, long cacheTime) {
        this.jsonData = jsonData;
        this.cacheTime = cacheTime;
        storeTime = System.currentTimeMillis();
    }

}