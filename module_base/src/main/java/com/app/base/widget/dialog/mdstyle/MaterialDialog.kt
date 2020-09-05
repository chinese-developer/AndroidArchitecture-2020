/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 * Material Design Dialogs
 */
@file:Suppress("unused")

package com.app.base.widget.dialog.mdstyle

import android.app.Dialog
import android.content.Context
import android.graphics.Color.TRANSPARENT
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.LayoutInflater
import androidx.annotation.*
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.core.DialogBehavior
import com.app.base.widget.dialog.mdstyle.core.ModalDialog
import com.app.base.widget.dialog.mdstyle.core.WhichButton
import com.app.base.widget.dialog.mdstyle.core.WhichButton.*
import com.app.base.widget.dialog.mdstyle.core.actions.getActionButton
import com.app.base.widget.dialog.mdstyle.core.callbacks.invokeAll
import com.app.base.widget.dialog.mdstyle.core.list.DialogAdapter
import com.app.base.widget.dialog.mdstyle.core.list.getListAdapter
import com.app.base.widget.dialog.mdstyle.core.main.DialogLayout
import com.app.base.widget.dialog.mdstyle.message.DialogMessageSettings
import com.app.base.widget.dialog.mdstyle.util.*
import com.app.base.widget.dialog.mdstyle.util.MDUtil.assertOneSet
import com.app.base.widget.dialog.mdstyle.util.MDUtil.resolveDimen

typealias DialogCallback = (MaterialDialog) -> Unit

class MaterialDialog @JvmOverloads constructor(
    val windowContext: Context,
    val dialogBehavior: DialogBehavior = DEFAULT_BEHAVIOR
) : Dialog(windowContext, inferTheme(windowContext, dialogBehavior)) {
    /** 不应该用静态变量来管理 tags, 应该将 tags 管理在当前 Dialog 下最为合适. */
    val config: MutableMap<String, Any> = mutableMapOf()

    @Suppress("UNCHECKED_CAST")
    fun <T> config(key: String): T {
        return config[key] as T
    }

    /** The root layout of the dialog. */
    val view: DialogLayout

    /** 如果启用了自动 dismiss, 则返回 true. */
    var autoDismissEnabled: Boolean = true
        internal set
    var titleFont: Typeface? = null
        internal set
    var bodyFont: Typeface? = null
        internal set
    var buttonFont: Typeface? = null
        internal set
    var cornerRadius: Float? = null
        internal set
    var cancelable: Boolean = true
        internal set
    var cancelOnTouchOutside: Boolean = true
        internal set

    @Px
    private var maxWidth: Int? = null

    internal val preShowListeners = mutableListOf<DialogCallback>()
    internal val showListeners = mutableListOf<DialogCallback>()
    internal val dismissListeners = mutableListOf<DialogCallback>()
    internal val cancelListeners = mutableListOf<DialogCallback>()

    private val positiveListeners = mutableListOf<DialogCallback>()
    private val negativeListeners = mutableListOf<DialogCallback>()
    private val neutralListeners = mutableListOf<DialogCallback>()

    init {
        /** creating an root layout of the [DialogLayout] */
        val rootView = dialogBehavior.createView(
            creatingContext = windowContext,
            dialogWindow = window!!,
            layoutInflater = LayoutInflater.from(windowContext),
            dialog = this
        )
        setContentView(rootView)
        // 将 MaterialDialog 自身实例对象传递给 [DialogLayout] 视图中的子视图 [DialogTitleLayout] 和 [DialogActionButtonLayout]
        this.view = dialogBehavior.getDialogLayout(rootView)
            .also { it.attachDialog(dialog = this) }

        // Set defaults
        this.titleFont = font(attr = R.attr.md_font_title)
        this.bodyFont = font(attr = R.attr.md_font_body)
        this.buttonFont = font(attr = R.attr.md_font_button)
        invalidateBackgroundColorAndRadius()
    }

    /** 填充标题栏的 icon */
    @JvmOverloads
    fun icon(
        @DrawableRes res: Int? = null,
        drawable: Drawable? = null
    ): MaterialDialog {
        assertOneSet("icon", drawable, res)
        populateIcon(
            view.titleLayout.iconView,
            iconRes = res,
            icon = drawable
        )
        return this
    }

    /**
     * 填充标题文字
     *
     * 字体样式与颜色 需要通过 style 设置:
     * typeface: [R.attr.md_font_title]
     * textColor:[R.attr.md_color_title]
     */
    @JvmOverloads
    fun title(
        text: String? = null,
        @StringRes res: Int? = null,
        @ColorRes colorId: Int? = null
    ): MaterialDialog {
        assertOneSet("title", text, res)
        populateText(
            view.titleLayout.titleView,
            textRes = res,
            text = text,
            typeface = this.titleFont,
            colorId = colorId,
            textColor = R.attr.md_color_title
        )
        return this
    }

    /**
     * 填充内容区域
     *
     * 字体样式 通过 style 设置：
     * typeface: [R.attr.md_font_body]
     */
    @JvmOverloads
    fun message(
        text: CharSequence? = null,
        @StringRes res: Int? = null,
        applySettings: (DialogMessageSettings.() -> Unit)? = null
    ): MaterialDialog {
        assertOneSet("message", text, res)
        this.view.contentLayout.setMessage(
            dialog = this,
            res = res,
            text = text,
            typeface = this.bodyFont,
            applySettings = applySettings
        )
        return this
    }

    /**
     * 确定按钮
     *
     * 字体样式与颜色 通过 style 设置：
     * typeface: [R.attr.md_font_button]
     * textColor:[R.attr.md_color_positive_button_text]
     */
    @JvmOverloads
    fun positiveButton(
        text: CharSequence? = null,
        @StringRes res: Int? = R.string.confirm,
        @ColorRes colorId: Int? = null,
        click: DialogCallback? = null
    ): MaterialDialog {
        if (click != null) {
            positiveListeners.add(click)
        }

        val btn = getActionButton(POSITIVE)

        populateText(
            btn,
            textRes = res,
            text = text,
            fallback = R.string.confirm,
            typeface = this.buttonFont,
            colorId = colorId,
            textColor = R.attr.md_color_positive_button_text
        )
        return this
    }

    fun positiveButton(
        @StringRes res: Int? = R.string.confirm,
        click: DialogCallback? = null
    ): MaterialDialog {
        positiveButton(text = null, res = res, colorId = null, click = click)
        return this
    }

    fun positiveButton(
        text: CharSequence? = null,
        click: DialogCallback? = null
    ): MaterialDialog {
        positiveButton(text = text, res = null, colorId = null, click = click)
        return this
    }

    fun positiveButton(
        click: DialogCallback? = null
    ): MaterialDialog {
        positiveButton(text = null, res = null, colorId = null, click = click)
        return this
    }

    /** Clears any positive action button listeners set via usages of [positiveButton]. */
    fun clearPositiveListeners(): MaterialDialog {
        this.positiveListeners.clear()
        return this
    }

    /**
     * 取消按钮
     *
     * 字体样式与颜色 通过 style 设置：
     * typeface: [R.attr.md_font_button]
     * textColor:[R.attr.md_color_negative_button_text]
     */
    @JvmOverloads
    fun negativeButton(
        text: CharSequence? = null,
        @StringRes res: Int? = R.string.cancel,
        @ColorRes colorId: Int? = null,
        click: DialogCallback? = null
    ): MaterialDialog {
        if (click != null) {
            negativeListeners.add(click)
        }

        val btn = getActionButton(NEGATIVE)

        populateText(
            btn,
            textRes = res,
            text = text,
            fallback = R.string.cancel,
            typeface = this.buttonFont,
            colorId = colorId,
            textColor = R.attr.md_color_negative_button_text
        )
        return this
    }

    fun negativeButton(
        text: CharSequence? = null,
        click: DialogCallback? = null
    ): MaterialDialog {
        negativeButton(text = text, res = null, colorId = null, click = click)
        return this
    }

    fun negativeButton(
        @StringRes res: Int? = R.string.cancel,
        click: DialogCallback? = null
    ): MaterialDialog {
        negativeButton(text = null, res = res, colorId = null, click = click)
        return this
    }

    fun negativeButton(
        click: DialogCallback? = null
    ): MaterialDialog {
        negativeButton(text = null, res = null, colorId = null, click = click)
        return this
    }

    /** Clears any negative action button listeners set via usages of [negativeButton]. */
    fun clearNegativeListeners(): MaterialDialog {
        this.negativeListeners.clear()
        return this
    }

    /**
     * 中性按钮，无法设置字体颜色（为后续可能的业务场景，做好拓展。）
     *
     * 字体样式与颜色 通过 style 设置：
     * typeface: [R.attr.md_font_button]
     * textColor: [R.attr.md_color_neutral_button_text]
     */
    @JvmOverloads
    fun neutralButton(
        text: CharSequence? = null,
        @StringRes res: Int? = null,
        @ColorRes colorId: Int? = null,
        click: DialogCallback? = null
    ): MaterialDialog {
        if (click != null) {
            neutralListeners.add(click)
        }

        val btn = getActionButton(NEUTRAL)
        if (res == null && text == null && btn.isVisibleForMd()) {
            return this
        }

        populateText(
            btn,
            textRes = res,
            text = text,
            typeface = this.buttonFont,
            colorId = colorId,
            textColor = R.attr.md_color_neutral_button_text
        )
        return this
    }

    /** Clears any negative action button listeners set via usages of [neutralButton]. */
    fun clearNeutralListeners(): MaterialDialog {
        this.neutralListeners.clear()
        return this
    }

    /** 如果调用该方法，将禁止自动 dismiss dialog, 必须手动 dismiss. */
    @CheckResult
    fun noAutoDismiss(): MaterialDialog {
        this.autoDismissEnabled = false
        return this
    }

    /**
     * 移动设备上最小宽度为56dp * 5 = 280dp.
     * 如果你重新规范了 dialog 的最大宽度 或 重写了该方法，要确保在较大屏幕上进行测试
     * 该设置仅会在 [show] 时起作用
     */
    @JvmOverloads
    fun maxWidth(
        @DimenRes res: Int? = null,
        @Px literal: Int? = null
    ): MaterialDialog {
        assertOneSet("maxWidth", res, literal)
        val shouldSetConstraints = this.maxWidth != null && this.maxWidth == 0
        this.maxWidth = if (res != null) {
            windowContext.resources.getDimensionPixelOffset(res)
        } else {
            literal!!
        }
        if (shouldSetConstraints) {
            setWindowConstraints()
        }
        return this
    }

    /**
     * 设置 dialog 圆角
     *
     * 某些场景可能需要：
     * e.g. bottom sheets 可能只想设置左上和右上的圆角.
     */
    @JvmOverloads
    fun cornerRadius(
        literalDp: Float? = null,
        @DimenRes res: Int? = null
    ): MaterialDialog {
        assertOneSet("cornerRadius", literalDp, res)
        this.cornerRadius = if (res != null) {
            windowContext.resources.getDimension(res)
        } else {
            val displayMetrics = windowContext.resources.displayMetrics
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, literalDp!!, displayMetrics)
        }
        invalidateBackgroundColorAndRadius()
        return this
    }

    override fun show() {
        setWindowConstraints()
        preShow()
        dialogBehavior.onPreShow(this)
        super.show()
        dialogBehavior.onPostShow(this)
    }

    inline fun show(func: MaterialDialog.() -> Unit): MaterialDialog {
        this.func()
        this.show()
        return this
    }

    @Deprecated(
        message = "不推荐这种方式 setCancelable(Boolean), 请使用下面我们自定义方式 [cancelable(Boolean)] " +
                "来代替它, 方便你更流畅的调用.",
        replaceWith = ReplaceWith("cancelable(cancelable)")
    )
    override fun setCancelable(cancelable: Boolean) {
        this.cancelable = cancelable
        super.setCancelable(cancelable)
    }

    /** 推荐使用该方式, 来配置 dialog 是否可以被 cancelled. 默认 true */
    fun cancelable(cancelable: Boolean): MaterialDialog {
        @Suppress("DEPRECATION")
        setCancelable(cancelable)
        return this
    }

    @Deprecated(
        message = "不推荐这种方式 setCanceledOnTouchOutside(Boolean), " +
                "请使用下面我们自定义方式 [cancelOnTouchOutside(Boolean)] 来代替它, 方便你更流畅的调用.",
        replaceWith = ReplaceWith("cancelOnTouchOutside(cancelable)")
    )
    override fun setCanceledOnTouchOutside(cancelOnTouchOutside: Boolean) {
        this.cancelOnTouchOutside = cancelOnTouchOutside
        super.setCanceledOnTouchOutside(cancelOnTouchOutside)
    }

    /** 推荐使用该方式, 来配置是否允许用户通过触摸 dialog UI 外部取消正在显示的 dialog. 默认 true */
    fun cancelOnTouchOutside(cancelable: Boolean): MaterialDialog {
        @Suppress("DEPRECATION")
        setCanceledOnTouchOutside(cancelable)
        return this
    }

    override fun dismiss() {
        if (dialogBehavior.onDismiss()) {
            return
        }
        hideKeyboard()
        super.dismiss()
    }

    internal fun onActionButtonClicked(which: WhichButton) {
        when (which) {
            POSITIVE -> {
                positiveListeners.invokeAll(this)
                val adapter = getListAdapter() as? DialogAdapter<*, *>
                adapter?.positiveButtonClicked()
            }
            NEGATIVE -> negativeListeners.invokeAll(this)
            NEUTRAL -> neutralListeners.invokeAll(this)
        }
        if (autoDismissEnabled) {
            dismiss()
        }
    }

    private fun setWindowConstraints() {
        dialogBehavior.setWindowConstraints(
            context = windowContext,
            maxWidth = maxWidth,
            window = window!!,
            view = view
        )
    }

    private fun invalidateBackgroundColorAndRadius() {
        val backgroundColor = resolveColor(attr = R.attr.md_background_color) {
            resolveColor(attr = R.attr.colorBackgroundFloating)
        }
        val cornerRadius =
            cornerRadius ?: resolveDimen(windowContext, attr = R.attr.md_corner_radius)
        window?.setBackgroundDrawable(ColorDrawable(TRANSPARENT))
        dialogBehavior.setBackgroundColor(
            view = view,
            color = backgroundColor,
            cornerRounding = cornerRadius
        )
    }

    /** 测试规格使用的，使用者无需关心 */
    @CheckResult
    fun debugMode(debugMode: Boolean = true): MaterialDialog {
        this.view.debugMode = debugMode
        return this
    }

    companion object {
        /**
         * 如果用户没有配置，则使用默认的 [dialogBehavior] 实现类 [ModalDialog].
         */
        @JvmStatic
        var DEFAULT_BEHAVIOR: DialogBehavior = ModalDialog
    }
}