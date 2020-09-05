package com.app.base.widget.toolbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import com.android.base.utils.android.ViewUtils;
import com.android.base.utils.android.views.FragmentExKt;
import com.app.base.R;

public class TopBarLayout extends LinearLayout {

    private static final String TAG = TopBarLayout.class.getSimpleName();

    private String mTitle;
    private Toolbar mToolbar;
    private int mMenuResId;
    private Drawable mNavigationIcon;
    private boolean mShowCuttingLime;
    private OnClickListener onNavigationOnClickListener;
    private static final int INVALIDATE_ID = -1;

    public TopBarLayout(@NonNull Context context) {
        this(context, null);
    }

    public TopBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBarLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //get resource
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TopBarLayout);
        mTitle = typedArray.getString(R.styleable.TopBarLayout_tb_title);
        mMenuResId = typedArray.getResourceId(R.styleable.TopBarLayout_tb_menu_id, INVALIDATE_ID);
        mShowCuttingLime = typedArray.getBoolean(R.styleable.TopBarLayout_tb_show_cutting_line, false);
        mNavigationIcon = typedArray.getDrawable(R.styleable.TopBarLayout_tb_navigation_icon);
        typedArray.recycle();
        //setup views
        setOrientation(VERTICAL);
        inflate(context, R.layout.widget_topbar_layout, this);
        setupViews();
    }

    private void setupViews() {
        mToolbar = findViewById(R.id.common_toolbar);
        View cuttingLineView = findViewById(R.id.widgetTitleCuttingLine);
        setupToolbar();
        cuttingLineView.setVisibility(mShowCuttingLime ? View.VISIBLE : View.GONE);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public void setTitle(String title) {
        mTitle = title;
        mToolbar.setTitle(mTitle);
    }

    private void setupToolbar() {
        //nav
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setContentInsetStartWithNavigation(0);
        mToolbar.setNavigationOnClickListener(this::onNavigationOnClick);
        if (getBackground() != null) {
            mToolbar.setBackground(getBackground());
        }
        //title
        mToolbar.setTitle(mTitle);
        //icon
        if (mNavigationIcon != null) {
            mToolbar.setNavigationIcon(mNavigationIcon);
        }
        //menu
        if (mMenuResId != INVALIDATE_ID) {
            mToolbar.inflateMenu(mMenuResId);
        }
    }

    public Menu getMenu() {
        return mToolbar.getMenu();
    }

    public void setOnNavigationOnClickListener(OnClickListener onNavigationOnClickListener) {
        this.onNavigationOnClickListener = onNavigationOnClickListener;
    }

    private void onNavigationOnClick(View v) {
        if (onNavigationOnClickListener != null) {
            onNavigationOnClickListener.onClick(v);
            return;
        }
        FragmentActivity realContext = ViewUtils.getRealContext(this);
        if (realContext != null) {
            FragmentExKt.exitFragment(realContext, false);
        } else {
            Log.w(TAG, "perform onNavigationOnClick --> fragmentBack, but real context can not be found");
        }
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        if (mToolbar != null) {
            mToolbar.setBackground(background);
        }
    }

}
