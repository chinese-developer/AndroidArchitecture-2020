package com.app.base.web;

import android.os.Bundle;

import com.android.base.utils.StringChecker;

public class AppWebFragment extends BaseWebFragment {

    public static AppWebFragment newInstance(String targetUrl) {
        AppWebFragment mallWebFragment = new AppWebFragment();
        mallWebFragment.setArguments(WebUtils.newBundle(targetUrl));
        return mallWebFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            String url = WebUtils.getUrl(getArguments());
            if (!StringChecker.isEmpty(url)) {
                startLoad(url);
            }
        }
    }

}
