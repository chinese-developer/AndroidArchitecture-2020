package com.android.base.animation;

import android.app.Application;
import android.graphics.drawable.Icon;

import androidx.annotation.DrawableRes;

import com.android.base.R;

public class AnimatorDurationScaler {

    private AnimatorDurationScaler() {
    }

    public static @DrawableRes
    int getIcon(float scale) {
        if (scale <= 0f) {
            return R.drawable.ic_animator_duration_off;
        } else if (scale <= 0.5f) {
            return R.drawable.ic_animator_duration_half_x;
        } else if (scale <= 1f) {
            return R.drawable.ic_animator_duration_1x;
        } else if (scale <= 1.5f) {
            return R.drawable.ic_animator_duration_1_5x;
        } else if (scale <= 2f) {
            return R.drawable.ic_animator_duration_2x;
        } else if (scale <= 5f) {
            return R.drawable.ic_animator_duration_5x;
        } else if (scale <= 10f) {
            return R.drawable.ic_animator_duration_10x;
        }
        return R.drawable.ic_animator_duration;
    }

    public static Icon getIcon(Application app) {
        return Icon.createWithResource(app, getIcon(5f));
    }

}