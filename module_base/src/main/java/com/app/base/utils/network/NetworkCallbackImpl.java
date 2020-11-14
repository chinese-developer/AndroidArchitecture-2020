package com.app.base.utils.network;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.android.base.TagsFactory;

import timber.log.Timber;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        Timber.tag(TagsFactory.debug).d("网络已连接");
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        Timber.tag(TagsFactory.debug).d("网络已断开");
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);
        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Timber.tag(TagsFactory.debug).d("网络发生变更，类型为：wifi");
            } else {
                Timber.tag(TagsFactory.debug).d("网络发生变更，类型为：4G");
            }
        }

    }
}
