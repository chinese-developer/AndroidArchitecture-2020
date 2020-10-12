package com.android.sdk.x5.tools;

/**
 * <pre>
 *     desc   : 自定义异常
 * </pre>
 */
public class WebViewException extends RuntimeException {

    private int mCode = 0;

    public WebViewException(int code, String msg) {
        super(msg);
        mCode = code;
    }

    public WebViewException(String msg) {
        super(msg);
    }

    public WebViewException(Throwable throwable) {
        super(throwable);
        if (throwable instanceof WebViewException) {
            mCode = ((WebViewException) throwable).getCode();
        }
    }

    public int getCode() {
        return mCode;
    }

    public String getMsg() {
        return getMessage();
    }
}
