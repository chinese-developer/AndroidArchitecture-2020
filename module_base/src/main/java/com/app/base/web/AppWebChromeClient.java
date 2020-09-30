package com.app.base.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.android.sdk.mediaselector.SystemMediaSelector;
import com.app.base.R;
import com.app.base.config.DirectoryManager;
import com.app.base.widget.dialog.Dialogs;
import com.app.base.widget.dialog.mdstyle.MaterialDialog;

import java.io.File;

import kotlin.Unit;
import timber.log.Timber;

/**
 * <pre>
 *     选择文件：https://isming.me/2015/12/21/android-webview-upload-file/
 * </pre>
 *
 */
@SuppressWarnings("unchecked")
class AppWebChromeClient extends WebChromeClient {

    private static final String TAG = AppWebChromeClient.class.getSimpleName();

    private final BaseWebFragment mFragment;
    private AppWebChromeClientCallback mAppWebChromeClientCallback;

    private ValueCallback<Uri> mUriValueCallback;
    private ValueCallback<Uri[]> mUriValueCallbacks;

    private SystemMediaSelector mSystemMediaSelector;

    private static final String IMAGE_TYPE = "image";

    AppWebChromeClient(BaseWebFragment fragment) {
        mFragment = fragment;
        initFileSelector();
    }

    void setAppWebChromeClientCallback(AppWebChromeClientCallback appWebChromeClientCallback) {
        mAppWebChromeClientCallback = appWebChromeClientCallback;
    }

    /**
     * 获得网页的加载进度并显示
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (mAppWebChromeClientCallback != null) {
            mAppWebChromeClientCallback.onProgressChanged(newProgress);
        }
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        Timber.d("onJsAlert() called with: view = [" + view + "], url = [" + url + "], message = [" + message + "], result = [" + result + "]");
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        Timber.d("onJsConfirm() called with: view = [" + view + "], url = [" + url + "], message = [" + message + "], result = [" + result + "]");
        if (mFragment.handleJsCall(view, message, null)) {
            result.confirm();
            return true;
        }
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        Timber.d("onJsPrompt() called with: view = [" + view + "], url = [" + url + "], message = [" + message + "], defaultValue = [" + defaultValue + "], result = [" + result + "]");
        if (mFragment.handleJsCall(view, message, result)) {
            result.confirm();
            return true;
        }
        return super.onJsPrompt(view, url, message, defaultValue, result);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        Timber.d("onReceivedTitle() called with: view = [" + view + "], title = [" + title + "]");
        if (mAppWebChromeClientCallback != null) {
            mAppWebChromeClientCallback.onReceivedTitle(title);
        }
        super.onReceivedTitle(view, title);
    }

    //  Android < 3.0
    public void openFileChooser(ValueCallback<Uri> valueCallback) {
        Timber.d("Android < 3.0 openFileChooser() called with: valueCallback = [" + valueCallback + "]");
        doOpenFileChooser(valueCallback, null);
    }

    //  Android  >= 3.0
    public void openFileChooser(@SuppressWarnings("rawtypes") ValueCallback valueCallback, String acceptType) {
        Timber.d("Android  >= 3.0 openFileChooser() called with: valueCallback = [" + valueCallback + "], acceptType = [" + acceptType + "]");
        doOpenFileChooser(valueCallback, acceptType);
    }

    // Android  >= 4.1
    public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
        Timber.d("Android  >= 4.1 openFileChooser() called with: uploadFile = [" + uploadFile + "], acceptType = [" + acceptType + "], capture = [" + capture + "]");
        doOpenFileChooser(uploadFile, acceptType);
    }

    //Android 5.0+
    @Override
    @SuppressLint("NewApi")
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        Timber.d("Android 5.0+ onShowFileChooser() called with: webView = [" + webView + "], filePathCallback = [" + filePathCallback + "], fileChooserParams = [" + fileChooserParams + "]");
        String acceptType = null;
        if (fileChooserParams != null && fileChooserParams.getAcceptTypes().length > 0) {
            acceptType = fileChooserParams.getAcceptTypes()[0];
        }
        doOpenFileChooserAboveL(filePathCallback, acceptType);
        return true;
    }

    ///////////////////////////////////////////////////////////////////////////
    // File Selector
    ///////////////////////////////////////////////////////////////////////////
    private void doOpenFileChooserAboveL(ValueCallback<Uri[]> filePathCallback, String acceptType) {
        if (mUriValueCallbacks != null) {
            mUriValueCallbacks.onReceiveValue(null);
        }
        mUriValueCallbacks = filePathCallback;
        if (!TextUtils.isEmpty(acceptType) && acceptType.contains(IMAGE_TYPE)) {
            takeImages();
        } else {
            mSystemMediaSelector.takeFile(acceptType);
        }
    }

    private void doOpenFileChooser(ValueCallback<Uri> valueCallback, String acceptType) {
        if (mUriValueCallback != null) {
            valueCallback.onReceiveValue(null);
        }
        mUriValueCallback = valueCallback;
        if (!TextUtils.isEmpty(acceptType) && acceptType.contains(IMAGE_TYPE)) {
            takeImages();
        } else {
            mSystemMediaSelector.takeFile(acceptType);
        }
    }

    private void takeImages() {
        CharSequence[] selection = new CharSequence[2];
        selection[0] = mFragment.getString(R.string.camera);
        selection[1] = mFragment.getString(R.string.album);
        Context context = mFragment.getContext();
        assert context != null;
        Dialogs.showListDialog(context, listDialogBuilder -> {
            listDialogBuilder.setItems(selection);
            listDialogBuilder.setPositiveListener((which, charSequence) -> {
                if (which == 0) {
                    String dcimPictureStorePath = DirectoryManager.createTempPicturePath(".jpeg");
                    mSystemMediaSelector.takePhotoFromCamera(dcimPictureStorePath);
                } else {
                    mSystemMediaSelector.takePhotoFormAlbum();
                }
                return Unit.INSTANCE;
            });
            listDialogBuilder.setNegativeListener(() -> {
                returnEmptyFile();
                return Unit.INSTANCE;
            });
            return Unit.INSTANCE;
        }).setOnDismissListener(dialog -> returnEmptyFile());
    }

    private void initFileSelector() {
        mSystemMediaSelector = new SystemMediaSelector(mFragment, new SystemMediaSelector.MediaSelectorCallback() {
            @Override
            public void onTakeSuccess(String path) {
                returnFile(path);
            }

            @Override
            public void onTakeFail() {
                returnEmptyFile();
            }
        });
    }

    private void returnFile(String path) {
        if (mUriValueCallbacks != null) {
            mUriValueCallbacks.onReceiveValue(new Uri[]{Uri.fromFile(new File(path))});
        }
        if (mUriValueCallback != null) {
            mUriValueCallback.onReceiveValue(Uri.fromFile(new File(path)));
        }
        mUriValueCallbacks = null;
        mUriValueCallback = null;
    }

    private void returnEmptyFile() {
        if (mUriValueCallback != null) {
            mUriValueCallback.onReceiveValue(null);
        }
        if (mUriValueCallbacks != null) {
            mUriValueCallbacks.onReceiveValue(null);
        }
        mUriValueCallbacks = null;
        mUriValueCallback = null;
    }

    ///////////////////////////////////////////////////////////////////////////
    // processResult
    ///////////////////////////////////////////////////////////////////////////

    void onActivityResult(int requestCode, int resultCode, Intent data) {
        mSystemMediaSelector.onActivityResult(requestCode, resultCode, data);
    }

    interface AppWebChromeClientCallback {

        void onReceivedTitle(String title);

        void onProgressChanged(int newProgress);

    }

}

