package com.android.sdk.x5.inter;

import androidx.annotation.IntRange;

import com.android.sdk.x5.utils.X5WebUtils;

/**
 * <pre>
 *     desc  : web的接口回调，包括常见状态页面切换，进度条变化等
 * </pre>
 */
public interface InterWebListener {

    /**
     * 隐藏进度条
     */
    void hindProgressBar();

    /**
     * 页面加载完成
     */
    void onPageFinished();

    /**
     * 展示异常页面
     * @param type                           异常类型
     */
    void showErrorView(@X5WebUtils.ErrorType int type);

    /**
     * 进度条变化时调用，这里添加注解限定符，必须是在0到100之间
     * @param newProgress                   进度0-100
     */
    void startProgress(@IntRange(from = 0, to = 100) int newProgress);

    /**
     * 获取加载网页的标题
     * @param title                         title标题
     */
    void showTitle(String title);
}
