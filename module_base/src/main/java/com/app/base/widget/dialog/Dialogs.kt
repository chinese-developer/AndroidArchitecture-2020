@file:JvmName("Dialogs")

package com.app.base.widget.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.android.base.app.ui.LoadingView
import com.android.base.utils.android.ActFragWrapper
import com.android.base.utils.ktx.getColorRes
import com.app.base.R

private val internalHandler = Handler(Looper.getMainLooper())

fun Dialog.onDismiss(action: () -> Unit): Dialog {
    setOnDismissListener {
        action()
    }
    return this
}

///////////////////////////////////////////////////////////////////////////
// Tips
///////////////////////////////////////////////////////////////////////////
class TipsDialogBuilder(private val actFragWrapper: ActFragWrapper) {
    companion object {
        internal const val TYPE_SUCCESS = 1
        internal const val TYPE_ERROR = 2
    }

    fun success() = TYPE_SUCCESS

    fun error() = TYPE_ERROR

    /*消息*/
    @StringRes
    var messageId = BaseDialogBuilder.NO_ID
        set(value) {
            message = actFragWrapper.context.getText(value)
        }
    var message: CharSequence = "debug：请设置message"

    var type = 0
    var autoDismissMillisecond: Long = 3000
}

fun showTipsDialog(actFragWrapper: ActFragWrapper, builder: TipsDialogBuilder.() -> Unit): Dialog {
    val tipsDialogBuilder = TipsDialogBuilder(actFragWrapper)
    builder(tipsDialogBuilder)
    return showTipsDialogImpl(actFragWrapper, tipsDialogBuilder)
}

fun Fragment.showTipsDialog(builder: TipsDialogBuilder.() -> Unit): Dialog {
    return showTipsDialog(ActFragWrapper.create(this), builder)
}

fun FragmentActivity.showTipsDialog(builder: TipsDialogBuilder.() -> Unit): Dialog {
    return showTipsDialog(ActFragWrapper.create(this), builder)
}

private fun showTipsDialogImpl(
    actFragWrapper: ActFragWrapper,
    tipsDialogBuilder: TipsDialogBuilder
): Dialog {
    val tipsDialog = TipsDialog(
        actFragWrapper.context,
        when (tipsDialogBuilder.type) {
            TipsDialogBuilder.TYPE_SUCCESS -> R.drawable.ic_app_logo
            TipsDialogBuilder.TYPE_ERROR -> R.drawable.icon_warning
            else -> throw IllegalArgumentException("you should define the type of tips ")
        }
    )

    tipsDialog.setMessage(tipsDialogBuilder.message)
    tipsDialog.setCancelable(false)

    val autoDismiss = Runnable {
        try {
            tipsDialog.dismiss()
        } catch (ignore: Exception) {
        }
    }

    val lifecycleOwner: LifecycleOwner = if (actFragWrapper.fragment != null) {
        actFragWrapper.fragment
    } else {
        actFragWrapper.context as FragmentActivity
    }

    lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            tipsDialog.dismiss()
            internalHandler.removeCallbacks(autoDismiss)
        }
    })

    tipsDialog.show()

    internalHandler.postDelayed(autoDismiss, tipsDialogBuilder.autoDismissMillisecond)

    return tipsDialog
}

///////////////////////////////////////////////////////////////////////////
// Loading Dialog
///////////////////////////////////////////////////////////////////////////
open class BaseLoadingDialog(val context: Context) {

    companion object {
        internal const val NO_ID = 0
    }

    @StyleRes
    var style: Int = NO_ID
    var title: CharSequence? = null

    @StringRes
    var titleId: Int = NO_ID
        set(value) {
            title = context.getText(value)
        }

    var titleSize = 16F
    var titleColor = context.getColorRes(R.color.colorAccent)

    var cancelable: Boolean = true
}


///////////////////////////////////////////////////////////////////////////
// Loading
///////////////////////////////////////////////////////////////////////////
fun showLoadingDialog(
    context: Context,
    builder: BaseLoadingDialog.() -> Unit,
    autoShow: Boolean = false
): Dialog {
    val baseLoadingDialog = BaseLoadingDialog(context)
    builder.invoke(baseLoadingDialog)
    val loadingDialog = LoadingDialog(baseLoadingDialog)
    loadingDialog.setCancelable(baseLoadingDialog.cancelable)
    loadingDialog.setCanceledOnTouchOutside(false)
    if (autoShow) loadingDialog.show()
    return loadingDialog
}

fun Fragment.showLoadingDialog(builder: BaseLoadingDialog.() -> Unit): Dialog? {
    val context = this.context ?: return null
    return showLoadingDialog(context, builder)
}

fun Activity.showLoadingDialog(builder: BaseLoadingDialog.() -> Unit): Dialog {
    return showLoadingDialog(this, builder)
}

fun LoadingView.showLoadingDialog(
    context: Context,
    builder: BaseLoadingDialog.() -> Unit
): Dialog? {
    return showLoadingDialog(context, builder, false)
}

///////////////////////////////////////////////////////////////////////////
// Confirm or Cancel
///////////////////////////////////////////////////////////////////////////
open class BaseDialogBuilder(val context: Context) {

    companion object {
        internal const val NO_ID = 0
    }

    //样式
    var style: Int = R.style.Theme_Dialog_Common_Transparent_Floating

    /*标题*/
    @StringRes
    var titleId: Int = NO_ID
        set(value) {
            title = context.getText(value)
        }
    var title: CharSequence? = null

    /**标题的字体大小，单位为 sp*/
    var titleSize = 16F

    /**标题的字体颜色*/
    var titleColor = context.getColorRes(R.color.textSecondPrimary)

    /*确认按钮*/
    @StringRes
    var positiveId: Int = NO_ID
        set(value) {
            positiveText = if (value == NO_ID) {
                null
            } else {
                context.getText(value)
            }
        }
    var positiveText: CharSequence? = context.getText(R.string.confirm)

    @ColorRes
    var positiveColorId: Int = NO_ID
        set(value) {
            positiveColor = ContextCompat.getColor(context, value)
        }

    @ColorInt
    var positiveColor: Int = NO_ID

    /*否认按钮*/
    @StringRes
    var negativeId: Int = NO_ID
        set(value) {
            negativeText = if (value == NO_ID) {
                null
            } else {
                context.getText(value)
            }
        }
    var negativeText: CharSequence? = context.getText(R.string.cancel)
    fun noNegative() {
        negativeText = null
    }

    /*取消*/
    var autoDismiss: Boolean = true
}

class ConfirmDialogBuilder(context: Context) : BaseDialogBuilder(context) {
    /*消息*/
    @StringRes
    var messageId = NO_ID
        set(value) {
            message = context.getText(value)
        }
    var message: CharSequence = "debug：请设置message"

    /**消息的字体大小，单位为 sp*/
    var messageSize = 14F

    /**消息的字体颜色*/
    var messageColor = context.getColorRes(R.color.text_808080)

    //确认与取消
    var positiveListener: ((dialog: Dialog) -> Unit)? = null
    var negativeListener: ((dialog: Dialog) -> Unit)? = null

    //顶部 icon
    var iconId = NO_ID
}

fun showConfirmDialog(context: Context, builder: ConfirmDialogBuilder.() -> Unit): Dialog {
    // 1. 构建Buidler，该类主要用于配置Dialog常规参数，提供了默认配置。如：字体大小，颜色，按钮监听回调等；
    val confirmDialogBuilder = ConfirmDialogBuilder(context)
    // 2. 将构建完Builder对象传递给外部调用者，让调用者可以自行修改配置参数。如标题、内容、按钮监听回调等；
    builder.invoke(confirmDialogBuilder)
    // 3. 构建Dialog实例对象
    val confirmDialog = ConfirmDialog(confirmDialogBuilder)
    // 4. 显示dialog
    confirmDialog.show()
    return confirmDialog
}

fun Fragment.showConfirmDialog(builder: ConfirmDialogBuilder.() -> Unit): Dialog? {
    val context = this.context ?: return null
    return showConfirmDialog(context, builder)
}

fun Activity.showConfirmDialog(builder: ConfirmDialogBuilder.() -> Unit): Dialog {
    return showConfirmDialog(this, builder)
}

///////////////////////////////////////////////////////////////////////////
// List
///////////////////////////////////////////////////////////////////////////
class ListDialogBuilder(context: Context) : BaseDialogBuilder(context) {

    @ArrayRes
    var itemsId: Int = NO_ID
        set(value) {
            items = context.resources.getTextArray(value)
        }
    var items: Array<CharSequence>? = null

    var adapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>? = null

    var selectedPosition: Int = 0

    var positiveListener: ((which: Int, item: CharSequence) -> Unit)? = null
    var negativeListener: (() -> Unit)? = null
}

fun showListDialog(context: Context, builder: ListDialogBuilder.() -> Unit): Dialog {
    val listDialogBuilder = ListDialogBuilder(context)
    builder.invoke(listDialogBuilder)
    val choiceDialog = ListDialog(listDialogBuilder)
    choiceDialog.show()
    return choiceDialog
}

fun Fragment.showListDialog(builder: ListDialogBuilder.() -> Unit): Dialog? {
    val context = this.context ?: return null
    return showListDialog(context, builder)
}

fun Activity.showListDialog(builder: ListDialogBuilder.() -> Unit): Dialog {
    return showListDialog(this, builder)
}
