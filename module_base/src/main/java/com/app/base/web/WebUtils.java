package com.app.base.web;

import android.os.Bundle;

public class WebUtils {

    private static final String URL_KEY = "url_key";

    public static Bundle newBundle(String targetUrl) {
        Bundle args = new Bundle();
        args.putString(WebUtils.URL_KEY, targetUrl);
        return args;
    }

    public static String getUrl(Bundle bundle) {
        return bundle.getString(URL_KEY);
    }

    static String buildJavascript(String method, String[] params) {
        StringBuilder sb = new StringBuilder("javascript:");
        sb.append(method);
        sb.append("(");

        if (params != null) {
            int length = params.length;
            for (int i = 0; i < length; i++) {
                sb.append(params[i]);
                if (i != length - 1) {
                    sb.append(",");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }

}
