package com.android.sdk.x5.helper;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.android.sdk.x5.R;
import com.android.sdk.x5.utils.EncodeUtils;
import com.android.sdk.x5.utils.OkHttpUtils;
import com.android.sdk.x5.utils.ToastUtils;
import com.android.sdk.x5.utils.WebFileUtils;
import com.tencent.smtt.sdk.WebView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <pre>
 *     desc  : 长按保存图片功能，因为请求图片涉及网络请求，后期这部分需要优化，暂时未用到
 * </pre>
 */
public final class SaveImageProcessor {

    /**
     * 上下文
     */
    private Context mContext;

    public boolean showActionMenu(final WebView webView) {
        final Context context = webView.getContext();
        if (context == null) {
            return false;
        }
        mContext = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setItems(new CharSequence[]{mContext.getString(R.string.save_image)}, (dialogInterface, i) -> {
            if (hasExtStoragePermission(mContext)) {
                saveImage(mContext, webView);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    private void saveImage(final Context context, final WebView webView) {
        File rootImagePath = WebFileUtils.getImageDir(context);
        if (rootImagePath == null) {
            ToastUtils.showRoundRectToast(context.getString(R.string.save_image_failed));
            return;
        }
        WebView.HitTestResult hitTestResult = webView.getHitTestResult();
        if (hitTestResult == null) {
            ToastUtils.showRoundRectToast(context.getString(R.string.save_image_failed));
            return;
        }
        String imageUrl = hitTestResult.getExtra();
        if (imageUrl == null) {
            ToastUtils.showRoundRectToast(context.getString(R.string.save_image_failed));
            return;
        } else if (imageUrl.startsWith("data:")) {
            String imageData = imageUrl.replaceFirst("data:image\\/\\w+;base64,", "");
            byte[] imageDataBytes = EncodeUtils.base64Decode(imageData);
            String fileName = WebFileUtils.getImageName("");
            File imageFile = new File(rootImagePath, fileName);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                fileOutputStream.write(imageDataBytes);
                fileOutputStream.close();
                String imagePath = imageFile.getAbsolutePath();
                insertMedia(context, imagePath);
                ToastUtils.showRoundRectToast(context.getString(R.string.save_image_succeed) + imagePath);
            } catch (IOException e) {
                ToastUtils.showRoundRectToast(context.getString(R.string.save_image_succeed));
                e.printStackTrace();
            }
        } else {
            Uri imageUri = Uri.parse(imageUrl);
            String fileName = imageUri.getLastPathSegment();
            if (TextUtils.isEmpty(fileName)) {
                fileName = WebFileUtils.getStringMd5(imageUrl);
            }
            final File imageFile = new File(rootImagePath, fileName);
            OkHttpUtils.downloadFile(context, imageUrl, imageFile, new OkHttpUtils.FileCallback() {
                @Override
                public void success() {
                    final String imagePath = imageFile.getAbsolutePath();
                    insertMedia(context, imagePath);
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showRoundRectToast(context.getString(R.string.save_image_succeed) + imagePath);
                        }
                    });
                }

                @Override
                public void fail(int i, Exception e) {
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtils.showRoundRectToast(context.getString(R.string.network_error));
                        }
                    });
                }
            });
        }
    }

    private boolean hasExtStoragePermission(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                        context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * 刷新图片
     *
     * @param context   上下文
     * @param imagePath 图片路径
     */
    private void insertMedia(Context context, String imagePath) {
        if (imagePath == null || imagePath.length() == 0 || context == null) {
            return;
        }
        try {
            // notify the system media
            MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, "", "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                final Uri contentUri = Uri.parse(imagePath);
                scanIntent.setData(contentUri);
                context.sendBroadcast(scanIntent);
            } else {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse(imagePath)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

