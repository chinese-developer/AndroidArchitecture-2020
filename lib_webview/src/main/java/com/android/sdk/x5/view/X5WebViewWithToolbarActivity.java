package com.android.sdk.x5.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.sdk.x5.R;
import com.android.sdk.x5.base.X5WebChromeClient;
import com.android.sdk.x5.base.X5WebViewClient;
import com.android.sdk.x5.inter.InterWebListener;
import com.android.sdk.x5.inter.VideoWebListener;
import com.android.sdk.x5.tools.AndroidBug5497Workaround;
import com.android.sdk.x5.utils.X5WebUtils;
import com.android.sdk.x5.widget.WebProgress;

/**
 * 应用被作为第三方浏览器打开的activity
 */
public class X5WebViewWithToolbarActivity extends AppCompatActivity {

    private X5WebView webView;
    private WebProgress pb;
    private TextView tvTitle;
    private Toolbar mTitleToolBar;
    private X5WebChromeClient x5WebChromeClient;
    private X5WebViewClient x5WebViewClient;
    private AndroidBug5497Workaround workaround;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //全屏播放退出全屏
            if (x5WebChromeClient!=null && x5WebChromeClient.inCustomView()) {
                x5WebChromeClient.hideCustomView();
                return true;
                //返回网页上一页
            } else if (webView.pageCanGoBack()) {
                //退出网页
                return webView.pageGoBack();
            } else {
                handleFinish();
            }
        }
        return false;
    }

    public void handleFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        try {
            if (x5WebChromeClient!=null){
                x5WebChromeClient.removeVideoView();
            }
            webView.destroy();
        } catch (Exception e) {
            Log.e("X5WebViewActivity", e.getMessage());
        }
        if (workaround!=null){
            workaround.onDestroy();
        }
        super.onDestroy();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initFindViewById();
        initToolBar();
        initWebView();
        // 处理 作为三方浏览器打开传过来的值
        getDataFromBrowser(getIntent());
        AndroidBug5497Workaround workaround = new AndroidBug5497Workaround(this);
    }

    private void initFindViewById() {
        webView = findViewById(R.id.web_view);
        pb = findViewById(R.id.progress);
        tvTitle = findViewById(R.id.tv_title);
        mTitleToolBar = findViewById(R.id.title_tool_bar);
        //显示进度条
        pb.show();
        //设置进度条过度颜色
        pb.setColor(Color.BLUE,Color.RED);
        //设置单色进度条
        pb.setColor(Color.BLUE);
    }

    private void initToolBar() {
        setSupportActionBar(mTitleToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mTitleToolBar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.icon_more));
        tvTitle.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvTitle.setSelected(true);
            }
        }, 1000);
        tvTitle.setText("加载中……");
    }

    private void initWebView() {
        x5WebChromeClient = webView.getX5WebChromeClient();
        x5WebViewClient = webView.getX5WebViewClient();
        x5WebChromeClient.setVideoWebListener(videoWebListener);
        x5WebViewClient.setWebListener(interWebListener);
        x5WebChromeClient.setWebListener(interWebListener);
    }

    /**
     * 使用singleTask启动模式的Activity在系统中只会存在一个实例。
     * 如果这个实例已经存在，intent就会通过onNewIntent传递到这个Activity。
     * 否则新的Activity实例被创建。
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getDataFromBrowser(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //这个是处理回调逻辑，上传图片成功后的回调
        if (requestCode == X5WebChromeClient.FILE_CHOOSER_RESULT_CODE) {
            x5WebChromeClient.uploadMessage(intent, resultCode);
        } else if (requestCode == X5WebChromeClient.FILE_CHOOSER_RESULT_CODE_5) {
            x5WebChromeClient.uploadMessageForAndroid5(intent, resultCode);
        }
    }


    /**
     * 作为三方浏览器打开传过来的值
     * Scheme: https
     * host: www.jianshu.com
     * path: /p/1cbaf784c29c
     * url = scheme + "://" + host + path;
     */
    private void getDataFromBrowser(Intent intent) {
        Uri data = intent.getData();
        if (data != null) {
            try {
                String scheme = data.getScheme();
                String host = data.getHost();
                String path = data.getPath();
                String text = "Scheme: " + scheme + "\n" + "host: " + host + "\n" + "path: " + path;
                Log.e("data", text);
                String url = scheme + "://" + host + path;
                webView.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private VideoWebListener videoWebListener = new VideoWebListener() {
        @Override
        public void showVideoFullView() {
            //视频全频播放时监听
        }

        @Override
        public void hindVideoFullView() {
            //隐藏全频播放，也就是正常播放视频
        }

        @Override
        public void showWebView() {
            //显示webView
        }

        @Override
        public void hindWebView() {
            //隐藏webView
        }
    };

    private InterWebListener interWebListener = new InterWebListener() {
        @Override
        public void hindProgressBar() {
            //进度完成后消失
            pb.hide();
        }

        @Override
        public void onPageFinished() {

        }

        @Override
        public void showErrorView(int type) {
            switch (type){
                //没有网络
                case X5WebUtils.ErrorMode.NO_NET:
                    break;
                //404，网页无法打开
                case X5WebUtils.ErrorMode.STATE_404:
                    break;
                //onReceivedError，请求网络出现error
                case X5WebUtils.ErrorMode.RECEIVED_ERROR:
                    break;
                //在加载资源时通知主机应用程序发生SSL错误
                case X5WebUtils.ErrorMode.SSL_ERROR:
                    break;
                default:
                    break;
            }
        }

        @Override
        public void startProgress(int newProgress) {
            //为单独处理WebView进度条
            pb.setWebProgress(newProgress);
        }

        @Override
        public void showTitle(String title) {
            tvTitle.setText(title);
        }
    };

}