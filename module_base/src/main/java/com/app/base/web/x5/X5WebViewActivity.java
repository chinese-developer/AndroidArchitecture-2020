package com.app.base.web;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.base.TagsFactory;
import com.android.base.utils.android.compat.StatusBarKt;
import com.android.sdk.x5.base.X5WebChromeClient;
import com.android.sdk.x5.base.X5WebViewClient;
import com.android.sdk.x5.inter.InterWebListener;
import com.android.sdk.x5.inter.VideoWebListener;
import com.android.sdk.x5.utils.X5WebUtils;
import com.android.sdk.x5.view.X5WebView;
import com.android.sdk.x5.widget.WebProgress;
import com.app.base.AppContext;
import com.app.base.R;
import com.app.base.app.AppBaseActivity;
import com.app.base.router.RouterPath;
import com.app.base.utils.BaseTools;
import com.app.base.utils.SaveImageUtils;
import com.app.base.utils.ShareUtils;
import com.tencent.smtt.sdk.WebView;

import cc.shinichi.library.ImagePreview;
import cc.shinichi.library.view.listener.OnOriginProgressListener;
import timber.log.Timber;

/**
 * 使用 tencent x5 内核处理网页
 */
@Route(path = RouterPath.X5WebView.PATH)
public class X5WebViewActivity extends AppBaseActivity {

    @Autowired(name = RouterPath.X5WebView.URL)
    String url;
    @Autowired(name = RouterPath.X5WebView.TITLE)
    String title;
    @Autowired(name = RouterPath.X5WebView.isLoadData)
    boolean isLoadData;
    @Autowired(name = RouterPath.X5WebView.ACTION_AUTO)
    String actionAutoJavaScript;

    private WebProgress progressBar;
    private X5WebView webView;
    private X5WebViewClient x5WebViewClient;
    // 加载视频相关
    private TextView toolbarTitle;
    private X5WebChromeClient x5WebChromeClient;
    private RotateAnimation rotate;
    private ImageView actionMoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_x5);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        initTitle();
        initWebView();
        getDataFromBrowser(getIntent());

        if (isLoadData) {
            webView.loadData(url, "text/html", "UTF-8");
        } else {
            webView.loadUrl(url);
        }
        getDataFromBrowser(getIntent());
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        // 支付宝网页版在打开文章详情之后,无法点击按钮下一步
        webView.resumeTimers();
        // 设置为横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        try {
            if (x5WebChromeClient != null) {
                x5WebChromeClient.removeVideoView();
            }
            if (webView != null) {
                webView.destroy();
                webView = null;
            }
            if (rotate != null) {
                rotate.cancel();
            }
        } catch (Exception e) {
            Timber.tag(TagsFactory.debug).d(e);
        }
        super.onDestroy();
    }

    private void initTitle() {
        progressBar = findViewById(R.id.progress);
        webView = findViewById(R.id.web_view);
        toolbarTitle = findViewById(R.id.tv_title_center);
        progressBar.show();
        progressBar.setColor(Color.parseColor("#FF4081"), Color.parseColor("#303F9F"));
        findViewById(R.id.tv_title_left).setOnClickListener(v -> finish());
        StatusBarKt.setStatusBarColor(this, ContextCompat.getColor(this, R.color.color_d62e1c), null, 1f);
        setTitle(title);
        actionMoreButton = findViewById(R.id.tv_title_right_image);
        actionMoreButton.setVisibility(View.VISIBLE);
        PopupMenu popupMenu = new PopupMenu(new ContextThemeWrapper(AppContext.get(), R.style.Widget_AppCompat_PopupMenu), actionMoreButton, Gravity.END);
        popupMenu.inflate(R.menu.webview_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            setActionMoreItemClickListener(item.getItemId());
            return true;
        });
        actionMoreButton.setOnClickListener(v -> popupMenu.show());
    }

    protected void initWebView() {
        progressBar.setVisibility(View.VISIBLE);
        x5WebChromeClient = webView.getX5WebChromeClient();
        x5WebViewClient = webView.getX5WebViewClient();
        x5WebChromeClient.setVideoWebListener(videoWebListener);
        x5WebViewClient.setWebListener(interWebListener);
        x5WebChromeClient.setWebListener(interWebListener);
        webView.setOnLongClickListener(v -> handleLongImage());
    }

    public void setTitle(String mTitle) {
        toolbarTitle.setText(mTitle);
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        x5WebChromeClient.onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 上传图片之后的回调
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 这个是处理回调逻辑，上传图片成功后的回调
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == X5WebChromeClient.FILE_CHOOSER_RESULT_CODE) {
            x5WebChromeClient.uploadMessage(intent, resultCode);
        } else if (requestCode == X5WebChromeClient.FILE_CHOOSER_RESULT_CODE_5) {
            x5WebChromeClient.uploadMessageForAndroid5(intent, resultCode);
        }
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
                Timber.tag(TagsFactory.debug).d(text);
                String url = scheme + "://" + host + path;
                webView.loadUrl(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 直接通过三方浏览器打开时，回退到首页
     */
    public void handleFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    /**
     * 长按图片事件处理
     */
    private boolean handleLongImage() {
        final WebView.HitTestResult hitTestResult = webView.getHitTestResult();
        // 如果是图片类型或者是带有图片链接的类型
        if (hitTestResult.getType() == com.tencent.smtt.sdk.WebView.HitTestResult.IMAGE_TYPE ||
                hitTestResult.getType() == com.tencent.smtt.sdk.WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            // 弹出保存图片的对话框
            new AlertDialog.Builder(X5WebViewActivity.this)
                    .setItems(new String[]{"查看大图", "保存图片到相册"}, (dialog, which) -> {
                        String picUrl = hitTestResult.getExtra();
                        switch (which) {
                            case 0:
                                showPreviewPhoto(picUrl);
                                break;
                            case 1:
                                SaveImageUtils.saveImageToGallery(X5WebViewActivity.this, picUrl, "");
                                break;
                            default:
                                break;
                        }
                    })
                    .show();
            return true;
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //全屏播放退出全屏
            if (x5WebChromeClient != null && x5WebChromeClient.inCustomView()) {
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

    private void showPreviewPhoto(String url) {
        ImagePreview.getInstance()
                .setContext(this)
                .setImage(url)
                .setFolderName("BigImageView/Download")
                .setZoomTransitionDuration(300)
                .setEnableClickClose(true)
                .setEnableDragClose(true)
                .setShowCloseButton(false)
                .setShowDownButton(true)
                .setShowIndicator(true)
                // 设置失败时的占位图，默认为库中自带R.drawable.load_failed，设置为 0 时不显示
                .setErrorPlaceHolder(R.drawable.load_failed)
                // 设置查看原图时的百分比样式：库中带有一个样式：ImagePreview.PROGRESS_THEME_CIRCLE_TEXT，使用如下：
                .setProgressLayoutId(ImagePreview.PROGRESS_THEME_CIRCLE_TEXT, new OnOriginProgressListener() {
                    @Override
                    public void progress(View parentView, int progress) {
                        // 需要找到进度控件并设置百分比，回调中的parentView即传入的布局的根View，可通过parentView找到控件：
                        ProgressBar progressBar = parentView.findViewById(R.id.sh_progress_view);
                        TextView textView = parentView.findViewById(R.id.sh_progress_text);
                        progressBar.setProgress(progress);
                        String progressText = progress + "%";
                        textView.setText(progressText);
                    }

                    @Override
                    public void finish(View parentView) {

                    }
                }).start();
    }

    private void setActionMoreItemClickListener(int itemId) {
        if (itemId == android.R.id.home) {
            handleFinish();
        } else if (itemId == R.id.actionbar_share) {
            String shareText = webView.getTitle() + webView.getUrl();
            ShareUtils.share(X5WebViewActivity.this, shareText);
        } else if (itemId == R.id.actionbar_cope) {
            if (!TextUtils.isEmpty(webView.getUrl())) {
                BaseTools.copy(webView.getUrl());
                Toast.makeText(this, "复制成功", Toast.LENGTH_LONG).show();
            }
        } else if (itemId == R.id.actionbar_open) {
            BaseTools.openLink(X5WebViewActivity.this, webView.getUrl());
        } else if (itemId == R.id.actionbar_webview_refresh) {// 刷新页面
            if (webView != null) {
                webView.reload();
                if (rotate == null) {
                    rotate = new RotateAnimation(0, 3600,
                            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(2000);
                    rotate.setFillAfter(true);
                    // 减速- 动画插入器
                    rotate.setInterpolator(new DecelerateInterpolator());
                    actionMoreButton.setAnimation(rotate);
                }
                actionMoreButton.startAnimation(rotate);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (webView != null) {
            webView.getSettings().setJavaScriptEnabled(false);
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
            progressBar.hide();
        }

        @Override
        public void onPageFinished() {
            if (!TextUtils.isEmpty(actionAutoJavaScript)) {
                x5WebViewClient.onPageFinished(webView, actionAutoJavaScript);
            }
        }

        @Override
        public void showErrorView(int type) {
            switch (type) {
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
            progressBar.setWebProgress(newProgress);
        }

        @Override
        public void showTitle(String title) {
            toolbarTitle.setText(title);
        }
    };

    @Override
    protected boolean tintStatusBar() {
        return false;
    }
}
