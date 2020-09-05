/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.android.cache;

import android.content.Context;

public class MMKVStorageFactoryImpl implements StorageFactory {

    @Override
    public Builder newBuilder(Context context) {
        return new MMKVStorageBuilder(context);
    }

    static class MMKVStorageBuilder extends Builder {

        MMKVStorageBuilder(Context context) {
            super(context);
        }

        @Override
        public Storage build() {
            MMKVStorageImpl mmkvStorage = new MMKVStorageImpl(context, storageId, multiProcess, cacheTime);
            if (encipher != null) {
                return new EncipherStorage(mmkvStorage, encipher);
            }

            return mmkvStorage;
        }
    }

}
