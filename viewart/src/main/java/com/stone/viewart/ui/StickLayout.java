package com.stone.viewart.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;


/**
 * desc     : 内部有一个 Header 和 ListView，内外都上下滑动
 *              当 Header 显示或滑动到 ListView 顶部，由外部拦截事件；
 *              当 Header 隐藏
 *                  若 ListView 滑动到顶部且手势是向下，由外部拦截事件；
 *              其它由内部 ListView 拦截
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/29 14 56
 */
public class StickLayout extends LinearLayout {

    private static final int STATUS_EXPANDED = 1;
    private static final int STATUS_COLLAPSED = 2;
    
    private int mTouchSlop;
    private int mLastX;
    private int mLastY;
    private int mLastXIntercept; //记录上次拦截滑动的坐标
    private int mLastYIntercept;
    private boolean mDisallowIntercepetTouchEventOnHeader;
    private int mHeaderHeight;
    private int mOriginalheaderHeight;
    private int mStatus = STATUS_EXPANDED;
    private GiveUpTouchEventListener mGiveUpTouchEventListener;
    private boolean mIsSticky;
    private Scroller mScroller;

    public interface GiveUpTouchEventListener {
        boolean giveUpTouchEvent(MotionEvent event);
    }

    public StickLayout(Context context) {
        this(context, null);
    }

    public StickLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastXIntercept = x;
                mLastYIntercept = y;
                mLastX = x;
                mLastY = y;
                intercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastXIntercept;
                int deltaY = y - mLastYIntercept;
                if (mDisallowIntercepetTouchEventOnHeader && y <= getHeaderHeight()) {
                    intercepted = false;
                } else if (Math.abs(deltaY) <= Math.abs(deltaX)) {
                    intercepted = false;
                } else if (mStatus == STATUS_EXPANDED && deltaY <= -mTouchSlop) {
                    intercepted = true;
                } else if (mGiveUpTouchEventListener != null) {
                    if (mGiveUpTouchEventListener.giveUpTouchEvent(ev) && deltaY >= mTouchSlop) {
                        intercepted = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mLastXIntercept = mLastYIntercept = 0;
                break;
            default:
                break;
        }
        return intercepted && mIsSticky;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsSticky) {
            return true;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                mHeaderHeight += deltaX;
                break;
            case MotionEvent.ACTION_UP:
               int destHeight = 0;
                if (mHeaderHeight <= mOriginalheaderHeight * 0.5f) {
                    destHeight = 0;
                    mStatus = STATUS_COLLAPSED;
                } else {
                    destHeight = mOriginalheaderHeight;
                    mStatus = STATUS_EXPANDED;
                }

                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    private void smoothScrollBy(int dx, int dy) {

    }

}
