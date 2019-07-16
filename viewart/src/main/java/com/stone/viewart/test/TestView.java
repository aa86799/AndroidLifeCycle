package com.stone.viewart.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Scroller;

import androidx.annotation.Nullable;


/**
 * desc     : 主要是一些 api 的说明
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/28 16 29
 */
public class TestView extends View implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener /*,GestureDetector.OnContextClickListener*/
        , ScaleGestureDetector.OnScaleGestureListener {

    //系统所能识别的最小滑动距离
    private int mScaledTouchSlop;

    //滑动速率追踪
    private VelocityTracker mVelocityTracker;

    //手势检测
    private GestureDetector mGestureDetector;
    //空实现手势几个监听接口；23以后，也会实现 GestureDetector.OnContextClickListener
//    GestureDetector.SimpleOnGestureListener;

    //缩放手势检测
    private ScaleGestureDetector mScaleGestureDetector;
    //实现SimpleOnScaleGestureListener
//    ScaleGestureDetector.SimpleOnScaleGestureListener;

    //view 的内容滚动器
    private Scroller mScroller;

    public TestView(Context context) {
        this(context, null);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

        mVelocityTracker = VelocityTracker.obtain();

        mGestureDetector = new GestureDetector(this);
        mGestureDetector.setOnDoubleTapListener(this);//设置双击监听
//        mGestureDetector.setContextClickListener(this); //监听上下文发生的触摸事件

        mScroller = new Scroller(getContext());
    }

    //api21 later
//    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //使用mGestureDetector 处理 触摸事件；需要实现GestureDetector.OnGestureListener
//        return mGestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                System.out.println("stone-> ACTION_DOWN");
//                break;
                return true;//消费后，执行 move
            case MotionEvent.ACTION_MOVE:
                System.out.println("stone-> ACTION_MOVE");
                velocity(event);
                break;
            case MotionEvent.ACTION_UP:
                System.out.println("stone-> ACTION_UP");
                break;
        }

        int pointerCount = event.getPointerCount(); //获取手指触摸点的个数
        for (int i = 0; i < pointerCount; i++) {
            int pointerId = event.getPointerId(i); //根据触摸点的 index，返回触摸 id
        }

        return super.onTouchEvent(event);
    }

    private void velocity(MotionEvent event) {
        mVelocityTracker.addMovement(event);
        /*
         * 计算速率，参数毫秒， 即测算的是 像素/毫秒，如下即 像素/秒
         * 在获取速率前，都需要先调用computeCurrentVelocity
         * 速率值有负值，即方向性：从左向右，从上向下为正；反之为负
         *      即计算公式：速率=(终点-起点)/消耗的时间
         * 由于消耗时间的不确定性，如，100ms滑动200px，200ms滑动600px，分别得到结果速率：200px/100ms, 600px/200ms;
         *   且每几十(10，20...)毫秒，就会计算一次并获取，所以在一定时间段内，会进行多次测算；
         *   所以从 move 中打印出的数据，发现是一组从低到高再从高到低的数据 (这里说的正方向)
         */
        mVelocityTracker.computeCurrentVelocity(1000);
        float vx = mVelocityTracker.getXVelocity();//获取 x 方向速率
//        mVelocityTracker.getXVelocity(1); //指定pointerId上的 x 方向的速率
        float vy = mVelocityTracker.getYVelocity();
//        mVelocityTracker.getYVelocity(1);

        System.out.println("stone-> " +vx + ", " + vy);

        //当不需要 mVelocityTracker 时，需要
        mVelocityTracker.clear();
        mVelocityTracker.recycle();
    }

    @Override//GestureDetector.OnGestureListener
    public boolean onDown(MotionEvent e) {
        //down事件
        return false;
    }

    @Override//GestureDetector.OnGestureListener
    public void onShowPress(MotionEvent e) {
        //down事件发生而move或则up还没发生前触发，强调尚未松开或拖动
    }

    @Override//GestureDetector.OnGestureListener
    public boolean onSingleTapUp(MotionEvent e) {
        //一次down，up事件
        return false;
    }

    @Override//GestureDetector.OnGestureListener
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //在屏幕上拖动事件。无论是用手拖动view，或者是以抛的动作滚动，都会多次触发,
        // 这个方法在ACTION_MOVE动作发生时就会触发
        return false;
    }

    @Override//GestureDetector.OnGestureListener
    public void onLongPress(MotionEvent e) {
        //长按事件；Touch了不移动, 一直Touch down时触发
    }

    @Override//GestureDetector.OnGestureListener
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //快速滑动手势事件；Touch了滑动一点距离后，在ACTION_UP时才会触发
        //e1,第一个ACTION_DOWN；e2，引发当前事件的ACTION_MOVE；
        return false;
    }

    @Override//GestureDetector.OnDoubleTapListener
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //确认单击事件后，短时间内没有再次单击， 即双击时是不会触发的；onSingleTapUp，双击会触发两次
        return false;
    }

    @Override//GestureDetector.OnDoubleTapListener
    public boolean onDoubleTap(MotionEvent e) {
        //在双击的第二下，Touch down时触发
        return false;
    }

    @Override//GestureDetector.OnDoubleTapListener
    public boolean onDoubleTapEvent(MotionEvent e) {
        /*
         * 通知DoubleTap手势中的事件, 双击的第二下Touch down和up都会触发，可用e.getAction()区分
         */
        return false;
    }

//    @Override//GestureDetector.OnContextClickListener  api23 later
//    public boolean onContextClick(MotionEvent e) {
//        return false;
//    }

    @Override//ScaleGestureDetector.OnScaleGestureListener
    public boolean onScale(ScaleGestureDetector detector) {
        //当缩放进行中触发

        /*
            detector.getCurrentSpan();//两点间的距离跨度
			detector.getCurrentSpanX();//两点间的x距离
			detector.getCurrentSpanY();//两点间的y距离
			detector.getFocusX();		//所有pointers 之间的焦点 x 值
			detector.getFocusY();		//所有pointers 之间的焦点 y 值
			detector.getPreviousSpan();	//上次两点间的距离跨度
			detector.getPreviousSpanX();//上次两点间的x距离
			detector.getPreviousSpanY();//上次两点间的y距离
			detector.getEventTime();	//当前事件的触发时间
			detector.getTimeDelta();    //两次事件间的时间差
			detector.getScaleFactor();  //与上次事件相比，得到的缩放比例因子
         */
        return false;
    }

    @Override//ScaleGestureDetector.OnScaleGestureListener
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        //当缩放开始触发
        return false;
    }

    @Override//ScaleGestureDetector.OnScaleGestureListener
    public void onScaleEnd(ScaleGestureDetector detector) {
        //当缩放结束触发
    }

    @Override//view 重绘后，会触发
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {//滚动器是否还要计算偏移
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }

    /**
     * 从当前位置，平滑滚动到目标
     * @param destX 目标要滚动到的 x 坐标，相对于 view
     * @param destY
     */
    private void smoothScrollerTo(int destX, int destY) {
        int startScrollX = getScrollX();
        int startScrollY = getScrollY();
        int deltaX = destX - startScrollX;
        int deltaY = destY - startScrollY;
        mScroller.startScroll(startScrollX, startScrollY, deltaX, deltaY, 1000);
        postInvalidate();

        /*
         * 当知道滚动的目标距离时，调用startScroll方法后，即为这种模式。
         * 根据插入器Interpolator(如果有)，算出一定时间内需要滚动的距离，
         * 最后将滚动到的目标点x,y存储在mCurrX和mCurrY中。
         */
    }

    private void viewScrollFling() {
        /*
         * 当不知道滚动的目标距离时，如ListView、GridView、ScrollView，
         * 根据手指滑动后弹起的速率来计算目标的滚动距离。最后将滚动到的目标点x,y存储在mCurrx和mCurrY中。
         */

        //快速滚动
//        mScroller.fling(int startX, int startY, int velocityX, int velocityY,
//        int minX, int maxX, int minY, int maxY);
    }

    //坐标关系
    private void coordinatorRelation() {
        getLeft(); //view 的相对于父容器的左坐标
        getTop();
        getRight();
        getBottom();
//        setLeft(); //都有对应的 set 方法

        getX(); //x = left + translationX； 即view 的内容区的左上角 x 坐标
        getY(); //y = top + translationY；  即view 的内容区的左上角 y 坐标
//        setX(); //都有对应的 set 方法
        getTranslationX(); //左上角相对于父容器 x 方向的偏移量
        getTranslationY(); //左上角相对于父容器 y 方向的偏移量
//        setTranslationX(); //都有对应的 set 方法


    }
}
