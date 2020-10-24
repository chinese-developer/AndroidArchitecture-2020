package com.android.base.animation;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;

import com.android.base.R;

/**
 * A helper class for working with the system animator duration scale.
 * <p>
 * Note that this requires a system level permission, so consumers <b>must</b> run this
 * <code>adb</code> command to use.
 * <p>
 * <code>adb shell pm grant uk.co.nickbutcher.animatordurationtile android.permission.WRITE_SECURE_SETTINGS</code>
 */
public class AnimatorDurationScaler {

    private static final String TAG = "AnimatorDurationScaler";

    private AnimatorDurationScaler() { }

    static @DrawableRes int getIcon(float scale) {
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

    public static float getAnimatorScale(@NonNull ContentResolver contentResolver) {
        float scale = 1f;
        try {
            scale = Settings.Global.getFloat(contentResolver,
                    Settings.Global.ANIMATOR_DURATION_SCALE);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Could not read Animator Duration Scale setting", e);
        }
        return scale;
    }

    public static boolean setAnimatorScale(
            @NonNull Context context,
            @FloatRange(from = 0.0, to = 10.0) float scale) {
        try {
            Settings.Global.putFloat(
                    context.getContentResolver(), Settings.Global.ANIMATOR_DURATION_SCALE, scale);
            return true;
        } catch (SecurityException se) {
            Toast.makeText(context.getApplicationContext(), "Need to grant permission. Please run:\\n\\n\n" +
                    "        adb shell pm grant uk.co.nickbutcher.animatordurationtile\n" +
                    "        android.permission.WRITE_SECURE_SETTINGS", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}