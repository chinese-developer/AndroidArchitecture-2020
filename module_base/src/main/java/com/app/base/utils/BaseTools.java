package com.app.base.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.DrawableRes;

import com.android.base.utils.BaseUtils;
import com.app.base.AppContext;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;

import timber.log.Timber;

public class BaseTools {

    /**
     * 实现文本复制功能
     *
     * @param content 复制的文本
     */
    public static void copy(String content) {
        if (!TextUtils.isEmpty(content)) {
            ClipboardManager cmb = (ClipboardManager) AppContext.Companion.get().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(content.trim());
            ClipData clipData = ClipData.newPlainText(null, content);
            cmb.setPrimaryClip(clipData);
        }
    }

    /**
     * 获取系统剪切板内容
     */
    public static String getClipContent() {
        ClipboardManager manager = (ClipboardManager) AppContext.Companion.get().getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null && manager.getPrimaryClip() != null) {
            if (manager.hasPrimaryClip() && manager.getPrimaryClip().getItemCount() > 0) {
                CharSequence addedText = manager.getPrimaryClip().getItemAt(0).getText();
                String addedTextString = String.valueOf(addedText);
                if (!TextUtils.isEmpty(addedTextString)) {
                    return formatUrl(String.valueOf(addedText));
                }
            }
        }
        return "";
    }

    /**
     * 清空剪切板内容
     * 加上  manager.setText(null);  不然小米3Android6.0 清空无效
     * 因为api过期使用最新注意使用 manager.getPrimaryClip()，不然小米3Android6.0 清空无效
     */
    public static void clearClipboard() {
        try {
            ClipboardManager manager = (ClipboardManager) AppContext.Companion.get().getSystemService(Context.CLIPBOARD_SERVICE);
            if (manager != null) {
                try {
                    manager.setPrimaryClip(Objects.requireNonNull(manager.getPrimaryClip()));
                    manager.setText(null);
                } catch (Exception e) {
                    Timber.e(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用浏览器打开链接
     */
    public static void openLink(Context context, String content) {
        Uri issuesUrl = Uri.parse(content);
        Intent intent = new Intent(Intent.ACTION_VIEW, issuesUrl);
        context.startActivity(intent);
    }

    /**
     * 获取剪切板上的链接
     */
    private static String formatUrl(String url) {
        if (url.startsWith("http")) {
            return url;
        } else if (!url.startsWith("http") && url.contains("http")) {
            // 有http且不在头部
            url = url.substring(url.indexOf("http"), url.length());
        } else if (url.startsWith("www")) {
            // 以"www"开头
            url = "http://" + url;
        } else if (!url.startsWith("http") && (url.contains(".me") || url.contains(".com") || url.contains(".cn"))) {
            // 不以"http"开头且有后缀
            url = "http://www." + url;
        } else {
            url = "";
        }
        if (url.contains(" ")) {
            int i = url.indexOf(" ");
            url = url.substring(0, i);
        }
        return url;
    }

    public static String getResourcesUri(@DrawableRes int id) {
        Resources resources = BaseUtils.getResources();
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
    }

    // Drawable转换成Bitmap
    public Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE
                        ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0,
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    // 将Bitmap转换成InputStream(压缩率quality、100表示不压缩、10表示压缩90%)
    public InputStream Bitmap2InputStream(Bitmap bm, int quality) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

}
