package com.cjs.android.testtab.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 自定义ScrollView,用于监听滑动
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @createTime 2022/6/1 10:53
 */
public class CustomScrollView extends ScrollView {

    public Callbacks mCallbacks;

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mCallbacks != null) {
            mCallbacks.onScrollChanged(l, t, oldl, oldt);
        }
    }

    /**
    * 回调接口
    * @author JasonChen
    * @email chenjunsen@outlook.com
    * @createTime 2022/6/1 10:54
    */
    public interface Callbacks {
        /**
         * 滚动状态改变的时候
         * @param x
         * @param y
         * @param oldx
         * @param oldy
         */
        void onScrollChanged(int x, int y, int oldx, int oldy);
    }

}
