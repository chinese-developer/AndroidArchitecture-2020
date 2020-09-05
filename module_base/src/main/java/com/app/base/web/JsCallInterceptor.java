package com.app.base.web;

import androidx.annotation.Nullable;

public interface JsCallInterceptor {

    boolean intercept(String method, @Nullable String[] args, @Nullable ResultReceiver resultReceiver);

}
