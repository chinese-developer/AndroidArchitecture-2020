package com.app.base.data;

import android.app.Application;

import com.android.base.utils.android.cache.SpCache;
import com.android.sdk.net.NetContext;
import com.app.base.AppContext;
import com.app.base.BuildConfig;
import com.app.base.data.app.AppDataSource;
import com.blankj.utilcode.util.NetworkUtils;

import timber.log.Timber;

import static com.app.base.data.NetProviderImplKt.newApiHandler;
import static com.app.base.data.NetProviderImplKt.newErrorDataAdapter;
import static com.app.base.data.NetProviderImplKt.newOkHttpConfig;
import static com.app.base.data.URLProviderKt.addAllHost;
import static com.app.base.data.URLProviderKt.addReleaseHost;
import static com.app.base.data.URLProviderKt.getBaseUrl;
import static com.app.base.data.URLProviderKt.getBaseWebUrl;
import static com.app.base.data.URLProviderKt.getEnvironment;
import static com.app.base.data.api.HttpResultKt.isLoginExpired;

/**
 * Data层配置，抽象为DataContext
 */
public class DataContext {

    private static DataContext sDataContext;

    private static final String NAME = "data_context";
    private static final String HOST_KEY = "host_key";

    /*环境配置*/
    public static final int BUILD_DEV = 1;
    public static final int BUILD_TEST = 2;
    public static final int BUILD_RELEASE = 3;

    /*do not modify it*/
    static final String DEV = "dev";
    static final String TEST = "test";
    static final String RELEASE = "release";

    public synchronized static void init(Application application) {
        if (sDataContext != null) {
            throw new IllegalStateException("DataContext was  initialized");
        }
        sDataContext = new DataContext(application);
    }

    public static DataContext getInstance() {
        if (sDataContext == null) {
            throw new IllegalStateException("DataContext has not initialized");
        }
        return sDataContext;
    }

    private AppDataSource mAppDataSource;
    private final SpCache mSpCache;
    private int mHostEnvIdentification;

    private DataContext(Application application) {
        mSpCache = new SpCache(NAME, false/*不适用 apply，立即保存*/);
        //环境初始化
        initEnvironment();
        //初始化网络库
        NetContext.get()
                .newBuilder()
                .aipHandler(newApiHandler())
                .httpConfig(newOkHttpConfig())
                .networkChecker(NetworkUtils::isConnected)
                .errorDataAdapter(newErrorDataAdapter())
                .setup();
    }

    void publishLoginExpired(int code) {
        AppContext.errorHandler().handleGlobalError(code);
        Timber.d("用户登录过期：%s", ((isLoginExpired(code)) ? "token 过期。" : "其他设备登录。"));
    }

    /**
     * 获取语言环境和主机环境的综合标识，比如<font color="red">调试环境的中文</font>
     *
     * @return tag
     */
    final String environmentTag() {
        return hostTag();
    }

    /**
     * 获取当前构建的主机地址标识对象字符串，如：<font color="red">调试、预发布、生产</font>
     *
     * @return Identification
     */
    private String hostTag() {
        if (mHostEnvIdentification == BUILD_RELEASE) {
            return RELEASE;
        } else if (mHostEnvIdentification == BUILD_TEST) {
            return TEST;
        }
        return DEV;
    }

    private void initEnvironment() {
        if (BuildConfig.openDebug) {
            mHostEnvIdentification = mSpCache.getInt(HOST_KEY, BUILD_DEV);
            addAllHost();
        } else {
            mHostEnvIdentification = BUILD_RELEASE;
            addReleaseHost();
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // public
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 获取当前构建的主机地址标识，如：<font color="red">调试、预发布、生产</font>
     *
     * @return Identification
     */
    public int hostIdentification() {
        return mHostEnvIdentification;
    }

    /**
     * 用于调试，切换主机环境。
     */
    public void switchHost(int host) {
        mSpCache.putInt(HOST_KEY, host);
    }

    public void onAppDataSourcePrepared(AppDataSource appDataSource) {
        if (mAppDataSource != null) {
            throw new IllegalStateException("DataContext.onAppDataSourcePrepared already called");
        }
        mAppDataSource = appDataSource;
    }

    public String getAppToken() {
        if (mAppDataSource != null) {
            return mAppDataSource.appToken();
        }
        return "";
    }

    public String baseWebUrl() {
        return getBaseWebUrl();
    }

    static String baseUrl() {
        return getBaseUrl();
    }

    static String environment() {
        return getEnvironment();
    }

}