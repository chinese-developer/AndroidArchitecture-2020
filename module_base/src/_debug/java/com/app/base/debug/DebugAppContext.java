package com.app.base.debug;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.android.base.utils.android.cache.SpCache;

import java.util.UUID;

/**
 * 调试模式下的 AppContext ,模拟一个device sn 上报。
 */
public class DebugAppContext {

    @SuppressLint("CheckResult")
    private void syncUserDataIfLogined() {

    }

    private String createFakeSN() {
        SpCache spCache = new SpCache("fake_app_token");
        final String fake_sn_key = "FAKE_SN_KEY";
        String fake_sn = spCache.getString(fake_sn_key, "");
        if (TextUtils.isEmpty(fake_sn)) {
            String uuid = UUID.randomUUID().toString();
            fake_sn = uuid.substring(0, uuid.length() / 2);
            spCache.putString(fake_sn_key, fake_sn);
        }
        return fake_sn;
    }

}
