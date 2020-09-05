/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.android.cache;

import android.content.Context;

import timber.log.Timber;

public class DiskLruStorageFactoryImpl implements StorageFactory {

    @Override
    public Builder newBuilder(Context context) {
        return new DiskLruStorageBuilder(context);
    }

    static class DiskLruStorageBuilder extends Builder {

        DiskLruStorageBuilder(Context context) {
            super(context);
        }

        @Override
        public Builder enableMultiProcess(boolean multiProcess) {
            if (multiProcess) {
                Timber.tag(CacheConstantKt.TAG).d("DiskLruStorage was initialized, but do not support multi process");
            }
            super.enableMultiProcess(multiProcess);
            return this;
        }

        @Override
        public Storage build() {
            DiskLruStorageImpl diskLruStorage = new DiskLruStorageImpl(context, storageId);
            if (encipher != null) {
                return new EncipherStorage(diskLruStorage, encipher);
            }
            return diskLruStorage;
        }
    }

}