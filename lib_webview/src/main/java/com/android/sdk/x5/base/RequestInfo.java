package com.android.sdk.x5.base;

import java.io.Serializable;
import java.util.Map;

/**
 * 自定义RequestInfo实体类
 */
public class RequestInfo implements Serializable {

    public String url;

    public Map<String, String> headers;

    public RequestInfo(String url, Map<String, String> additionalHttpHeaders) {
        this.url = url;
        this.headers = additionalHttpHeaders;
    }

}
