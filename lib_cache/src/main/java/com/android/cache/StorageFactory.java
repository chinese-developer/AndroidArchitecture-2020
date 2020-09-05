/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.android.cache;

import android.content.Context;

public interface StorageFactory {

    Builder newBuilder(Context context);

    @SuppressWarnings("UnusedReturnValue")
    abstract class Builder {

        Context context;
        String storageId;
        boolean multiProcess;
        Encipher encipher;
        long cacheTime;

        Builder(Context context) {
            this.context = context;
        }

        /**
         * 是否允许跨进程访问存储
         */
        public Builder enableMultiProcess(boolean multiProcess) {
            this.multiProcess = multiProcess;
            return this;
        }

        /**
         * @param storageId 存储标识
         */
        public Builder storageId(String storageId) {
            this.storageId = storageId;
            return this;
        }

        public Builder encipher(Encipher encipher) {
            this.encipher = encipher;
            return this;
        }

        public Builder cacheTime(long cacheTime) {
            this.cacheTime = cacheTime;
            return this;
        }

        public abstract Storage build();
    }

}