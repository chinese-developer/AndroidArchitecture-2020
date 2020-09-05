package com.android.base.app.ui;

import androidx.annotation.StringRes;

/**
 * 显示通用的 LoadingDialog 和 Message
 *
 */
public interface LoadingView {

    void showLoadingDialog();

    void showLoadingDialog(boolean cancelable);

    void showLoadingDialog(CharSequence message, boolean cancelable);

    void showLoadingDialog(@StringRes int messageId, boolean cancelable);

    void dismissLoadingDialog();

    void showMessage(CharSequence message);

    void showMessage(@StringRes int messageId);

}
