package com.android.sdk.net.provider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public interface ErrorDataAdapter {

    Object createErrorDataStub(Type type, Annotation[] annotations, Retrofit retrofit, ResponseBody value);

    boolean isErrorDataStub(Object object);

}
