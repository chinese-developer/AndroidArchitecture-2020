/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.viewpagers

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.WhichButton.NEGATIVE
import com.app.base.widget.dialog.mdstyle.core.WhichButton.POSITIVE
import com.app.base.widget.dialog.mdstyle.core.widget.DotsIndicator
import com.app.base.widget.dialog.mdstyle.list.DialogRecyclerView
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.core.actions.getActionButton
import com.app.base.widget.dialog.mdstyle.core.actions.setActionButtonEnabled
import com.app.base.widget.dialog.mdstyle.core.customview.customView
import com.app.base.widget.dialog.mdstyle.core.customview.getCustomView
import com.app.base.widget.dialog.mdstyle.util.MDUtil.isColorDark
import com.app.base.widget.dialog.mdstyle.util.MDUtil.resolveColor
import com.app.base.widget.dialog.mdstyle.util.invalidateDividers
import com.app.base.widget.dialog.mdstyle.util.onPageSelected

typealias MaterialDialogItemCallback = ((dialog: MaterialDialog, item: Int) -> Unit)?

private const val KEY_CUSTOM_VIEWPAGER = "item_custom_viewpager"
private const val KEY_WAIT_FOR_POSITIVE = "item_wait_for_positive"
private const val KEY_CHANGE_ACTION_BUTTON_COLOR = "item_change_action_button_color"

/**
 * 包含 ViewPager Dialog，默认仅支持1个页面的滑动。
 *
 * @param items 显示在网格中的顶级颜色整数数组。
 * @param subItems 每种顶级颜色下都存在的可选子级别颜色。
 * @param initialSelection 设置初始选中颜色值
 * @param waitForPositiveButton waitForPositiveButton为true时，在用户选择之前不调用选择
 * @param allowSliding 允许像右位移一格
 * @param changeActionButtonsColor changeActionButtonsColor为true时，操作按钮的颜色将与所选颜色匹配。
 * @param selection selection用户选择颜色时调用的可选回调。
 */
@JvmOverloads
@CheckResult
fun MaterialDialog.viewPager(
    items: IntArray,
    subItems: Array<IntArray>? = null,
    initialSelection: Int? = null,
    waitForPositiveButton: Boolean = true,
    allowSliding: Boolean = false,
    changeActionButtonsColor: Boolean = false,
    selection: MaterialDialogItemCallback = null
): MaterialDialog {
    config.run {
        set(KEY_WAIT_FOR_POSITIVE, waitForPositiveButton)
        set(KEY_CUSTOM_VIEWPAGER, allowSliding)
        set(KEY_CHANGE_ACTION_BUTTON_COLOR, changeActionButtonsColor)
    }

    if (!allowSliding) {
        customView(R.layout.md_item_chooser_base_grid, verticalPadding = true)
        setupGridLayout(
            items = items,
            subItems = subItems,
            initialSelection = initialSelection,
            waitForPositiveButton = waitForPositiveButton,
            selection = selection,
            allowSliding = allowSliding
        )
    } else {
        customView(R.layout.md_item_chooser_base_pager)

        val viewPager = getPager()
        viewPager.adapter = DialogPagerAdapter()
        viewPager.onPageSelected { pageIndex ->
            setActionButtonEnabled(POSITIVE, selectedColor() != null)
            if (pageIndex == 0) {
                getCustomView()
                    .findViewById<DialogRecyclerView>(R.id.itemPreChooserGrid)
                    .invalidateDividers()
            } else {
                invalidateDividers(showTop = false, showBottom = false)
            }
        }

        getPageIndicator()?.run {
            attachViewPager(viewPager = viewPager)
            setDotTint(resolveColor(windowContext, attr = android.R.attr.textColorPrimary))
        }
        setupGridLayout(
            items = items,
            subItems = subItems,
            initialSelection = initialSelection,
            waitForPositiveButton = waitForPositiveButton,
            selection = selection,
            allowSliding = allowSliding
        )
        setupCustomPage(
            items = items
        )
    }

    if (waitForPositiveButton && selection != null) {
        setActionButtonEnabled(POSITIVE, false)
        positiveButton {
            selectedColor()?.let { selected ->
                selection.invoke(this, selected)
            }
        }
    }

    return this
}

fun MaterialDialog.setupCustomPage(
    items: IntArray,
    subItems: Array<IntArray>? = null,
    initialSelection: Int? = null,
    waitForPositiveButton: Boolean = true,
    allowSliding: Boolean = false,
    selection: MaterialDialogItemCallback = null
) {
    // 你可以在这里写你的自定义view
    val customView = getCustomView()
        .findViewById<TextView>(R.id.customView)
    customView.text = "Page 2"
}

private fun MaterialDialog.setupGridLayout(
    items: IntArray,
    subItems: Array<IntArray>?,
    initialSelection: Int? = null,
    waitForPositiveButton: Boolean = true,
    allowSliding: Boolean = false,
    selection: MaterialDialogItemCallback = null
) {
    require(subItems == null || items.size == subItems.size) {
        "Sub-items array size should match the items array size."
    }

    val gridRecyclerView = getCustomView()
        .findViewById<DialogRecyclerView>(R.id.itemPreChooserGrid)
    val gridColumnCount = 4
    gridRecyclerView.layoutManager = GridLayoutManager(windowContext, gridColumnCount)
    gridRecyclerView.attach(this)

    val adapter = ItemGridAdapter(
        dialog = this,
        items = items,
        subItems = subItems,
        initialSelection = initialSelection,
        waitForPositiveButton = waitForPositiveButton,
        callback = selection,
        enableSliding = allowSliding
    )
    gridRecyclerView.adapter = adapter
}

internal fun MaterialDialog.updateActionButtonsColor(@ColorInt color: Int) {
    val changeButtonColor: Boolean = config(KEY_CHANGE_ACTION_BUTTON_COLOR)
    if (!changeButtonColor) return

    val adjustedColor = Color.rgb(Color.red(color), Color.green(color), Color.blue(color))
    val isAdjustedDark = adjustedColor.isColorDark(0.25)
    val isPrimaryDark =
        resolveColor(context = context, attr = android.R.attr.textColorPrimary).isColorDark()

    val finalColor = if (isPrimaryDark && !isAdjustedDark) {
        resolveColor(context = context, attr = android.R.attr.textColorPrimary)
    } else if (!isPrimaryDark && isAdjustedDark) {
        resolveColor(context = context, attr = android.R.attr.textColorPrimaryInverse)
    } else {
        adjustedColor
    }
    getActionButton(POSITIVE).updateTextColor(finalColor)
    getActionButton(NEGATIVE).updateTextColor(finalColor)
}

internal fun MaterialDialog.setPage(@IntRange(from = 0, to = 1) index: Int) {
    getPager().setCurrentItem(index, true)
}

private fun MaterialDialog.getPager() = findViewById<ViewPager>(R.id.colorChooserPager)

private fun MaterialDialog.getPageCustomView() = findViewById<View?>(R.id.itemPreChooserGrid)

private fun MaterialDialog.getPageGridView() = findViewById<RecyclerView>(R.id.itemPreChooserGrid)

private fun MaterialDialog.selectedColor(): Int? {
    return (getPageGridView().adapter as ItemGridAdapter).selectedColor()
}

private fun MaterialDialog.getPageIndicator() =
    findViewById<DotsIndicator?>(R.id.colorChooserPagerDots)