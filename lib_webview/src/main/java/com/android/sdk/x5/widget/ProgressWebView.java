package com.android.sdk.x5.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.sdk.x5.R;
import com.android.sdk.x5.inter.InterWebListener;
import com.android.sdk.x5.utils.X5WebUtils;
import com.android.sdk.x5.view.X5WebView;

/**
 * <pre>
 *     desc  : 自定义带进度条的webView
 * </pre>
 */
public class ProgressWebView extends FrameLayout {

    private X5WebView webView;
    private String urlTitle;

    public ProgressWebView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public ProgressWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProgressWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        View view = LayoutInflater.from(context).inflate(
                R.layout.view_progress_web_view, this, false);
        webView = view.findViewById(R.id.web_view);
        final WebProgress pbProgress = view.findViewById(R.id.progress);
        pbProgress.show();
        pbProgress.setColor(Color.BLUE);
        webView.getX5WebChromeClient().setWebListener(new InterWebListener() {
            @Override
            public void hindProgressBar() {
                pbProgress.setVisibility(GONE);
            }

            @Override
            public void onPageFinished() {

            }

            @Override
            public void showErrorView(@X5WebUtils.ErrorType int type) {
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
                pbProgress.setWebProgress(newProgress);
            }

            @Override
            public void showTitle(String title) {
                urlTitle = title;
            }
        });
    }

    /**
     * 获取X5WebView对象
     * @return                                  获取X5WebView对象
     */
    public X5WebView getWebView(){
        return webView;
    }

    /**
     * 获取监听到url的标题
     * @return
     */
    public String getTitle(){
        return urlTitle;
    }

}
