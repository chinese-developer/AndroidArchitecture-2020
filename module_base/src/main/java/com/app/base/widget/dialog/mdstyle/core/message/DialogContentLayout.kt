/**
 * Designed and developed by Nemo (privateemailmaster@gmail.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.message

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.*
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.RestrictTo
import androidx.annotation.RestrictTo.Scope.LIBRARY_GROUP
import androidx.annotation.StringRes
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.main.DialogLayout
import com.app.base.widget.dialog.mdstyle.core.main.DialogScrollView
import com.app.base.widget.dialog.mdstyle.list.DialogRecyclerView
import com.app.base.widget.dialog.mdstyle.message.DialogMessageSettings
import com.app.base.widget.dialog.mdstyle.util.MDUtil.maybeSetTextColor
import com.app.base.widget.dialog.mdstyle.util.inflate

/**
 *@author Nemo
 *      Email: Nemo@seektopser.com
 *      Date : 2019-10-26 15:44
 *
 * 该布局显示在 dialog 中间区域, 位于 [DialogTitleLayout] 和 [DialogActionButtonLayout] 中间,
 * 通常包含 message 之类的信息等。
 */
@RestrictTo(LIBRARY_GROUP)
class DialogContentLayout(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val rootLayout: DialogLayout?
        get() = parent as DialogLayout

    private var scrollFrame: ViewGroup? = null
    private var messageTextView: TextView? = null
    private var useHorizontalPadding: Boolean = false
    private val frameHorizontalMargin: Int by lazy {
        resources.getDimensionPixelSize(R.dimen.md_dialog_frame_margin_horizontal)
    }

    var scrollView: DialogScrollView? = null
    var recyclerView: DialogRecyclerView? = null
    var customView: View? = null

    override fun onMeasure(
        widthMeasureSpec: Int,
        heightMeasureSpec: Int
    ) {
        val specWidth = getSize(widthMeasureSpec)
        val specHeight = getSize(heightMeasureSpec)

        scrollView?.measure(
            makeMeasureSpec(specWidth, EXACTLY),
            makeMeasureSpec(specHeight, AT_MOST)
        )
        val scrollViewHeight = scrollView?.measuredHeight ?: 0
        val remainingHeightAfterScrollView = specHeight - scrollViewHeight
        val childCountWithoutScrollView = if (scrollView != null) childCount - 1 else childCount

        if (childCountWithoutScrollView == 0) {
            // No more children to measure
            setMeasuredDimension(specWidth, scrollViewHeight)
            return
        }

        val heightPerRemainingChild = remainingHeightAfterScrollView / childCountWithoutScrollView

        var totalChildHeight = scrollViewHeight
        for (i in 0 until childCount) {
            val currentChild = getChildAt(i)
            if (currentChild.id == scrollView?.id) {
                continue
            }
            currentChild.measure(
                if (currentChild == customView && useHorizontalPadding) {
                    makeMeasureSpec(specWidth - (frameHorizontalMargin * 2), EXACTLY)
                } else {
                    makeMeasureSpec(specWidth, EXACTLY)
                },
                makeMeasureSpec(heightPerRemainingChild, AT_MOST)
            )
            totalChildHeight += currentChild.measuredHeight
        }

        setMeasuredDimension(specWidth, totalChildHeight)
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        var currentTop = 0
        for (i in 0 until childCount) {
            val currentChild = getChildAt(i)
            val currentBottom = currentTop + currentChild.measuredHeight
            val childLeft: Int
            val childRight: Int

            if (currentChild == customView && useHorizontalPadding) {
                childLeft = frameHorizontalMargin
                childRight = measuredWidth - frameHorizontalMargin
            } else {
                childLeft = 0
                childRight = measuredWidth
            }

            currentChild.layout(
                /*left=   */childLeft,
                /*top=    */currentTop,
                /*right=  */childRight,
                /*bottom= */currentBottom
            )

            currentTop = currentBottom
        }
    }

    fun setMessage(
        dialog: MaterialDialog,
        @StringRes res: Int?,
        text: CharSequence?,
        typeface: Typeface?,
        applySettings: (DialogMessageSettings.() -> Unit)?
    ) {
        addContentScrollView()
        if (messageTextView == null) {
            messageTextView =
                inflate<TextView>(R.layout.md_dialog_stub_message, scrollFrame).apply {
                    scrollFrame!!.addView(this)
                }
        }

        val messageSettings = DialogMessageSettings(dialog, messageTextView!!)
        applySettings?.invoke(messageSettings)

        messageTextView?.run {
            typeface?.let { this.typeface = it }
            maybeSetTextColor(dialog.windowContext, R.attr.md_color_content)
            messageSettings.setText(res, text)
        }
    }

    fun addRecyclerView(
        dialog: MaterialDialog,
        adapter: RecyclerView.Adapter<*>,
        layoutManager: LayoutManager?
    ) {
        if (recyclerView == null) {
            recyclerView = inflate<DialogRecyclerView>(R.layout.md_dialog_stub_recyclerview).apply {
                this.attach(dialog)
                this.layoutManager = layoutManager ?: LinearLayoutManager(dialog.windowContext)
            }
            addView(recyclerView)
        }
        recyclerView?.adapter = adapter
    }

    fun addCustomView(
        @LayoutRes res: Int?,
        view: View?,
        scrollable: Boolean,
        horizontalPadding: Boolean
    ): View {
        check(customView == null) { "Custom view already set" }

        if (view != null && view.parent != null) {
            // 确保当前视图无 parent view
            val parent = view.parent as? ViewGroup
            parent?.let { parent.removeView(view) }
        }

        if (scrollable) {
            this.useHorizontalPadding = false
            addContentScrollView()
            customView = view ?: inflate(res!!, scrollFrame)
            scrollFrame!!.addView(customView?.apply {
                if (horizontalPadding) {
                    updatePadding(
                        left = frameHorizontalMargin,
                        right = frameHorizontalMargin
                    )
                }
            })
        } else {
            this.useHorizontalPadding = horizontalPadding
            customView = view ?: inflate(res!!)
            addView(customView)
        }

        return customView!!
    }

    fun haveMoreThanOneChild(): Boolean = childCount > 1
    private fun hasChild(): Boolean = childCount > 0

    fun modifyFirstAndLastPadding(
        top: Int = -1,
        bottom: Int = -1
    ) {
        if (top != -1 && hasChild()) {
            getChildAt(0).updatePadding(top = top)
        }
        if (bottom != -1 && hasChild()) {
            getChildAt(childCount - 1).updatePadding(bottom = bottom)
        }
    }

    fun modifyScrollViewPadding(
        top: Int = -1,
        bottom: Int = -1
    ) {
        val targetView = if (scrollView != null) scrollView else recyclerView
        if (top != -1) {
            targetView?.updatePadding(top = top)
        }
        if (bottom != -1) {
            targetView?.updatePadding(bottom = bottom)
        }
    }

    private fun addContentScrollView() {
        if (scrollView == null) {
            scrollView = inflate<DialogScrollView>(R.layout.md_dialog_stub_scrollview).apply {
                /** [DialogLayout] */
                this.rootView = rootLayout
                scrollFrame = this.getChildAt(0) as ViewGroup
            }
            addView(scrollView)
        }
    }
}