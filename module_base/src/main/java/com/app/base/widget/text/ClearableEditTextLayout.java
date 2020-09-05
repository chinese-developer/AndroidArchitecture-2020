package com.app.base.widget.text;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.android.base.utils.android.UnitConverter;
import com.app.base.R;

public class ClearableEditTextLayout extends FrameLayout {

    private EditText mEditText;
    private View mClearView;

    private static final int EDIT_PADDING_END = 50;
    private int mOriginPaddingRight;
    private int mCompatBtnPaddingEnd;

    public ClearableEditTextLayout(Context context) {
        this(context, null);
    }

    public ClearableEditTextLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    public ClearableEditTextLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.widget_text_input_password_icon_padding, this);
        mCompatBtnPaddingEnd = UnitConverter.dpToPx(EDIT_PADDING_END);
        mClearView = findViewById(R.id.widgetBtnClear);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            // EditText's underline
            LayoutParams flp = new LayoutParams(params);
            flp.gravity = Gravity.CENTER_VERTICAL | (flp.gravity & ~Gravity.VERTICAL_GRAVITY_MASK);
            mEditText = (EditText) child;
            mOriginPaddingRight = mEditText.getPaddingRight();
            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    boolean empty = TextUtils.isEmpty(s);
                    mClearView.setVisibility(empty ? INVISIBLE : VISIBLE);
                    mEditText.setPadding(
                            mEditText.getPaddingLeft(),
                            mEditText.getPaddingTop(),
                            empty ? mOriginPaddingRight : mCompatBtnPaddingEnd,
                            mEditText.getPaddingBottom()
                    );
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            mClearView.setOnClickListener(v -> mEditText.setText(""));

            super.addView(child, index, flp);

            mClearView.bringToFront();
        } else {
            super.addView(child, index, params);
        }
    }

    public EditText getEditText() {
        return mEditText;
    }

}
