package com.android.sdk.x5.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * <pre>
 *     desc  : 网络请求工具类
 * </pre>
 */
public class OkHttpUtils {

    private static OkHttpClient mOkHttpClient = null;

    private static OkHttpClient withOkHttpClient() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }
        return mOkHttpClient;
    }

    public static void downloadFile(Context context, final String fileUrl, final File file, final FileCallback fileCallback) {
        if (!hasNetworkPermission(context)) {
            X5LogUtils.d("has no network permission to download file");
            fileCallback.fail(-1, null);
            return;
        }
        final Request request = new Request.Builder().url(fileUrl).build();
        final Call call = withOkHttpClient().newCall(request);
        call.enqueue(new Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream is = null;
                    byte[] buf = new byte[1024];
                    int len = 0;
                    FileOutputStream fos = null;
                    try {
                        ResponseBody responseBody = response.body();
                        if (responseBody != null) {
                            is = responseBody.byteStream();
                            fos = new FileOutputStream(file);
                            while ((len = is.read(buf)) != -1) {
                                fos.write(buf, 0, len);
                            }
                            fos.flush();
                            fileCallback.success();
                        } else {
                            fileCallback.fail(-1, null);
                        }
                    } catch (IOException e) {
                        fileCallback.fail(-1, e);
                        e.printStackTrace();
                    } finally {
                        try {
                            if (is != null) {
                                is.close();
                            }
                            if (fos != null) {
                                fos.close();
                            }
                        } catch (IOException e) {
                            fileCallback.fail(-1, e);
                            e.printStackTrace();
                        }
                    }
                } else {
                    fileCallback.fail(response.code(), null);
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                fileCallback.fail(-1, e);
            }
        });
    }

    public interface FileCallback {
        void success();
        void fail(int code, Exception e);
    }

    private static boolean hasNetworkPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context != null &&
                    PackageManager.PERMISSION_GRANTED == context.checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) &&
                    PackageManager.PERMISSION_GRANTED == context.checkSelfPermission(Manifest.permission.INTERNET)) {
                return true;
            }
            return false;
        }
        return true;
    }

}
