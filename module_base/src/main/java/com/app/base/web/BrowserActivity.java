package com.app.base.web;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.URLUtil;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.android.base.utils.android.compat.StatusBarUtil;
import com.app.base.R;
import com.app.base.app.AppBaseActivity;
import com.app.base.data.DataConfig;
import com.app.base.router.RouterPath;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * 应用内浏览器
 */
@Route(path = RouterPath.Browser.PATH)
@AndroidEntryPoint
public class BrowserActivity extends AppBaseActivity {

    @Autowired(name = RouterPath.Browser.URL_KEY)
    String targetUrl;
    @Autowired(name = RouterPath.Browser.FRAGMENT_KEY)
    String fragmentClass;
    @Autowired(name = RouterPath.Browser.SHOW_HEADER)
    Boolean showHeader = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        setContentView(R.layout.app_base_web_activity);
        String url = targetUrl;
        if (!URLUtil.isValidUrl(targetUrl)) {
            url = DataConfig.getInstance().baseWebUrl() + targetUrl;
        }

        if (savedInstanceState == null) {
            if (!TextUtils.isEmpty(fragmentClass)) {
                Fragment instantiate = getSupportFragmentManager().getFragmentFactory().instantiate(getClassLoader(), fragmentClass);
                instantiate.setArguments(WebUtils.newBundle(url));
                getSupportFragmentManager().beginTransaction().replace(R.id.common_container_id, instantiate).commit();
            } else {
                AppWebFragment webFragment = AppWebFragment.newInstance(url);
                webFragment.setHeaderVisible(showHeader);
                getSupportFragmentManager().beginTransaction().replace(R.id.common_container_id, webFragment).commit();
            }
        }

        StatusBarUtil.setTranslucentSystemUi(this, true, false);
    }

    @Override
    protected boolean tintStatusBar() {
        return false;
    }

}
