package com.sary.arounddepot.view;

import com.sary.arounddepot.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.RelativeLayout.LayoutParams;

/**
 * 自己重写的ViewGroup,用与滑动切换界面使用
 * 
 * 
 * 
 */
public class FlipperLayout extends RelativeLayout {

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mWidth;

    public static final int SCREEN_STATE_CLOSE = 0;
    public static final int SCREEN_STATE_OPEN = 1;
    public static final int TOUCH_STATE_RESTART = 0;
    public static final int TOUCH_STATE_SCROLLING = 1;
    public static final int SCROLL_STATE_NO_ALLOW = 0;
    public static final int SCROLL_STATE_ALLOW = 1;
    private int mScreenState = 0;
    private int mTouchState = 0;
    private int mScrollState = 0;
    private int mVelocityValue = 0;
    private boolean mOnClick = false;
    private onUgcDismissListener mOnUgcDismissListener;
    private onUgcShowListener mOnUgcShowListener;

    /** {@link #mCenterView}的两侧阴影效果 */
    private RelativeLayout bgShade;
    private int mScreenWidth;
    private int mScreenHeight;
    private Activity context;
    private LayoutParams bgParams;
    private View currentView;
    
    private float mLastMotionX;
    private float mLastMotionY;
    //控制左侧侧边栏能否滑动
    private boolean mCanSlideLeft = true;
    private boolean mIsBeingDragged = true;


    public FlipperLayout(Context context) {
        super(context);
        mScroller = new Scroller(context);
        mWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 54, getResources().getDisplayMetrics());

    }

    public FlipperLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FlipperLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int height = child.getMeasuredHeight();
            int width = child.getMeasuredWidth();
            child.layout(0, 0, width, height);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev){
    	Log.i("tag", "进入。");
        obtainVelocityTracker(ev);
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTART : TOUCH_STATE_SCROLLING;
            if (mTouchState == TOUCH_STATE_RESTART) {
                int x = (int) ev.getX();
                int screenWidth = getWidth();
                if (x <= mWidth && mScreenState == SCREEN_STATE_CLOSE && mTouchState == TOUCH_STATE_RESTART
                        || x >= screenWidth - mWidth && mScreenState == SCREEN_STATE_OPEN
                        && mTouchState == TOUCH_STATE_RESTART) {
                    if (mScreenState == SCREEN_STATE_OPEN) {
                        mOnClick = true;
                    }
                    mScrollState = SCROLL_STATE_ALLOW;
                } else {
                    mOnClick = false;
                    mScrollState = SCROLL_STATE_NO_ALLOW;
                }
            } else {
                return false;
            }
            break;
        case MotionEvent.ACTION_MOVE:
            mVelocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
            if (mScrollState == SCROLL_STATE_ALLOW && getWidth() - (int) ev.getX() < mWidth) {
                return true;
            }
            break;
        case MotionEvent.ACTION_UP:
            releaseVelocityTracker();
            if (mOnClick) {
                mOnClick = false;
                mScreenState = SCREEN_STATE_CLOSE;
                mScroller.startScroll(getChildAt(2).getScrollX(), 0, -getChildAt(2).getScrollX(), 0, 800);
                invalidate();
            }
            break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
    	Log.i("tag", "进入。。");
        obtainVelocityTracker(ev);
        switch (ev.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTART : TOUCH_STATE_SCROLLING;
            if (mTouchState == TOUCH_STATE_SCROLLING) {
                return false;
            }
            mLastMotionX = ev.getX();
            mLastMotionY = ev.getY();
            mIsBeingDragged = false; // 每次在拦截down时间时，都对mIsBeingDragged复制为false
            break;

        case MotionEvent.ACTION_MOVE:
        	final float xDiff = ev.getX() - mLastMotionX;
            final float yDiff = Math.abs(ev.getY() - mLastMotionY);
        	if (mCanSlideLeft == false){
                return false;
            }
        	Log.i("tag", "xDiff:"+xDiff+" mCanSlideLeft:"+mCanSlideLeft);
        	
            mOnClick = false;
            mVelocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
            if (mScrollState == SCROLL_STATE_ALLOW && Math.abs(mVelocityTracker.getXVelocity()) > 200) {
                return true;
            }
            break;

        case MotionEvent.ACTION_UP:
            releaseVelocityTracker();
            if (mScrollState == SCROLL_STATE_ALLOW && mScreenState == SCREEN_STATE_OPEN) {
                return true;
            }
            break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {
    	Log.i("tag", "进入。。");
        obtainVelocityTracker(event);
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTART : TOUCH_STATE_SCROLLING;
            if (mTouchState == TOUCH_STATE_SCROLLING) {
                return false;
            }
            break;

        case MotionEvent.ACTION_MOVE:
            mVelocityTracker.computeCurrentVelocity(1000, ViewConfiguration.getMaximumFlingVelocity());
            mVelocityValue = (int) mVelocityTracker.getXVelocity();
            getChildAt(2).scrollTo(-(int) event.getX(), 0);
            getChildAt(1).scrollTo(-(int) event.getX()+45, 0);// 阴影层跟着移动，但是要偏移出去一部分
            break;
        case MotionEvent.ACTION_UP:
            if (mScrollState == SCROLL_STATE_ALLOW){
                if (mVelocityValue > 2000) {
                    mScreenState = SCREEN_STATE_OPEN;
                    mScroller.startScroll(getChildAt(2).getScrollX(), 0,
                            -(getWidth() - Math.abs(getChildAt(2).getScrollX()) -

                            mWidth), 0, 250);
                    invalidate();

                } else if (mVelocityValue < -2000) {
                    mScreenState = SCREEN_STATE_CLOSE;
                    mScroller.startScroll(getChildAt(2).getScrollX(), 0, -getChildAt(2).getScrollX(), 0, 250);
                    invalidate();
                } else if (event.getX() < getWidth() / 2) {
                    mScreenState = SCREEN_STATE_CLOSE;
                    mScroller.startScroll(getChildAt(2).getScrollX(), 0, -getChildAt(2).getScrollX(), 0, 800);
                    invalidate();
                } else {
                    mScreenState = SCREEN_STATE_OPEN;
                    mScroller.startScroll(getChildAt(2).getScrollX(), 0,
                            -(getWidth() - Math.abs(getChildAt(2).getScrollX()) -

                            mWidth), 0, 800);
                    invalidate();
                }
            }
            break;
        }
        return true;
    }
    
    //设置侧边栏能否侧滑
    public void setCanSliding(boolean canSlideLeft){
    	this.mCanSlideLeft = canSlideLeft;
    }

    public void open() {
        mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTART : TOUCH_STATE_SCROLLING;
        if (mTouchState == TOUCH_STATE_RESTART) {
            mScreenState = SCREEN_STATE_OPEN;
            mScroller.startScroll(getChildAt(2).getScrollX(), 0, -(getWidth() - Math.abs(getChildAt(2).getScrollX()) -
            mWidth), 0, 800);
            invalidate();
        }
    }

    public void close(View view) {
        mScreenState = SCREEN_STATE_CLOSE;
        mScroller.startScroll(getChildAt(2).getScrollX(), 0, -getChildAt(2).getScrollX(), 0, 800);
        invalidate();
        setContentView(view);
    }

    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
//            getChildAt(2).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//            getChildAt(1).scrollTo(mScroller.getCurrX() - 24, mScroller.getCurrY());// 背景阴影左偏
//            postInvalidate();
            
            int oldX = getChildAt(2).getScrollX();
            int oldY = getChildAt(2).getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            if (oldX != x || oldY != y) { // 检查滚动后值，保证getChildAt(2)和mScroller滚动后的坐标一致
                if (getChildAt(2) != null) {
                    getChildAt(2).scrollTo(x, y);
                    if (x < 0)
                        getChildAt(1).scrollTo(x + 52, y);// 背景阴影右偏
                    else
                        getChildAt(1).scrollTo(x - 24, y);// 背景阴影左偏
                }
            }
            invalidate();
        } else {
            if (mScreenState == SCREEN_STATE_OPEN) {
                if (mOnUgcDismissListener != null) {
                    mOnUgcDismissListener.dismiss();
                }
            } else if (mScreenState == SCREEN_STATE_CLOSE) {
                if (mOnUgcShowListener != null) {
                    mOnUgcShowListener.show();
                }
            }
        }
    }

    private void obtainVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    public int getScreenState() {
        return mScreenState;
    }

    public void setContentView(View view) {
        removeViewAt(2);
        addView(view, 2, getLayoutParams());
        currentView = view;
    }

    public interface OnOpenListener {
        public abstract void open();
    }

    public interface OnCloseListener {
        public abstract void close();
    }

    public interface onUgcDismissListener {
        public abstract void dismiss();
    }

    public interface onUgcShowListener {
        public abstract void show();
    }

    public void setOnUgcDismissListener(onUgcDismissListener onUgcDismissListener) {
        mOnUgcDismissListener = onUgcDismissListener;
    }

    public void setOnUgcShowListener(onUgcShowListener onUgcShowListener) {
        mOnUgcShowListener = onUgcShowListener;
    }
}
