package com.android.base.utils.android;

import android.os.StrictMode;

public class DebugUtils {

    private DebugUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 开启严苛模式
     */
    public static void startStrictMode() {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .build());

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeathOnNetwork()
                .build());
    }

}