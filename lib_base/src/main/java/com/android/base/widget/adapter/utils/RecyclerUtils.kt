package com.android.base.widget.adapter.utils

import android.app.Dialog
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.android.base.widget.adapter.BindingAdapter
import com.android.base.widget.adapter.BindingAdapter.Companion.HORIZONTAL
import com.android.base.widget.adapter.DefaultDecoration
import com.android.base.widget.adapter.annotaion.DividerOrientation
import com.android.base.widget.adapter.layoutmanager.HoverGridLayoutManager
import com.android.base.widget.adapter.layoutmanager.HoverLinearLayoutManager
import com.android.base.widget.adapter.layoutmanager.HoverStaggeredGridLayoutManager

val RecyclerView.bindingAdapter
    get() = adapter as? BindingAdapter ?: throw NullPointerException("RecyclerView has no BindingAdapter")

var RecyclerView.models
    get() = bindingAdapter.models
    set(value) {
        bindingAdapter.models = value
    }

/**
 * 添加数据
 * @param models 被添加的数据
 * @param animation 添加数据是否显示动画
 */
fun RecyclerView.addModels(models: List<Any?>?, animation: Boolean = true) {
    bindingAdapter.addModels(models, animation)
}


//<editor-fold desc="配置列表">
/**
 * 设置适配器
 */
fun RecyclerView.setup(block: BindingAdapter.(RecyclerView) -> Unit): BindingAdapter {
    val adapter = BindingAdapter()
    adapter.block(this)
    this.adapter = adapter
    return adapter
}
//</editor-fold>


//<editor-fold desc="布局管理器">

/**
 * 创建[HoverLinearLayoutManager]  线性列表
 * @param orientation 列表方向
 * @param reverseLayout 是否反转列表
 * @param scrollEnabled 是否允许滚动
 */
fun RecyclerView.linear(
    @RecyclerView.Orientation orientation: Int = VERTICAL,
    reverseLayout: Boolean = false,
    scrollEnabled: Boolean = true
): RecyclerView {
    layoutManager = HoverLinearLayoutManager(
        context,
        orientation,
        reverseLayout
    ).setScrollEnabled(scrollEnabled)
    return this
}

/**
 * 创建[HoverGridLayoutManager] 网格列表
 * @param spanCount 网格跨度数量
 * @param orientation 列表方向
 * @param reverseLayout 是否反转
 * @param scrollEnabled 是否允许滚动
 */
fun RecyclerView.grid(
    spanCount: Int = 1,
    @RecyclerView.Orientation orientation: Int = VERTICAL,
    reverseLayout: Boolean = false,
    scrollEnabled: Boolean = true
): RecyclerView {
    layoutManager =
        HoverGridLayoutManager(context, spanCount, orientation, reverseLayout).setScrollEnabled(
            scrollEnabled
        )
    return this
}

/**
 * 创建[HoverStaggeredGridLayoutManager] 交错列表
 * @param orientation 列表方向
 * @param spanCount 网格跨度数量
 * @param scrollEnabled 是否允许滚动
 */
fun RecyclerView.staggered(
    spanCount: Int,
    @RecyclerView.Orientation orientation: Int = VERTICAL,
    scrollEnabled: Boolean = true
): RecyclerView {
    layoutManager =
        HoverStaggeredGridLayoutManager(spanCount, orientation).setScrollEnabled(scrollEnabled)
    return this
}
//</editor-fold>

//<editor-fold desc="分割线">

/**
 * 函数配置分割线
 * 具体配置参数查看[DefaultDecoration]
 */
fun RecyclerView.divider(
    block: DefaultDecoration.() -> Unit
): RecyclerView {
    val itemDecoration = DefaultDecoration(context).apply(block)
    addItemDecoration(itemDecoration)
    return this
}

/**
 * 指定Drawable资源为分割线, 分割线的间距和宽度应在资源文件中配置
 * @param drawable 描述分割线的drawable
 * @param orientation 分割线方向, 仅[androidx.recyclerview.widget.GridLayoutManager]需要使用此参数, 其他LayoutManager都是根据其方向自动推断
 */
fun RecyclerView.divider(
    @DrawableRes drawable: Int,
    @DividerOrientation orientation: Int = HORIZONTAL
): RecyclerView {
    return divider {
        setDrawable(drawable)
        this.orientation = orientation
    }
}
//</editor-fold>


//<editor-fold desc="对话框">
/**
 *  快速为对话框创建一个列表
 */
fun Dialog.setup(block: BindingAdapter.(RecyclerView) -> Unit): Dialog {
    val context = context
    val recyclerView = RecyclerView(context)
    recyclerView.setup(block)
    recyclerView.layoutManager = LinearLayoutManager(context)
    recyclerView.layoutParams = RecyclerView.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    setContentView(recyclerView)
    return this
}
//</editor-fold>



