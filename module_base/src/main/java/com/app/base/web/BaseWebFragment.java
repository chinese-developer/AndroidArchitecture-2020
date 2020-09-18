package com.app.base.web;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JsPromptResult;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.base.app.fragment.BaseFragment;
import com.android.base.utils.android.WebViewUtils;
import com.android.base.utils.android.views.FragmentExKt;
import com.app.base.R;
import com.app.base.utils.ToolbarUtils;
import com.app.base.widget.toolbar.TopBarLayout;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import timber.log.Timber;

public class BaseWebFragment extends BaseFragment {

    protected View mLayout;
    protected WebView mWebView;
    protected TopBarLayout topbar;
    private View mInsetsView;
    private View mErrorLayout;
    private WebProgress mWebProgress;

    protected String mCurrentUrl;

    private AppWebChromeClient mWebChromeClient;
    private JsBridgeHandler mJsBridgeHandler;

    private boolean mTitleIsHidden = false;
    private static final String TITLE_STATUS = "title_status";

    @Override
    public void onAttach(Context context) {
        mJsBridgeHandler = new JsBridgeHandler(this);
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mTitleIsHidden = savedInstanceState.getBoolean(TITLE_STATUS, false);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(TITLE_STATUS, mTitleIsHidden);
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mLayout == null) {
            mLayout = inflater.inflate(getWebLayoutRes(), container, false);
            setupViews();
        }
        return mLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        mWebView.resumeTimers();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WebViewUtils.destroy(mWebView);
    }

    ///////////////////////////////////////////////////////////////////////////
    // Back handle
    ///////////////////////////////////////////////////////////////////////////
    @Override
    public boolean handleBackPress() {
        Timber.d("mWebView.canGoBack() = " + mWebView.canGoBack());
        Timber.d("isVisible = " + isVisible());
        if (isVisible() && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public View getView() {
        View view = super.getView();
        assert view != null;
        return view;
    }

    ///////////////////////////////////////////////////////////////////////////
    // WebView setup
    ///////////////////////////////////////////////////////////////////////////

    protected void setupViews() {
        /*Find view*/
        mWebView = mLayout.findViewById(R.id.web_view);
        mWebProgress = new WebProgress(mLayout.findViewById(R.id.web_pb));
        mErrorLayout = mLayout.findViewById(R.id.layout_error);
        topbar = mLayout.findViewById(R.id.gtlWebRulesTitle);
        mInsetsView = mLayout.findViewById(R.id.ivWebRulesInsets);

        /*Title*/
        topbar.setOnNavigationOnClickListener(v -> FragmentExKt.exitFragment(getActivity(), false));
        ToolbarUtils.setupToolBar(this, mLayout);
        if (!mTitleIsHidden) {
            setTitleVisible(View.VISIBLE);
        }

        /*Error layout*/
        mErrorLayout.setBackgroundColor(Color.WHITE);

        /*WebView*/
        DefaultWebSetting defaultWebSetting = new DefaultWebSetting(mWebView);
        defaultWebSetting.setupBasic();
        defaultWebSetting.setupCache();

        mWebChromeClient = new AppWebChromeClient(this);
        mWebChromeClient.setAppWebChromeClientCallback(appWebChromeClientCallback);
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mAppWebViewClient);
    }

    private AppWebChromeClient.AppWebChromeClientCallback appWebChromeClientCallback = new AppWebChromeClient.AppWebChromeClientCallback() {
        @Override
        public void onReceivedTitle(String title) {
            processReceivedTitle(title);
        }

        @Override
        public void onProgressChanged(int newProgress) {
            progressProgress(newProgress);
        }
    };

    private WebViewClient mAppWebViewClient = new AppWebViewClient() {

        @Override
        boolean appShouldOverrideUrlLoading(WebView view, String url) {
            return processShouldOverrideUrlLoading(view, url);
        }

        @Override
        void onAppPageError(String url) {
            onLoadError(url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            onLoadFinished(url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            onLoadStart(url);
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mWebChromeClient.onActivityResult(requestCode, resultCode, data);
    }

    ///////////////////////////////////////////////////////////////////////////
    // extends
    ///////////////////////////////////////////////////////////////////////////
    protected void onLoadFinished(@SuppressWarnings("unused") String url) {
    }

    protected void progressProgress(int newProgress) {
        mWebProgress.onProgress(newProgress);
    }

    protected void processReceivedTitle(String title) {
        if (autoHandleTitle()) {
            topbar.setTitle(title);
        }
    }

    protected void onLoadStart(@SuppressWarnings("unused") String url) {
        mErrorLayout.setVisibility(View.GONE);
    }

    protected void onLoadError(@SuppressWarnings("unused") String url) {
        mErrorLayout.setVisibility(View.VISIBLE);
        mErrorLayout.findViewById(R.id.base_retry_btn).setOnClickListener(view1 -> mWebView.loadUrl(mCurrentUrl));
    }

    public void startLoad(String url) {
        Timber.d("firstLoadUrl() called with: currentUrl = [" + url + "]");
        loadUrl(url);
    }

    @SuppressWarnings("unused")
    protected void startLoad(String url, Map<String, String> header) {
        Timber.d("startLoadUrl() called with: url = [" + url + "], header = [" + header + "]");
        mCurrentUrl = url;
        mWebView.loadUrl(mCurrentUrl, header);
    }

    @SuppressWarnings("unused")
    protected void startPostLoad(String url, String postData) {
        Timber.d("startLoadUrl() called with: url = [" + url + "], postData = [" + postData + "]");
        mCurrentUrl = url;
        try {
            mWebView.postUrl(url, postData.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void loadUrl(String url) {
        mCurrentUrl = url;
        mWebView.loadUrl(mCurrentUrl);
    }

    protected boolean autoHandleTitle() {
        return true;
    }

    protected boolean processShouldOverrideUrlLoading(@SuppressWarnings("unused") WebView webView, String url) {
        if (URLUtil.isNetworkUrl(url)) {
            loadUrl(url);
        } else {
            processAppUrl(url);
        }
        return true;
    }

    protected void processAppUrl(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    protected int getWebLayoutRes() {
        return R.layout.app_base_web_fragment;
    }

    boolean handleJsCall(@SuppressWarnings("unused") WebView view, String message, JsPromptResult jsPromptResult) {
        return mJsBridgeHandler.handleJsCall(message, jsPromptResult);
    }

    @SuppressWarnings("unused")
    public final JsBridgeHandler getJsBridgeHandler() {
        return mJsBridgeHandler;
    }

    protected final void refresh() {
        loadUrl(mCurrentUrl);
    }

    public void exit() {
        FragmentExKt.exitFragment(getActivity(), false);
    }

    public final void setHeaderVisible(boolean showHeader) {
        mTitleIsHidden = !showHeader;
        if (topbar != null) {
            if (showHeader) {
                setTitleVisible(View.VISIBLE);
            } else {
                setTitleVisible(View.GONE);
            }
        }
    }

    protected final void setTitleVisible(int visible) {
        mInsetsView.setVisibility(visible);
        topbar.setVisibility(visible);
    }

}
