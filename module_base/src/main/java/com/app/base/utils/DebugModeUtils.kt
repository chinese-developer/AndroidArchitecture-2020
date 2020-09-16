package com.app.base.utils

import android.app.Activity
import android.content.DialogInterface
import android.os.Process
import android.os.SystemClock
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.app.base.R
import com.app.base.data.DataConfig

class DebugModeUtils(
        val activity: Activity,
        lifecycleOwner: LifecycleOwner? = null,
        lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
) {

    init {
        lifecycleOwner?.lifecycle?.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (lifeEvent == event) {

                }
            }
        })
    }

    private var mHits: LongArray? = LongArray(5)

    fun switchHostWhen5hitsFast(view: TextView?) {
        if (mHits == null) mHits = LongArray(5)
        mHits?.apply {
            System.arraycopy(this, 1, this, 0, size - 1)
            this[size - 1] = SystemClock.uptimeMillis()
            if (SystemClock.uptimeMillis() - this[0] <= 1000) {
                mHits = null
                switchHost(view)
            }
        }
    }

    fun switchHost(view: TextView?) {
        val hostEnvIdentification: Int = DataConfig.getInstance().hostIdentification()
        val choice = IntArray(1)
        if (hostEnvIdentification == DataConfig.BUILD_UAT) {
            choice[0] = 1
        } else if (hostEnvIdentification == DataConfig.BUILD_RELEASE) {
            choice[0] = 2
        }
        AlertDialog.Builder(activity)
                .setSingleChoiceItems(arrayOf("开发", "预演", "正式"), choice[0]) { dialog: DialogInterface, which: Int ->
                    dialog.dismiss()
                    val host: Int = when (which) {
                        0 -> DataConfig.BUILD_DEV
                        1 -> DataConfig.BUILD_UAT
                        else -> DataConfig.BUILD_RELEASE
                    }
                    showRestartAction(activity, host, view)
                }.show()
    }

    private fun showRestartAction(activity: Activity, host: Int, view: TextView?) {
        AlertDialog.Builder(activity)
                .setMessage("应用将会重启！当前环境的登录状态与数据缓存将会清空，确定要切换吗？")
                .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
                .setPositiveButton(R.string.confirm) { dialog, _ ->
                    dialog.dismiss()
                    displayCurrentHost(host, view)
                    DataConfig.getInstance().switchHost(host)
                    // 清除所有数据
                    com.app.base.AppContext.appDataSource().logout()
                    // AppContext.storageManager().stableStorage().clearAll(); 不清除持久化的数据
                    // 重启
                    activity.window.decorView.post { doRestart() }
                }.show()
    }

    ///////////////////////////////////////////////////////////////////////////
    // 环境相关
    ///////////////////////////////////////////////////////////////////////////
    fun displayCurrentHost(hostEnvIdentification: Int, view: TextView?) {
        view?.apply {
            when (hostEnvIdentification) {
                DataConfig.BUILD_DEV -> {
                    text = "开发环境"
                }
                DataConfig.BUILD_UAT -> {
                    text = "预演环境"
                }
                DataConfig.BUILD_RELEASE -> {
                    text = "正式环境"
                }
            }
        }
    }

    private fun doRestart() {
        com.app.base.AppContext.get().restartApp()
        Process.killProcess(Process.myPid())
    }

}