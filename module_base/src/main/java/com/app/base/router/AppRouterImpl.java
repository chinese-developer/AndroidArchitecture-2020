package com.app.base.router;

import android.net.Uri;

import com.alibaba.android.arouter.facade.template.IProvider;

import javax.inject.Inject;

public class AppRouterImpl implements AppRouter {

    @Inject public AppRouterImpl() {}

    @Override
    public IPostcard build(String path) {
        return RouterManager.build(path);
    }

    @Override
    public IPostcard build(Uri path) {
        return RouterManager.build(path);
    }

    @Override
    public void inject(Object object) {
        RouterManager.inject(object);
    }

    @Override
    public <T extends IProvider> T navigation(Class<T> iProviderClass) {
        return RouterManager.navigation(iProviderClass);
    }

}
