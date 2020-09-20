package com.app.base.utils

import android.app.Activity
import android.content.DialogInterface
import android.os.Process
import android.os.SystemClock
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.app.base.AppContext
import com.app.base.R
import com.app.base.data.*
import com.app.base.data.BUILD_RELEASE
import com.app.base.data.BUILD_UAT
import com.app.base.data.api.ApiParameter
import com.app.base.scope.DialogCoroutineScope

class DebugModeUtils(
    val activity: FragmentActivity,
    lifeEvent: Lifecycle.Event? = Lifecycle.Event.ON_DESTROY
) {

    init {
        activity.lifecycle.addObserver(object : LifecycleEventObserver {
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
        val platforms = if (ApiParameter.PLATFORM_COUNT == 2) {
            when (hostEnvIdentification) {
                BUILD_UAT -> choice[0] = 0
                else -> choice[0] = 1
            }
            arrayOf("预发布", "正式")
        } else {
            when (hostEnvIdentification) {
                BUILD_UAT -> choice[0] = 0
                BUILD_RELEASE -> choice[0] = 1
                else -> choice[0] = 2
            }
            arrayOf("预发布", "正式", "开发")
        }
        AlertDialog.Builder(activity)
            .setSingleChoiceItems(platforms, choice[0]) { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                val host: Int = when (which) {
                    0 -> DataConfig.BUILD_UAT
                    1 -> DataConfig.BUILD_RELEASE
                    else -> DataConfig.BUILD_DEV
                }
                showRestartAction(activity, host, which, view)
            }.show()
    }

    private fun showRestartAction(activity: FragmentActivity, host: Int, position: Int, view: TextView?) {
        AlertDialog.Builder(activity)
            .setMessage("应用将会重启！当前环境的登录状态与数据缓存将会清空，确定要切换吗？")
            .setNegativeButton(R.string.cancel) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(R.string.confirm) { dialog, _ ->
                dialog.dismiss()
                displayHostName(host, view)
                DataConfig.getInstance().switchHost(host, position)
                // 清除所有数据（除了持久化数据 stableStorage)
                AppContext.appDataSource().logout()
                // 重启
                activity.window.decorView.post { doRestart() }
            }.show()
    }

    ///////////////////////////////////////////////////////////////////////////
    // 环境相关
    ///////////////////////////////////////////////////////////////////////////
    fun displayHostName(hostEnvIdentification: Int, view: TextView?) {
        view?.apply {
            when (hostEnvIdentification) {
                DataConfig.BUILD_DEV -> text = "开发环境"
                DataConfig.BUILD_UAT ->  text = "预演环境"
                DataConfig.BUILD_RELEASE -> text = "正式环境"
            }
        }
    }

    private fun doRestart() {
        com.app.base.AppContext.get().restartApp()
        Process.killProcess(Process.myPid())
    }

}