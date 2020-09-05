package com.app.base.widget.dialog;

import android.content.Context;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.base.R;

class TipsDialog extends AppCompatDialog {

    private TextView mMessageTv;

    TipsDialog(Context context, @DrawableRes int drawableRes) {
        super(context);
        Window window = getWindow();
        assert window != null;
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.windowAnimations = R.style.Style_Anim_Fade_In;
        setView(drawableRes);
    }

    private void setView(@DrawableRes int drawableRes) {
        setContentView(R.layout.dialog_tips);
        mMessageTv = findViewById(R.id.dialog_tips_tv_title);
        ImageView imageView = findViewById(R.id.dialog_tips_iv);
        assert imageView != null;
        imageView.setImageResource(drawableRes);
    }

    public void setMessage(CharSequence message) {
        if (!TextUtils.isEmpty(message)) {
            mMessageTv.setText(message);
        }
    }

    public void setMessage(@StringRes int messageId) {
        if (messageId != 0) {
            mMessageTv.setText(messageId);
        }
    }

}
