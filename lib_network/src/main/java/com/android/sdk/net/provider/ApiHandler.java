package com.android.sdk.net.provider;

import com.android.sdk.net.core.Response;

import androidx.annotation.NonNull;

public interface ApiHandler {

    void onApiError(@NonNull Response<?> response);

}
