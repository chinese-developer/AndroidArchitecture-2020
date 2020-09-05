/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 */

package com.android.cache;

import android.content.Context;

import com.tencent.mmkv.MMKV;

import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import timber.log.Timber;

import static com.android.cache.CacheConstantKt.TAG;

@SuppressWarnings("WeakerAccess,unused")
public class MMKVStorageImpl extends BaseStorage {

    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);
    private static final String KEY_EXPIRY_TIME_MILLIS = "key_expiry_time_millis";

    private final MMKV mMmkv;
    private long cacheTime;

    public MMKVStorageImpl(Context context, String mmkvId) {
        this(context, mmkvId, false, 0);
    }

    public MMKVStorageImpl(Context context, String mmkvId, boolean multiProcess, long cacheTime) {

        if (INITIALIZED.compareAndSet(false, true)) {
            String rootDir = MMKV.initialize(context.getApplicationContext());
            Timber.tag(TAG).d("MMKV initialized and rootDir is: %s", rootDir);
        }

        int mode = multiProcess ? MMKV.MULTI_PROCESS_MODE : MMKV.SINGLE_PROCESS_MODE;
        mMmkv = MMKV.mmkvWithID(mmkvId, mode);

        if (cacheTime != 0) {
            if (mMmkv.decodeLong(KEY_EXPIRY_TIME_MILLIS, 0) == 0) {
                mMmkv.encode(KEY_EXPIRY_TIME_MILLIS, System.currentTimeMillis());
            }
        }

        this.cacheTime = cacheTime;
    }

    @Override
    public void putString(@NonNull String key, @Nullable String value) {
        try {
            if (value == null) {
                remove(key);
                return;
            }
            mMmkv.encode(key, value);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    @NonNull
    @Override
    public String getString(@NonNull String key, @NonNull String defaultValue) {
        String result = null;
        try {
            if (checkExpiryDate()) {
                result = mMmkv.decodeString(key, defaultValue);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return result == null ? defaultValue : result;
    }

    @Nullable
    @Override
    public String getString(@NonNull String key) {
        try {
            if (checkExpiryDate()) {
                return mMmkv.decodeString(key);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return null;
    }

    @Override
    public void putLong(@NonNull String key, long value) {
        try {
            mMmkv.encode(key, value);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    @Override
    public long getLong(@NonNull String key, long defaultValue) {
        try {
            if (checkExpiryDate()) {
                return mMmkv.decodeLong(key, defaultValue);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public void putInt(@NonNull String key, int value) {
        try {
            mMmkv.encode(key, value);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    @Override
    public int getInt(@NonNull String key, int defaultValue) {
        try {
            if (checkExpiryDate()) {
                return mMmkv.decodeInt(key, defaultValue);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public void putBoolean(@NonNull String key, boolean value) {
        try {
            mMmkv.encode(key, value);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    @Override
    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        try {
            if (checkExpiryDate()) {
                return mMmkv.decodeBool(key, defaultValue);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return defaultValue;
    }

    @Override
    public void remove(@NonNull String key) {
        try {
            mMmkv.removeValueForKey(key);
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    @Override
    public void clearAll() {
        mMmkv.clear();
    }

    public boolean checkExpiryDate() {
        try {
            if (cacheTime != 0) {
                long currentTimeMillis = System.currentTimeMillis();
                long expiryTimeMillis = mMmkv.decodeLong(KEY_EXPIRY_TIME_MILLIS, 0);
                long _expiryTimeMillis = (currentTimeMillis - expiryTimeMillis) / 1000;

                Timber.tag(TAG).d("expiryDate:%s < %s", String.valueOf(_expiryTimeMillis), cacheTime);
                boolean hasNotExpired = _expiryTimeMillis < cacheTime;

                if (!hasNotExpired) {
                    clearAll();
                    mMmkv.encode(KEY_EXPIRY_TIME_MILLIS, System.currentTimeMillis());
                }
                return hasNotExpired;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}