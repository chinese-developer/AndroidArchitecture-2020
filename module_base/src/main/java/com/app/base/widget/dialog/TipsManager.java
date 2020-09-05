package com.app.base.widget.dialog;

import android.graphics.Color;

import com.app.base.R;
import com.google.android.material.snackbar.Snackbar;
import android.view.Gravity;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;

public class TipsManager {

    public static void showMessage(CharSequence message) {
        ToastUtils.setBgResource(R.drawable.shape_common_toast);
        ToastUtils.setMsgTextSize(16);
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
        ToastUtils.setMsgColor(Color.WHITE);
        ToastUtils.showShort(message);
    }

    public static void showLongMessage(CharSequence message) {
        ToastUtils.setBgResource(R.drawable.shape_common_toast);
        ToastUtils.setMsgTextSize(16);
        ToastUtils.setGravity(Gravity.CENTER, 0, 0);
        ToastUtils.setMsgColor(Color.WHITE);
        ToastUtils.showLong(message);
    }

    public static void showMessage(View anchor, CharSequence message) {
        Snackbar.make(anchor, message, Snackbar.LENGTH_SHORT).show();
    }

    public static void showMessage(View anchor, CharSequence message, CharSequence actionText, View.OnClickListener onClickListener) {
        Snackbar.make(anchor, message, Snackbar.LENGTH_SHORT)
                .setAction(actionText, onClickListener)
                .show();
    }

}
