package com.android.base.utils.adapter;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 多级列表联动，当点击一侧列表控制另一侧列表滚动时，是否需要滚动效果的工具类
 * <pre>
 *
 *  if (isScrollSmoothly()) {
 *     // 带有滚动动画
 *     RecyclerViewScrollHelper.smoothScrollToPosition(recyclerview,
 *     LinearSmoothScroller.SNAP_TO_START,
 *     holder.getAdapterPosition());
 *  } else {
 *     // 不带有滚动动画
 *     layoutManager.scrollToPositionWithOffset(
 *     holder.getAdapterPosition()), SCROLL_OFFSET);
 *  }
 *
 * </pre>
 */
public class RecyclerViewScrollHelper {

    public static void smoothScrollToPosition(RecyclerView recyclerView, int snapMode, int position) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            LinearSmoothScroller mScroller = null;
            if (snapMode == LinearSmoothScroller.SNAP_TO_START) {
                mScroller = new TopSmoothScroller(recyclerView.getContext());
            } else if (snapMode == LinearSmoothScroller.SNAP_TO_END) {
                mScroller = new BottomSmoothScroller(recyclerView.getContext());
            } else {
                mScroller = new LinearSmoothScroller(recyclerView.getContext());
            }
            mScroller.setTargetPosition(position);
            manager.startSmoothScroll(mScroller);
        }
    }

    public static class TopSmoothScroller extends LinearSmoothScroller {
        TopSmoothScroller(Context context) {
            super(context);
        }

        @Override
        protected int getHorizontalSnapPreference() {
            return SNAP_TO_START;
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }

    public static class BottomSmoothScroller extends LinearSmoothScroller {
        BottomSmoothScroller(Context context) {
            super(context);
        }

        @Override
        protected int getHorizontalSnapPreference() {
            return SNAP_TO_END;
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_END;
        }
    }
}
