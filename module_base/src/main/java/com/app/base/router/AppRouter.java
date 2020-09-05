package com.app.base.router;

import android.net.Uri;

import com.alibaba.android.arouter.facade.template.IProvider;

public interface AppRouter {

    IPostcard build(String path);

    IPostcard build(Uri path);

    void inject(Object object);

    <T extends IProvider> T navigation(Class<T> iProviderClass);

}
