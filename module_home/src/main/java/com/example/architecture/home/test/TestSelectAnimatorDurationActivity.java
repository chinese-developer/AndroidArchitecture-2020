package com.example.architecture.home.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.CheckedTextView;

import androidx.annotation.FloatRange;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.android.base.animation.AnimatorDurationScaler;
import com.example.architecture.home.R;


public class TestSelectAnimatorDurationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_select_scale_dialog);
        float scale = AnimatorDurationScaler.getAnimatorScale(getContentResolver());
        ((Checkable) findViewById(getScaleItemId(scale))).setChecked(true);
    }

    public void scaleClick(View v) {
        uncheckAllChildren((ViewGroup) v.getParent());
        ((CheckedTextView) v).setChecked(true);
        AnimatorDurationScaler.setAnimatorScale(this, getScale(v.getId()));
        finishAfterTransition();
    }

    public void cancel(View v) {
        finishAfterTransition();
    }

    private float getScale(@IdRes int id) {
        if (id == R.id.scale_off) {
            return 0f;
        } else if (id == R.id.scale_0_5) {
            return 0.5f;
        } else if (id == R.id.scale_1_5) {
            return 1.5f;
        } else if (id == R.id.scale_2) {
            return 2f;
        } else if (id == R.id.scale_5) {
            return 5f;
        } else if (id == R.id.scale_10) {
            return 10f;
        }
        return 1f;
    }

    private @IdRes
    int getScaleItemId(@FloatRange(from = 0.0, to = 10.0) float scale) {
        if (scale <= 0f) {
            return R.id.scale_off;
        } else if (scale <= 0.5f) {
            return R.id.scale_0_5;
        } else if (scale <= 1f) {
            return R.id.scale_1;
        } else if (scale <= 1.5f) {
            return R.id.scale_1_5;
        } else if (scale <= 2f) {
            return R.id.scale_2;
        } else if (scale <= 5f) {
            return R.id.scale_5;
        } else {
            return R.id.scale_10;
        }
    }

    private void uncheckAllChildren(@NonNull ViewGroup vg) {
        for (int i = vg.getChildCount() - 1; i >= 0; i--) {
            View child = vg.getChildAt(i);
            if (child instanceof Checkable) {
                ((Checkable) child).setChecked(false);
            }
        }
    }

}
