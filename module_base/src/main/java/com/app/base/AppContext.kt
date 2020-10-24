/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

@file:Suppress("unused")

package com.app.base

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import androidx.multidex.MultiDex
import com.android.base.TagsFactory
import com.android.base.app.BaseAppContext
import com.android.base.app.Sword
import com.android.base.app.mvvm.VMViewModel
import com.android.base.rx.SchedulerProvider
import com.android.sdk.net.NetConfig
import com.android.sdk.net.error.RequestParamsException
import com.android.sdk.net.error.ResponseException
import com.android.sdk.net.error.ServerResponseException
import com.android.sdk.net.service.ServiceFactory
import com.app.base.app.AppSecurity
import com.app.base.app.ErrorHandler
import com.app.base.data.DataConfig
import com.app.base.data.app.AppDataSource
import com.app.base.data.app.StorageManager
import com.app.base.debug.DebugTools
import com.app.base.router.AppRouter
import com.app.base.router.RouterManager
import com.app.base.scope.DialogCoroutineScope
import com.app.base.widget.dialog.AppLoadingView
import com.android.base.widget.statusLayout.StateConfig
import com.drake.tooltip.toast
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
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
    // Provides the user with a download/upload listener callback
    // Alternatively, you can create a Retrofit that doesn't carry token
    val downloadOrUploadServiceWithoutToken: ServiceFactory by lazy { NetConfig.downloadOrUploadServiceWithoutToken }

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
        // 调试
        DebugTools.init(this)
        // 数据层
        DataConfig.init(this)
        // 安全层
        AppSecurity.init()
        // 路由
        RouterManager.init(this)

        StateConfig.apply {
            emptyLayout = R.layout.base_layout_empty
            loadingLayout = R.layout.base_layout_loading
            errorLayout = R.layout.base_layout_error
        }.setRetryIds(R.id.base_retry_btn)

        // 初始化 SmartRefreshLayout, 这是自动下拉刷新和上拉加载采用的第三方库  [https://github.com/scwang90/SmartRefreshLayout/tree/master] V2版本
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            MaterialHeader(context)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }

        com.android.base.widget.adapter.BindingAdapter.modelId = BR.item

        // 基础库配置
        Sword.get()
            .registerLoadingFactory { AppLoadingView(it) } // 默认的通用的LoadingDialog和Toast实现
            .setCrashProcessor { _, _ ->  }
            .setErrorClassifier(object : Sword.ErrorClassifier {
                override fun isNetworkError(throwable: Throwable): Boolean {
                    Timber.tag(TagsFactory.okHttp).d(throwable)
                    return throwable is RequestParamsException || throwable is IOException || throwable is ResponseException
                }

                override fun isServerError(throwable: Throwable): Boolean {
                    Timber.tag(TagsFactory.okHttp).d(throwable)
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

        @JvmStatic
        fun serviceFactoryWithoutToken(): ServiceFactory {
            return context.downloadOrUploadServiceWithoutToken
        }

        var onDialog: DialogCoroutineScope.(FragmentActivity) -> Dialog = {
            val progress = ProgressDialog(activity)
            progress.setMessage(activity.getString(R.string.loading))
            progress
        }
    }
}


fun Activity.appRouter() = AppContext.appRouter()
fun Activity.schedulerProvider() = AppContext.schedulerProvider()
fun Activity.storageManager() = AppContext.storageManager()
fun Activity.errorHandler() = AppContext.errorHandler()
fun Activity.appDataSource() = AppContext.appDataSource()
fun Activity.serviceFactoryWithoutToken() = AppContext.serviceFactoryWithoutToken()

/**
 * 设置使用DialogObserver默认弹出的加载对话框
 * 默认使用系统自带的ProgressDialog
 */
fun CoroutineContext.dialog(block: (DialogCoroutineScope.(context: FragmentActivity) -> Dialog)) {
    AppContext.onDialog = block
}

fun toast(msg: CharSequence? = null) {
    AppContext.get().toast(msg ?: "")
}

fun VMViewModel.toast(msg: CharSequence? = null) {
    AppContext.get().toast(msg ?: "")
}
