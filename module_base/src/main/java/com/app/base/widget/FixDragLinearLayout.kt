package com.app.base.widget


import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.LinearLayout
import com.app.base.R

/**
 * 解决：
 * 1. ViewPager2中嵌套SwipeRefreshLayout，SwipeRefreshLayout和RecyclerView上下滑动困难。
 * 2. RecyclerView或SwipeRefreshLayout中嵌套ViewPager2，
 *    ViewPager2左右滑动困难，事件容易给RecyclerView或SwipeRefreshLayout吃掉。
 *
 *    *注意父布局继承的是 LinearLayout，使用 ConstraintLayout 当高度为 0dp 时，子 view 动态计算子view宽高并设置 layoutParams 会出问题，所以推荐使用 FixDragLinearLayout
 *    *如果存在上拉加载操作，在上拉加载过程中（上拉动画执行过程中时）依然存在事件被抢，上下滑动时会造成左右滑动。为了避免这种问题，尽量预加载。
 */
class FixDragLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var downX: Float = 0f
    private var downY: Float = 0f
    private var isDragged = false
    private val touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    val HORIZONTAL = LinearLayout.HORIZONTAL
    val VERTICAL = LinearLayout.VERTICAL
    private var customOrientation = HORIZONTAL

    init {
        attrs?.let {
            val a = context.obtainStyledAttributes(attrs, R.styleable.FixDragLayout)
            customOrientation = a.getInt(R.styleable.FixDragLayout_android_orientation, HORIZONTAL)
            a.recycle()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                isDragged = false
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isDragged) {
                    val dx = kotlin.math.abs(ev.x - downX)
                    val dy = kotlin.math.abs(ev.y - downY)
                    if (customOrientation == HORIZONTAL) {
                        isDragged = dx > touchSlop && dx > dy
                    } else if (customOrientation == VERTICAL) {
                        isDragged = dy > touchSlop && dy > dx
                    }
                }
                parent.requestDisallowInterceptTouchEvent(isDragged)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                isDragged = false
                parent.requestDisallowInterceptTouchEvent(false)
            }
        }
        return super.onInterceptTouchEvent(ev)
    }
}