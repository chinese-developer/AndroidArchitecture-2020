package com.android.sdk.net.core;

/**
 * 业务数据模型抽象
 *
 */
public interface Response<T> {

    /**
     * 实际返回类型
     */
    T getData();

    /**
     * 响应码
     */
    int getCode();

    /**
     * 相应消息
     */
    String getMessage();

    /**
     * 请求是否成功
     */
    boolean isSuccessful();

}
