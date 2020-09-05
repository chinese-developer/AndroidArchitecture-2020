package com.android.base.receiver;

import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;

public enum NetworkState {

    STATE_WIFI,
    STATE_GPRS,
    STATE_NONE;

    private static final BehaviorProcessor<NetworkState> PROCESSOR = BehaviorProcessor.create();

    public static Flowable<NetworkState> observableState() {
        return PROCESSOR;
    }

    /**
     * 检测当前是否有网络连接
     * @return true 有 false 没有联网
     */
    public boolean isConnected() {
        return this != STATE_NONE;
    }

    static void notify(NetworkState networkState) {
        PROCESSOR.onNext(networkState);
    }


}
