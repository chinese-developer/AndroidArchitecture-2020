package com.android.base.interfaces.adapter;

import android.widget.SeekBar;

public interface OnSeekBarChangeListenerAdapter extends SeekBar.OnSeekBarChangeListener {

    @Override
    default void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    default void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    default void onStopTrackingTouch(SeekBar seekBar) {
    }

}
