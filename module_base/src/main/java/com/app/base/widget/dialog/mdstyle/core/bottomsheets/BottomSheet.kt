/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 */

package com.app.base.widget.dialog.mdstyle.core.bottomsheets

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build.VERSION
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager.LayoutParams
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.app.base.widget.dialog.mdstyle.MaterialDialog
import com.app.base.widget.dialog.mdstyle.core.DialogBehavior
import com.app.base.widget.dialog.mdstyle.core.LayoutMode
import com.app.base.widget.dialog.mdstyle.core.LayoutMode.MATCH_PARENT
import com.app.base.widget.dialog.mdstyle.core.button.DialogActionButtonLayout
import com.app.base.widget.dialog.mdstyle.core.button.shouldBeVisible
import com.app.base.widget.dialog.mdstyle.core.main.DialogLayout
import com.app.base.R
import com.app.base.widget.dialog.mdstyle.util.MDUtil.getWidthAndHeight
import com.app.base.widget.dialog.mdstyle.util.MDUtil.waitForHeight
import com.app.base.widget.dialog.mdstyle.util.animatePeekHeight
import com.app.base.widget.dialog.mdstyle.util.animateValues
import com.app.base.widget.dialog.mdstyle.util.onDetach
import com.app.base.widget.dialog.mdstyle.util.setCallbacks
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN
import kotlin.math.min
import kotlin.properties.Delegates

class BottomSheet(
  private val layoutMode: LayoutMode = MATCH_PARENT
) : DialogBehavior {
  internal var bottomSheetBehavior: BottomSheetBehavior<*>? = null
  private var bottomSheetView: ViewGroup? = null

  private var rootView: CoordinatorLayout? = null
  private var buttonsLayout: DialogActionButtonLayout? = null
  private var dialog: MaterialDialog? = null

  internal var defaultPeekHeight: Int by Delegates.notNull()
  internal var maxPeekHeight: Int = -1
  private var actualPeekHeight: Int by Delegates.notNull()

  override fun getThemeRes(isDark: Boolean): Int {
    return if (isDark) {
      R.style.MD_Dark_BottomSheet
    } else {
      R.style.MD_Light_BottomSheet
    }
  }

  @SuppressLint("InflateParams")
  override fun createView(
    creatingContext: Context,
    dialogWindow: Window,
    layoutInflater: LayoutInflater,
    dialog: MaterialDialog
  ): ViewGroup {
    rootView = layoutInflater.inflate(
        R.layout.md_dialog_base_bottomsheet,
        null,
        false
    ) as CoordinatorLayout

    this.dialog = dialog
    this.bottomSheetView = rootView!!.findViewById(R.id.md_root_bottom_sheet)
    this.buttonsLayout = rootView!!.findViewById(R.id.md_button_layout)

    val (_, windowHeight) = dialogWindow.windowManager.getWidthAndHeight()
    defaultPeekHeight = (windowHeight * DEFAULT_PEEK_HEIGHT_RATIO).toInt()
    actualPeekHeight = defaultPeekHeight
    maxPeekHeight = windowHeight

    setupBottomSheetBehavior()
    if (creatingContext is Activity) {
      carryOverWindowFlags(
          dialogWindow = dialogWindow,
          creatingActivity = creatingContext
      )
    }

    return rootView!!
  }

  private fun setupBottomSheetBehavior() {
    bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView!!)
        .apply {
          isHideable = true
          // 初始高度为 0 , 这样我们可以为它设置动画效果.
          peekHeight = 0
        }
    bottomSheetBehavior!!.setCallbacks(
        onSlide = { currentHeight ->
          val buttonsLayoutHeight = buttonsLayout?.measuredHeight ?: currentHeight + 1
          if (currentHeight in 1..buttonsLayoutHeight) {
            val diff = buttonsLayoutHeight - currentHeight
            buttonsLayout?.translationY = diff.toFloat()
          } else if (currentHeight > 0) {
            buttonsLayout?.translationY = 0f
          }

          // 如果当前是向上滑动（没到顶），则显示分割线
          invalidateDivides(currentHeight)
        },
        onHide = {
          buttonsLayout?.visibility = GONE
          dialog?.dismiss()
        }
    )

    bottomSheetView!!.waitForHeight {
      actualPeekHeight = min(defaultPeekHeight, min(measuredHeight, defaultPeekHeight))
    }
  }

  override fun getDialogLayout(root: ViewGroup): DialogLayout {
    return (root.findViewById(R.id.md_root) as DialogLayout).also { dialogLayout ->
      dialogLayout.layoutMode = layoutMode
      dialogLayout.attachButtonsLayout(buttonsLayout!!)
    }
  }

  override fun setWindowConstraints(
    context: Context,
    window: Window,
    view: DialogLayout,
    maxWidth: Int?
  ) {
    if (maxWidth == 0) {
      return
    }
    window.setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    val lp = LayoutParams()
        .apply {
          copyFrom(window.attributes)
          width = /*maxWidth ?: */LayoutParams.MATCH_PARENT
          height = LayoutParams.MATCH_PARENT
        }
    window.attributes = lp
  }

  override fun setBackgroundColor(
    view: DialogLayout,
    color: Int,
    cornerRounding: Float
  ) {
    bottomSheetView?.background = GradientDrawable().apply {
      cornerRadii = floatArrayOf(
          cornerRounding, cornerRounding, // top left
          cornerRounding, cornerRounding, // top right
          0f, 0f, // bottom left
          0f, 0f // bottom right
      )
      setColor(color)
    }
    buttonsLayout?.setBackgroundColor(color)
  }

  override fun onPreShow(dialog: MaterialDialog) {
    if (dialog.cancelOnTouchOutside && dialog.cancelable) {
      rootView?.setOnClickListener { this.dialog?.dismiss() }
    } else {
      rootView?.setOnClickListener(null)
    }

    bottomSheetView!!.waitForHeight {
      bottomSheetBehavior?.peekHeight = 0
      bottomSheetBehavior?.state = STATE_COLLAPSED
      bottomSheetBehavior?.animatePeekHeight(
          view = bottomSheetView!!,
          start = 0,
          dest = actualPeekHeight,
          duration = LAYOUT_PEEK_CHANGE_DURATION_MS,
          onEnd = {
            invalidateDivides(actualPeekHeight)
          }
      )
      showButtons()
    }
  }

  override fun onPostShow(dialog: MaterialDialog) = Unit

  override fun onDismiss(): Boolean {
    if (dialog != null &&
        bottomSheetBehavior != null &&
        bottomSheetBehavior!!.state != STATE_HIDDEN
    ) {
      bottomSheetBehavior!!.state = STATE_HIDDEN
      hideButtons()
      return true
    }
    return false
  }

  private fun hideButtons() {
    if (!buttonsLayout.shouldBeVisible()) return
    val animator = animateValues(
        from = 0,
        to = buttonsLayout!!.measuredHeight,
        duration = LAYOUT_PEEK_CHANGE_DURATION_MS,
        onUpdate = { buttonsLayout?.translationY = it.toFloat() }
    )
    buttonsLayout?.onDetach { animator.cancel() }
    animator.start()
  }

  private fun showButtons() {
    if (!buttonsLayout.shouldBeVisible()) {
      return
    }
    val start = buttonsLayout!!.measuredHeight
    buttonsLayout?.translationY = start.toFloat()
    buttonsLayout?.visibility = VISIBLE
    val animator = animateValues(
        from = start,
        to = 0,
        duration = BUTTONS_SHOW_DURATION_MS,
        onUpdate = { buttonsLayout?.translationY = it.toFloat() }
    )
    buttonsLayout?.onDetach { animator.cancel() }
    animator.apply {
      startDelay = BUTTONS_SHOW_START_DELAY_MS
      start()
    }
  }

  private fun carryOverWindowFlags(
    dialogWindow: Window,
    creatingActivity: Activity
  ) {
    val activityWindow = creatingActivity.window!!
    if (VERSION.SDK_INT >= 21) {
      dialogWindow.navigationBarColor = activityWindow.navigationBarColor
    }
  }

  private fun invalidateDivides(currentHeight: Int) {
    val contentLayout = dialog?.view?.contentLayout ?: return
    val mainViewHeight = dialog?.view?.measuredHeight ?: return
    val scrollView = contentLayout.scrollView
    val recyclerView = contentLayout.recyclerView
    when {
      currentHeight < mainViewHeight -> buttonsLayout?.drawDivider = true
      scrollView != null -> scrollView.invalidateDividers()
      recyclerView != null -> recyclerView.invalidateDividers()
      else -> buttonsLayout?.drawDivider = false
    }
  }

  companion object {
    internal const val LAYOUT_PEEK_CHANGE_DURATION_MS = 250L
    private const val DEFAULT_PEEK_HEIGHT_RATIO = 0.6f

    private const val BUTTONS_SHOW_START_DELAY_MS = 100L
    private const val BUTTONS_SHOW_DURATION_MS = 180L
  }
}