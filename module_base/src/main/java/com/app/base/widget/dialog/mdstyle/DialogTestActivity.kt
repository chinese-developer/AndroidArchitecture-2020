/**
 * Designed and developed by Nemo (nemo@seektopser.com)
 *
 *//*

package com.app.base.widget.dialog.mdstyle

import android.os.Bundle
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.android.base.utils.blankj.ToastUtils
import com.android.base.utils.getResColor
import com.android.ui.dialog.cuttinglinestyle.showConfirmDialog
import com.android.ui.dialog.mdstyle.MaterialDialog
import com.android.ui.dialog.mdstyle.core.DialogBehavior
import com.android.ui.dialog.mdstyle.core.LayoutMode.WRAP_CONTENT
import com.android.ui.dialog.mdstyle.core.ModalDialog
import com.android.ui.dialog.mdstyle.core.bottomsheets.BottomSheet
import com.android.ui.dialog.mdstyle.core.callbacks.onCancel
import com.android.ui.dialog.mdstyle.core.callbacks.onDismiss
import com.android.ui.dialog.mdstyle.core.callbacks.onPreShow
import com.android.ui.dialog.mdstyle.core.callbacks.onShow
import com.android.ui.dialog.mdstyle.core.checkbox.checkBoxPrompt
import com.android.ui.dialog.mdstyle.core.customview.customView
import com.android.ui.dialog.mdstyle.core.customview.getCustomView
import com.android.ui.dialog.mdstyle.core.input.input
import com.android.ui.dialog.mdstyle.core.list.listItems
import com.android.ui.dialog.mdstyle.core.list.listItemsMultiChoice
import com.android.ui.dialog.mdstyle.core.list.listItemsSingleChoice
import com.android.ui.dialog.mdstyle.core.viewpagers.viewPager
import com.android.ui.dialog.mdstyle.util.ColorPalette
import com.android.ui.utils.lifecycleOwner
import com.android.ui.utils.onClickDebounced
import com.android.ui.utils.startActivityForUriIntent
import com.lp.baseandroid.R
import com.lp.baseandroid.R.id
import com.lp.baseandroid.R.layout
import com.lp.baseandroid.R.mipmap
import com.lp.baseandroid.R.string
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.sample_dialog_activity.basic
import kotlinx.android.synthetic.main.sample_dialog_activity.basic_buttons
import kotlinx.android.synthetic.main.sample_dialog_activity.basic_checkbox_titled_buttons
import kotlinx.android.synthetic.main.sample_dialog_activity.basic_html_content
import kotlinx.android.synthetic.main.sample_dialog_activity.basic_icon
import kotlinx.android.synthetic.main.sample_dialog_activity.basic_long_titled_buttons
import kotlinx.android.synthetic.main.sample_dialog_activity.basic_stacked_buttons
import kotlinx.android.synthetic.main.sample_dialog_activity.basic_titled
import kotlinx.android.synthetic.main.sample_dialog_activity.basic_titled_buttons
import kotlinx.android.synthetic.main.sample_dialog_activity.bottomsheet_customView
import kotlinx.android.synthetic.main.sample_dialog_activity.bottomsheet_list
import kotlinx.android.synthetic.main.sample_dialog_activity.bottomsheet_vp_customView
import kotlinx.android.synthetic.main.sample_dialog_activity.custom_no_vp
import kotlinx.android.synthetic.main.sample_dialog_activity.custom_view
import kotlinx.android.synthetic.main.sample_dialog_activity.custom_vp
import kotlinx.android.synthetic.main.sample_dialog_activity.input_check_prompt
import kotlinx.android.synthetic.main.sample_dialog_activity.input_counter
import kotlinx.android.synthetic.main.sample_dialog_activity.list_long_titled
import kotlinx.android.synthetic.main.sample_dialog_activity.list_titled_message_buttons
import kotlinx.android.synthetic.main.sample_dialog_activity.misc_dialog_callbacks
import kotlinx.android.synthetic.main.sample_dialog_activity.multiple_choice_disabled_items
import kotlinx.android.synthetic.main.sample_dialog_activity.normal_dialog
import kotlinx.android.synthetic.main.sample_dialog_activity.single_choice_disabled_items
import kotlinx.android.synthetic.main.sample_dialog_activity.single_choice_long_items

*/
/**
 *@author Nemo
 *      Email: nemo@seektopser.com
 *      Date : 2019-10-28 15:56
 *
 * ios 风格 dialog [com.android.ui.dialog.cuttinglinestyle.ConfirmDialog]
 *
 * 和 Material 风格 Dialog [com.android.ui.dialog.MaterialDialog]
 *
 *//*

class DialogTestActivity : RxAppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.sample_dialog_activity)

    showNormalDialog()

    basic.onClickDebounced {
      MaterialDialog(this)
          .show {
            message(text = "我没有标题、没有按钮、没有圆角，仅仅只是一条 Message~")
            lifecycleOwner(this@DialogTestActivity)
          }
    }

    basic_titled.onClickDebounced {
      MaterialDialog(this)
          .show {
            title(text = "我是 title")
            message(text = "我是 message，圆角8dip~")
            cornerRadius(8f)
            lifecycleOwner(this@DialogTestActivity)
          }
    }

    basic_buttons.onClickDebounced {
      MaterialDialog(this)
          .show {
            message(text = "我是 message，我有按钮，圆角16dip~")
            positiveButton(res = string.confirm)
            negativeButton(res = string.cancel)
            cornerRadius(16f)
            lifecycleOwner(this@DialogTestActivity)
          }
    }

    basic_stacked_buttons.onClickDebounced {
      MaterialDialog(this)
          .show {
            message(text = "我是 message，我有按钮，圆角16dip~")
            positiveButton(text = "这是一个很长很长文字的 Button")
            negativeButton(text = "它充满一行，所以它是 stack")
            cornerRadius(4f)
            lifecycleOwner(this@DialogTestActivity)
          }
    }

    basic_titled_buttons.onClickDebounced {
      MaterialDialog(this)
          .show {
            title(text = "我是 title")
            message(text = "我是 message，我有按钮，圆角16dip~")
            cornerRadius(16f)
            positiveButton(res = string.confirm)
            negativeButton(res = string.cancel)
            lifecycleOwner(this@DialogTestActivity)
          }
    }

    basic_html_content.onClickDebounced {
      MaterialDialog(this)
          .show {
            title(res = string.app_name)
            message(res = string.htmlContent) {
              lineSpacing(1.4f)
              html {
                context.startActivityForUriIntent(it) { errorMessage ->
                  ToastUtils.showShort(errorMessage)
                }
              }
            }
            positiveButton(res = string.confirm)
            negativeButton(res = string.cancel)
            lifecycleOwner(this@DialogTestActivity)
          }
    }

    basic_long_titled_buttons.onClickDebounced {
      MaterialDialog(this)
          .show {
            title(text = "前赤壁赋\n宋代：苏轼")
            message(res = string.loremIpsum)
            positiveButton(res = string.confirm)
            negativeButton(res = string.cancel)
            lifecycleOwner(this@DialogTestActivity)
          }
    }

    basic_icon.onClickDebounced {
      MaterialDialog(this)
          .show {
            title(text = "这个标题带 icon")
            icon(mipmap.ic_launcher)
            message(text = "")
            positiveButton(res = string.confirm)
            negativeButton(res = string.cancel)
            lifecycleOwner(this@DialogTestActivity)
          }
    }

    basic_checkbox_titled_buttons.onClickDebounced {
      MaterialDialog(this)
          .show {
            var isChecked = false
            title(text = "协议")
            message(text = "公共场所请勿吸烟")
            cancelOnTouchOutside(false)
            // 禁止自动 dismiss dialog
            noAutoDismiss()
            positiveButton(res = string.confirm) {
              if (!isChecked) {
                ToastUtils.showShort("请勾选协议！")
                return@positiveButton
              }
              dismiss()
            }
            negativeButton(res = string.cancel) {
              dismiss()
            }
            checkBoxPrompt(text = "我同意") { checked ->
              isChecked = checked
              ToastUtils.showShort("Checked -> $checked")
            }
            lifecycleOwner(this@DialogTestActivity)
          }
    }

    multiple_choice_disabled_items.onClickDebounced {
      MaterialDialog(this)
          .show {
            title(text = "请选择您的性别：")
            listItemsMultiChoice(
                R.array.gender,
                disableIndices = intArrayOf(2),
                initialSelection = intArrayOf(0, 1)
            ) { _, indices, items ->
              ToastUtils.showShort(
                  "Selected items ${items.joinToString()} at indices ${indices.joinToString()}"
              )
            }
            positiveButton(res = string.confirm)
            lifecycleOwner(this@DialogTestActivity)
          }
    }

    list_titled_message_buttons.onClickDebounced {
      MaterialDialog(this).show {
        listItems(R.array.province) { _, index, text ->
          ToastUtils.showShort("Selected items $text at index $index")
        }
        positiveButton()
        negativeButton()
        lifecycleOwner(this@DialogTestActivity)
      }
    }

    list_long_titled.onClickDebounced {
      MaterialDialog(this).show {
        title(text = "请选择一个省份:")
        listItems(R.array.province) { _, index, text ->
          ToastUtils.showShort("Selected item $text at index $index")
        }
        lifecycleOwner(this@DialogTestActivity)
      }
    }

    */
/** 自定义 View *//*

    custom_view.onClickDebounced { showCustomViewDialog() }

    // ------------ bottomSheet ------------

    bottomsheet_list.onClickDebounced {
      MaterialDialog(this, BottomSheet(WRAP_CONTENT)).show {
        listItems(R.array.province) { _, index, text ->
          ToastUtils.showShort("Selected item $text at index $index")
        }
        positiveButton()
        negativeButton()
        lifecycleOwner(this@DialogTestActivity)
      }
    }

    bottomsheet_customView.onClickDebounced { showCustomViewDialog(BottomSheet(WRAP_CONTENT)) }

    bottomsheet_vp_customView.onClickDebounced {
      MaterialDialog(this, BottomSheet(WRAP_CONTENT)).show {
        title("颜色选择器")
        viewPager(
            items = ColorPalette.Primary,
            subItems = ColorPalette.PrimarySub,
            allowSliding = true
        ) { _, color ->
          ToastUtils.showShort("Selected color: ${Integer.toHexString(color)}")
        }
        positiveButton("选择")
        negativeButton()
        lifecycleOwner(this@DialogTestActivity)
      }
    }

    // ------------- CustomView ViewPager ---------------
    custom_no_vp.onClickDebounced {
      MaterialDialog(this).show {
        viewPager(
            ColorPalette.Primary,
            ColorPalette.PrimarySub
        ) { _, color ->
          ToastUtils.showShort("Selected color: ${Integer.toHexString(color)}")
        }
        positiveButton()
        negativeButton()
        lifecycleOwner(this@DialogTestActivity)
      }
    }

    custom_vp.onClickDebounced {
      MaterialDialog(this).show {
        title("CustomView + ViewPager")
        viewPager(
            ColorPalette.Primary,
            ColorPalette.PrimarySub,
            allowSliding = true,
            changeActionButtonsColor = true
        ) { _, color ->
          ToastUtils.showShort("Selected color: ${Integer.toHexString(color)}")
        }
        positiveButton()
        negativeButton()
        lifecycleOwner(this@DialogTestActivity)
      }
    }

    // input
    input_counter.onClickDebounced {
      MaterialDialog(this).show {
        title("请输入验证码")
        input(
            hint = "请输入4位数字验证码",
            inputType = InputType.TYPE_CLASS_NUMBER,
            maxLength = 4
        ) { _, text ->
          ToastUtils.showShort("Input: $text")
        }
        positiveButton()
        negativeButton()
        checkBoxPrompt(text = "注册即同意协议") { checked ->
          ToastUtils.showShort("Checked? $checked")
        }
        lifecycleOwner(this@DialogTestActivity)
      }
    }

    input_check_prompt.onClickDebounced {
      MaterialDialog(this).show {
        title("请输入你想输入的文字")
        input(
            hint = "随便写点什么吧~",
            inputType = InputType.TYPE_CLASS_TEXT
        ) { _, text ->
          ToastUtils.showShort("Input: $text")
        }
        lifecycleOwner(this@DialogTestActivity)
      }
    }

    misc_dialog_callbacks.onClickDebounced {
      MaterialDialog(this).show {
        title("Dialog 所有回调触发示例")
        message("你将会收到 onShow、onCancel、onDismiss 的回调 toast")
        positiveButton()
        negativeButton()
        onPreShow {
          Toast.makeText(windowContext, "onPreShow", Toast.LENGTH_SHORT)
              .show()
        }
        onShow {
          Toast.makeText(windowContext, "onShow", Toast.LENGTH_SHORT)
              .show()
        }
        onCancel {
          Toast.makeText(windowContext, "onCancel", Toast.LENGTH_SHORT)
              .show()
        }
        onDismiss {
          Toast.makeText(windowContext, "onDismiss", Toast.LENGTH_SHORT)
              .show()
        }
        lifecycleOwner(this@DialogTestActivity)
      }
    }

    // 单选
    single_choice_long_items.onClickDebounced {
      MaterialDialog(this).show {
        title("单选按钮")
        listItemsSingleChoice(
            res = R.array.longStory, waitForPositiveButton = false
        ) { _, index, text ->
          ToastUtils.showShort("Selected item $text at index $index")
        }
        lifecycleOwner(this@DialogTestActivity)
      }
    }

    single_choice_disabled_items.onClickDebounced {
      MaterialDialog(this).show {

        listItemsSingleChoice(
            res = R.array.province, initialSelection = 1, disabledIndices = intArrayOf(1, 3)
        ) { _, index, text ->
          ToastUtils.showShort("Selected item $text at index $index")
        }
        positiveButton()

        lifecycleOwner(this@DialogTestActivity)
      }
    }
  }

  private fun showCustomViewDialog(dialogBehavior: DialogBehavior = ModalDialog) {
    val dialog = MaterialDialog(this, dialogBehavior).show {
      title(text = "SOCIALDESK - 10th Floor")
      customView(
          layout.material_dialog_custom_view, scrollable = true, verticalPadding = true,
          horizontalPadding = true
      )
      positiveButton(text = "连接") { dialog ->
        val passwordInput: EditText = dialog.getCustomView()
            .findViewById(id.password)
        ToastUtils.showShort("Password: ${passwordInput.text}")
      }
      negativeButton(res = string.cancel)
      neutralButton("别点我", colorId = R.color.color_red)
      lifecycleOwner(this@DialogTestActivity)
    }

    val customView = dialog.getCustomView()
    val passwordInput: EditText = customView.findViewById(id.password)
    val showPasswordCheck: CheckBox = customView.findViewById(id.showPassword)
    showPasswordCheck.setOnCheckedChangeListener { _, isChecked ->
      passwordInput.inputType =
        if (!isChecked) InputType.TYPE_TEXT_VARIATION_PASSWORD else InputType.TYPE_CLASS_TEXT
      passwordInput.transformationMethod =
        if (!isChecked) PasswordTransformationMethod.getInstance() else null
    }
  }

  private fun showNormalDialog() {
    normal_dialog.onClickDebounced {
      showConfirmDialog {

        title = "我是Title"

        message = "我是Message" // CharSequence 方式
        positiveText = "去绑定"
        this.clearNegativeButtonText()
//        messageId = R.string.app_name // StringRes 方式

        // 默认显示 positiveButton & negativeButton 两个按钮
        positiveListener = { _ ->
          //          ToastUtils.showShort("positive button clicked")
        }

        negativeListener // 如果你不处理任何逻辑，无需传入回调监听，点击 negative 按钮后，会 dismiss 掉当前 dialog.

        messageSize = 14F // 默认 size
        messageColor =
          context.getResColor(com.android.ui.R.color.text_primary_dark) // 默认颜色，黑色 de000000 87% 透明度
        autoDismiss = false // 点击其他区域, 是否 dismiss 当前 dialog.
      }.show {
        lifecycleOwner(
            this@DialogTestActivity
        ) // 与当前视图绑定生命周期，在 ON_PAUSE 和 ON_DESTROY Status 会自动 dismiss.
      }
    }
  }
}
*/
