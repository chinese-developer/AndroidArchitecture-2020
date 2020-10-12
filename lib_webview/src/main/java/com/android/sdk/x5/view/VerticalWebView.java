package com.android.sdk.x5.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;


/**
 * <pre>
 *     desc  : 自定义x5的webView
 *     revise: 当WebView在最顶部或者最底部的时候，不消费事件
 * </pre>
 */
public class VerticalWebView extends ScrollWebView {

    private float downX;
    private float downY;

    public VerticalWebView(Context arg0) {
        super(arg0);
    }

    public VerticalWebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = ev.getX();
                downY = ev.getY();
                //如果滑动到了最底部，就允许继续向上滑动加载下一页，否者不允许
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = ev.getX() - downX;
                float dy = ev.getY() - downY;
                boolean allowParentTouchEvent;
                if (Math.abs(dy) > Math.abs(dx)) {
                    if (dy > 0) {
                        //位于顶部时下拉，让父View消费事件
                        allowParentTouchEvent = isTop();
                    } else {
                        //位于底部时上拉，让父View消费事件
                        allowParentTouchEvent = isBottom();
                    }
                } else {
                    //水平方向滑动
                    allowParentTouchEvent = true;
                }
                getParent().requestDisallowInterceptTouchEvent(!allowParentTouchEvent);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 回到顶部
     */
    public void goTop() {
        if (!isTop()){
            scrollTo(0, 0);
        }
    }
}
