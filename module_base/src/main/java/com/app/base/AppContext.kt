package com.app.base

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import androidx.multidex.MultiDex
import com.android.base.app.BaseAppContext
import com.android.base.app.Sword
import com.android.base.rx.SchedulerProvider
import com.android.sdk.net.NetConfig
import com.app.base.scope.DialogCoroutineScope
import com.android.sdk.net.error.RequestParamsException
import com.android.sdk.net.error.ResponseException
import com.android.sdk.net.error.ServerResponseException
import com.app.base.app.AppSecurity
import com.app.base.app.ErrorHandler
import com.app.base.config.AppSettings
import com.app.base.data.DataConfig
import com.app.base.data.app.AppDataSource
import com.app.base.data.app.StorageManager
import com.app.base.debug.DebugTools
import com.app.base.router.AppRouter
import com.app.base.router.RouterManager
import com.app.base.widget.dialog.AppLoadingView
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

open class AppContext : BaseAppContext() {

    @Inject lateinit var appDataSource: AppDataSource
    @Inject lateinit var errorHandler: ErrorHandler
    @Inject lateinit var appRouter: AppRouter
    @Inject lateinit var storageManager: StorageManager
    @Inject lateinit var schedulerProvider: SchedulerProvider
    @Inject lateinit var sharedPreferences: SharedPreferences

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        context = this
        initApp()
    }

    private fun initApp() {
        // 安全层
        AppSecurity.init()
        // 路由
        RouterManager.init(this)
        // 调试
        DebugTools.init(this)

        // 基础库配置
        Sword.get()
            .registerLoadingFactory { AppLoadingView(it) } // 默认的通用的LoadingDialog和Toast实现
            .setDefaultPageStart(AppSettings.DEFAULT_PAGE_START) // 分页开始页码
            .setDefaultPageSize(AppSettings.DEFAULT_PAGE_SIZE) // 默认分页大小
            .setErrorClassifier(object : Sword.ErrorClassifier {
                override fun isNetworkError(throwable: Throwable): Boolean {
                    Timber.tag("===OkHttp===").d(throwable)
                    return throwable is RequestParamsException || throwable is IOException || throwable is ResponseException
                }

                override fun isServerError(throwable: Throwable): Boolean {
                    Timber.tag("===OkHttp===").d(throwable)
                    return throwable is ServerResponseException || throwable is HttpException && throwable.code() >= 500
                }
            })

        // 给数据层设置全局数据源
        DataConfig.getInstance().onAppDataSourcePrepared(appDataSource())
    }

    open fun restartApp() {}

    companion object {

        @JvmStatic
        private lateinit var context: AppContext

        @JvmStatic
        fun get(): AppContext = context

        @JvmStatic
        fun storageManager(): StorageManager {
            return context.storageManager
        }

        @JvmStatic
        fun errorHandler(): ErrorHandler {
            return context.errorHandler
        }

        @JvmStatic
        fun appDataSource(): AppDataSource {
            return context.appDataSource
        }

        @JvmStatic
        fun appRouter(): AppRouter {
            return context.appRouter
        }

        @JvmStatic
        fun schedulerProvider(): SchedulerProvider {
            return context.schedulerProvider
        }

        //
        var onDialog: DialogCoroutineScope.(FragmentActivity) -> Dialog = {
            val progress = ProgressDialog(activity)
            progress.setMessage(activity.getString(R.string.net_dialog_msg))
            progress
        }
    }
}


fun Activity.appRouter() = AppContext.appRouter()
fun Activity.schedulerProvider() = AppContext.schedulerProvider()
fun Activity.storageManager() = AppContext.storageManager()
fun Activity.errorHandler() = AppContext.errorHandler()
fun Activity.appDataSource() = AppContext.appDataSource()

/**
 * 设置使用DialogObserver默认弹出的加载对话框
 * 默认使用系统自带的ProgressDialog
 */
fun CoroutineContext.Dialog(block: (DialogCoroutineScope.(context: FragmentActivity) -> Dialog)) {
    AppContext.onDialog = block
}

/**
 * 该函数指定某些Observer的onError中的默认错误信息处理
 *
 * @see NetConfig.onError
 */
fun CoroutineContext.onError(block: Throwable.() -> Unit) {
    NetConfig.onError = block
}