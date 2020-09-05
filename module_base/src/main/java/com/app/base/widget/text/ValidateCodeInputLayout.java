package com.app.base.widget.text;

import android.content.Context;
import android.graphics.Paint;

import com.app.base.R;
import com.google.android.material.textfield.TextInputLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.android.base.interfaces.adapter.TextWatcherAdapter;
import com.android.base.utils.android.UnitConverter;

import org.jetbrains.annotations.NotNull;

public class ValidateCodeInputLayout extends RelativeLayout {

    private TextInputLayout mTextInputLayout;
    private EditText mInputEditText;
    private CounterButton mCounterButton;
    private ImageButton mClearButton;

    private int mCounterButtonMargin;
    private int[] mPosition;
    private Paint.FontMetricsInt mFontMetricsInt;

    public ValidateCodeInputLayout(Context context) {
        this(context, null);
    }

    public ValidateCodeInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ValidateCodeInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.widget_validate_code_input, this);

        mTextInputLayout = findViewById(R.id.vcilTilValidateCode);
        mInputEditText = mTextInputLayout.getEditText();
        mCounterButton = findViewById(R.id.vcilCounterBtn);
        mClearButton = findViewById(R.id.vcilClearBtn);

        mCounterButtonMargin = UnitConverter.dpToPx(15);
        mPosition = new int[2];
        mFontMetricsInt = new Paint.FontMetricsInt();
        setupViews();
    }

    private void setupViews() {
        mCounterButton.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setEditTextPadding();
            }
        });

        post(() -> {
            setEditTextPadding();
            setCounterBtnPosition();
            setTextWatcher();
        });

    }

    private void setTextWatcher() {
        if (TextUtils.isEmpty(mInputEditText.getText().toString())) {
            mClearButton.setVisibility(INVISIBLE);
        } else {
            mClearButton.setVisibility(VISIBLE);
        }
        mClearButton.setOnClickListener(v -> mInputEditText.setText(""));
        mInputEditText.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mClearButton.setVisibility(INVISIBLE);
                } else {
                    mClearButton.setVisibility(VISIBLE);
                }
            }
        });
    }

    private void setCounterBtnPosition() {
        mInputEditText.setPadding(mInputEditText.getPaddingLeft(), mInputEditText.getPaddingTop(), mInputEditText.getPaddingRight(), mInputEditText.getPaddingBottom() + mCounterButton.getMeasuredHeight() / 2);
        mInputEditText.getLocationInWindow(mPosition);
        int mInputEditTextY = mPosition[1];

        getLocationInWindow(mPosition);
        int thisY = mPosition[1];

        int hDistance = (mInputEditTextY - thisY);
        LayoutParams layoutParams = (LayoutParams) mCounterButton.getLayoutParams();
        mInputEditText.getPaint().getFontMetricsInt(mFontMetricsInt);
        layoutParams.topMargin = (int) (hDistance + (mFontMetricsInt.descent - mFontMetricsInt.ascent) / 2F);

        LayoutParams clearButtonLayoutParams = (LayoutParams) mClearButton.getLayoutParams();
        clearButtonLayoutParams.topMargin = (int) ((layoutParams.topMargin + mCounterButton.getMeasuredHeight() - mCounterButton.getMeasuredHeight()) / 2F);
    }

    private void setEditTextPadding() {
        int width = mCounterButton.getWidth();
        mInputEditText.setPadding(mInputEditText.getPaddingLeft(), mInputEditText.getPaddingTop(), width + mCounterButtonMargin, mInputEditText.getPaddingBottom());
    }

    public void setError(CharSequence error) {
        mTextInputLayout.setError(error);
    }

    public EditText getEditText() {
        return mInputEditText;
    }

    public TextInputLayout getTextInputLayout() {
        return mTextInputLayout;
    }

    @NotNull
    public String code() {
        return mInputEditText.getText().toString();
    }

    public void setHint(int hintRes) {
        mTextInputLayout.setHint(getResources().getString(hintRes));
    }

    public void startCounter() {
        mCounterButton.startCounter();
    }

    public void setCounterEnable(boolean enable) {
        mCounterButton.setEnabled(enable);
    }

    public void setOnCounterClickListener(OnClickListener onCounterClickListener) {
        mCounterButton.setOnClickListener(onCounterClickListener);
    }

    public void clearCounter() {
        mCounterButton.clearCounter();
    }

}
