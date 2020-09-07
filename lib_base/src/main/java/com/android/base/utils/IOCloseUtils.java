package com.android.base.utils;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;

import java.io.Closeable;
import java.io.IOException;

public class IOCloseUtils {

    private IOCloseUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 安静关闭IO
     */
    public static void closeIO(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭IO
     */
    public static void closeIOQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }

    /**
     * 关闭IO
     *
     * @param closeables closeable
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            closeIO(closeable);
        }
    }

    /**
     * 安静关闭IO
     *
     * @param closeables closeable
     */
    public static void closeIOQuietly(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            closeIOQuietly(closeable);
        }
    }

}
