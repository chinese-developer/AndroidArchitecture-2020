package com.app.base.web.actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.URLUtil;

import androidx.lifecycle.Lifecycle;

import com.android.base.utils.common.Checker;
import com.app.base.R;
import com.app.base.config.DirectoryManager;
import com.app.base.web.OKHttpDownloader;
import com.app.base.web.BaseWebFragment;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.io.File;

final class SaveImageAction {

    private final BaseWebFragment mFragment;
    private final String[] mArgs;

    private SaveImageAction(BaseWebFragment fragment, String[] args) {
        mFragment = fragment;
        mArgs = args;
    }

    public static void action(BaseWebFragment fragment, String[] args) {
        new SaveImageAction(fragment, args).run();
    }

    private void run() {
        if (Checker.isEmpty(mArgs)) {
            return;
        }

        String imageUrl = mArgs[0];

        if (!URLUtil.isNetworkUrl(imageUrl)) {
            return;
        }

        OKHttpDownloader.download(imageUrl, getSaveFile(imageUrl))
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(mFragment, Lifecycle.Event.ON_DESTROY)))
                .subscribe(
                        this::showImageSaveSuccess,
                        throwable -> mFragment.showMessage(R.string.image_save_fail_tips));
    }

    private void showImageSaveSuccess(@SuppressWarnings("unused") String path) {
        mFragment.showMessage(mFragment.getString(R.string.image_save_success_tips));
        Context context = mFragment.getContext();
        if (context != null) {
            try {
                //https://juejin.im/post/5ae0541df265da0b9d77e45a
                MediaStore.Images.Media.insertImage(context.getContentResolver(), path, "", "");
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private File getSaveFile(String url) {
        int index = url.lastIndexOf("/");
        String fileName = url.substring(index + 1, url.length());
        String path = DirectoryManager.createDCIMPictureStorePath(fileName);
        return new File(path);
    }

}
