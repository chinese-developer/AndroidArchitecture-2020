package com.android.sdk.x5.bridge;

import com.android.sdk.x5.inter.BridgeHandler;
import com.android.sdk.x5.inter.CallBackFunction;

/**
 * <pre>
 *     desc  : 默认的BridgeHandler
 * </pre>
 */
public class DefaultHandler implements BridgeHandler {

	@Override
	public void handler(String data, CallBackFunction function) {
		if(function != null){
			function.onCallBack("DefaultHandler response data");
		}
	}

}
