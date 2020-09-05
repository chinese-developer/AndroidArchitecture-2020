package com.app.base.web;

import android.os.Build;
import androidx.annotation.RequiresApi;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

abstract class AppWebViewClient extends WebViewClient {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = request.getUrl().toString();
        return appShouldOverrideUrlLoading(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return appShouldOverrideUrlLoading(view, url);
    }

    abstract boolean appShouldOverrideUrlLoading(WebView view, String url);

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        onAppPageError(request.getUrl().toString());
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        onAppPageError(failingUrl);
    }

    /**
     * 加载页面的服务器出现错误时（如404）回调。
     */
    abstract void onAppPageError(String s);

}
