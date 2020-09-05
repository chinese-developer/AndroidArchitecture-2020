package com.app.base.utils;

import android.annotation.SuppressLint;

import com.app.base.AppContext;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * 图片压缩工具
 *
 */
public class ImageCompressor {

    private ImageCompressor() {
        throw new UnsupportedOperationException();
    }

    @SuppressLint("CheckResult")
    public static void compressMulti(@NonNull List<String> photos, @NonNull final Callback<List<File>> callback) {
        callback.onStart();
        Observable.just(photos)
                .subscribeOn(Schedulers.io())
                .map(source1 -> {
                    try {
                        return Luban.with(AppContext.Companion.get()).load(photos).get();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        throwable -> {
                            throwable.printStackTrace();
                            callback.onError(throwable);
                        });
    }

    @SuppressLint("CheckResult")
    public static void compress(@NonNull String photo, @NonNull final Callback<File> callback) {
        Observable.just(photo)
                .subscribeOn(Schedulers.io())
                .map(s -> {
                    try {
                        return Luban.with(AppContext.Companion.get()).get(photo);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        callback::onSuccess,
                        throwable -> {
                            throwable.printStackTrace();
                            callback.onError(throwable);
                        });
    }


    public interface Callback<T> {
        void onSuccess(T result);

        default void onStart() {
        }

        default void onError(Throwable e) {
        }
    }


}
