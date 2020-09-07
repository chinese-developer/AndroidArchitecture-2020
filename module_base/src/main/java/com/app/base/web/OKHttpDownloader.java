package com.app.base.web;

import com.android.base.utils.IOCloseUtils;
import com.android.sdk.net.NetConfig;
import com.blankj.utilcode.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class OKHttpDownloader {

    /**
     * @param url      下载地址
     * @param saveFile 保存的路径
     * @return Observable，实际类型参数为保存后的路径
     */
    public static Observable<String> download(String url, File saveFile) {
        return Observable.just(url)
                .subscribeOn(Schedulers.io())
                .map(s -> downLoadImage(url, saveFile))
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static String downLoadImage(String url, File saveFile) {

        OkHttpClient okHttpClient = NetConfig.INSTANCE.getOKHttpRegular();

        Request request = new Request.Builder()
                .url(url)
                .build();

        ResponseBody body = null;

        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                body = response.body();
            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (body == null) return "";

        FileUtils.createOrExistsDir(saveFile);

        InputStream is = null;
        FileOutputStream fos = null;
        try {
            byte[] buf = new byte[1024 * 2];
            int len;
            is = body.byteStream();
            fos = new FileOutputStream(saveFile);
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOCloseUtils.closeIOQuietly(is, fos);
        }

        return saveFile.getAbsolutePath();
    }
}
