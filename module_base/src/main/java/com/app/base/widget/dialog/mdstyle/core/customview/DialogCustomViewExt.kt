/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.customview

import android.view.View
import androidx.annotation.CheckResult
import androidx.annotation.LayoutRes
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.util.MDUtil.assertOneSet
import com.app.base.widget.dialog.mdstyle.util.MDUtil.waitForWidth

internal const val CUSTOM_VIEW_NO_VERTICAL_PADDING = "md.custom_view_no_vertical_padding"

@CheckResult fun MaterialDialog.getCustomView(): View {
    return this.view.contentLayout.customView ?: throw IllegalStateException(
        "You have not setup this dialog as a customView dialog."
    )
}

/**
 * 自定义 Material Design Dialog 布局视图（包含标题、内容、按钮以及checkbox）
 *
 * @param viewRes 自定义 view layout resource
 * @param view 自定义 view
 * @param scrollable 自定义视图是否包含在 ScrollView 中
 * @param verticalPadding 当设置为true时，则将24dp水平填充应用于您的自定义视图顶部与底部
 * @param horizontalPadding 当设置为true时，则将24dp水平填充应用于您的自定义视图左右两边
 * @param dialogWrapContent 当设置为true时，使用当前对话框宽度 measuredWidth 代替内容宽度
 */
@JvmOverloads
fun MaterialDialog.customView(
    @LayoutRes viewRes: Int? = null,
    view: View? = null,
    scrollable: Boolean = false,
    verticalPadding: Boolean = false,
    horizontalPadding: Boolean = false,
    dialogWrapContent: Boolean = false
): MaterialDialog {
    assertOneSet("customView", view, viewRes)
    config[CUSTOM_VIEW_NO_VERTICAL_PADDING] = verticalPadding

    if (dialogWrapContent) {
        // 推迟窗口测量, 赋值 literal 为 0 的目的是初始化 maxWidth 值, 使其不为 null .
        maxWidth(literal = 0)
    }

    this.view.contentLayout.addCustomView(
        res = viewRes,
        view = view,
        scrollable = scrollable,
        horizontalPadding = horizontalPadding
    )
        .also {
            if (dialogWrapContent) {
                it.waitForWidth {
                    maxWidth(literal = measuredWidth)
                }
            }
        }

    return this
}