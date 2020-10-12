package com.android.sdk.x5.inter;


/**
 * <pre>
 *     desc  : js接口
 * </pre>
 */
public interface WebViewJavascriptBridge {
	
	void callHandler(String handlerName);
	void callHandler(String handlerName, String data);
	void callHandler(String handlerName, String data, CallBackFunction callBack);
	void unregisterHandler(String handlerName);
	void registerHandler(String handlerName, BridgeHandler handler);

}
