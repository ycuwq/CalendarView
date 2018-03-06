package com.ycuwq.calendarview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.LinearLayout;

/**
 * CalendarView的父容器，实现滚动方法
 * Created by ycuwq on 2018/2/26.
 */
public class CalendarLayout extends LinearLayout {

    private CalendarView mCalendarView;
    private View mContentView;
    private float mMoveY, mLastDownY;
    private int mTouchSlop;

    private CalendarItemPager mMonthPager;

    /**
     * contentView最大上滑距离
     */
    private int mContentViewMaxTranslationY;

    /**
     * MonthView选中的Item的顶部坐标
     */
    private int mMonthViewSelectedItemTop;

    private int mContentViewOverScrollMode;

    /**
     * 是否在动画中在动画中，在动画中拦截触摸监听
     */
    private boolean mIsAnimating;


    /**
     * 是否允许滚动切换周月显示
     */
    private boolean mEnableScroll = true;

    public CalendarLayout(Context context) {
        this(context, null);
    }

    public CalendarLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final ViewConfiguration configuration = ViewConfiguration.get(context);

        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 2 && getChildAt(0) instanceof CalendarView) {
            mCalendarView = (CalendarView) getChildAt(0);

            mMonthPager = mCalendarView.getMonthPager();
        } else {
            throw new ViewNotFoundException(getClass().getName() + " must be 2 child view, " +
                    "and first child is com.ycuwq.calendarview.CalendarView");
        }
        mContentView = getChildAt(1);
        mContentViewOverScrollMode = mContentView.getOverScrollMode();
    }

    /**
     * ContentView是否滑动到顶部
     */
    private boolean isScrollToTop() {
        if (mContentView instanceof RecyclerView)
            return ((RecyclerView) mContentView).computeVerticalScrollOffset() == 0;
        if (mContentView instanceof AbsListView) {
            boolean result = false;
            AbsListView listView = (AbsListView) mContentView;
            if (listView.getFirstVisiblePosition() == 0) {
                final View topChildView = listView.getChildAt(0);
                result = topChildView.getTop() == 0;
            }
            return result;
        }
        return mContentView.getScrollY() == 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!mEnableScroll) {
            return;
        }
        //在m将ContentView扩大，否则ContentView在上滑的时候下边会有空隙。
        if (mCalendarView.getMonthPager().getVisibility() == VISIBLE) {
            int monthPagerHeight = mCalendarView.getMonthPager().getMeasuredHeight();
            mContentViewMaxTranslationY = monthPagerHeight - monthPagerHeight / CalendarItemView.MAX_ROW;
            int contentViewHeight = mContentView.getMeasuredHeight() + mContentViewMaxTranslationY;
            int heightSpec = MeasureSpec.makeMeasureSpec(contentViewHeight, MeasureSpec.EXACTLY);
            mContentView.measure(widthMeasureSpec, heightSpec);
        }
        //如果mContentViewMaxTranslation 为0的话，计算该值。
        if (mContentViewMaxTranslationY == 0 && mCalendarView.getMonthPager().getVisibility() == GONE) {
            int weekPagerHeight = mCalendarView.getWeekPager().getMeasuredHeight();
            mContentViewMaxTranslationY = weekPagerHeight * (CalendarItemView.MAX_ROW - 1);
        }
    }

    private void updateSelectedItemTop() {
        CalendarItemView calendarItemView =  mMonthPager.findViewWithTag(mMonthPager.getCurrentItem());
        if (calendarItemView == null) {
            return;
        }
        mMonthViewSelectedItemTop = calendarItemView.getSelectedItemTop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mEnableScroll) {
            return super.onInterceptTouchEvent(ev);
        }
        updateSelectedItemTop();
        if (mIsAnimating) {
            return true;
        }
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMoveY = mLastDownY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mMoveY;
                if (Math.abs(dy) < mTouchSlop) {
                    return false;
                }
                if (mCalendarView.getCalendarType() == CalendarView.TYPE_MONTH && dy < 0) {
                    mContentView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                    return true;
                } else if (mCalendarView.getCalendarType() == CalendarView.TYPE_WEEK && dy > 0) {
                    mContentView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnableScroll) {
            return super.onTouchEvent(event);
        }
        if (mIsAnimating) {
            return true;
        }
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMoveY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mMoveY;
                mMoveY = y;
                float contentViewTransY = mContentView.getTranslationY();
                //当ContentView 没有滑动到顶部或者已经滑动到周模式还继续上滑时让Content滑动
                if (!isScrollToTop() || (dy < 0 && mMonthPager.getVisibility() != VISIBLE)) {
                    return mContentView.onTouchEvent(event);
                }
                //
                //向上滑动，并且contentView已经平移到最大距离，则切换到周模式
                if (dy < 0 && contentViewTransY == -mContentViewMaxTranslationY) {
                    showWeek();
                    return true;
                }
                if (dy > 0 && contentViewTransY == 0) {
                    //周模式和月模式的contentViewTransY都为0，需要根据MonthPager是否显示判断
                    if (mMonthPager.getVisibility() == VISIBLE) {
                        mCalendarView.setTypeToMonth();
                    } else {    //在周模式下拉
                        hideWeek();
                    }
                }
                scrollChild(dy);
                return true;
            case MotionEvent.ACTION_UP:
                if (mContentView.getTranslationY() == 0) {
                    return true;
                }
                //如果 mContentView.getTranslationY() != 0 意味着没有滚动完成
                float moveY = event.getY() - mLastDownY;
                int type = mCalendarView.getCalendarType();
                if (type == CalendarView.TYPE_MONTH) {
                    if (moveY < -100) {
                        shrink();
                    } else {
                        expand();
                    }
                } else {
                    if (moveY > 100) {
                        expand();
                    } else {
                        shrink();
                    }
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void scrollChild(float dy) {
        //手指下滑
        if (dy > 0) {
            if (mContentView.getTranslationY() + dy > 0) {
                mContentView.setTranslationY(0);
            } else {
                mContentView.setTranslationY(mContentView.getTranslationY() + dy);
            }
            //这个判断的目的是要保证下滑的时候要优先使ContentView滑动到日历的下方之后，再滑动MonthPager
            if (mMonthPager.getTranslationY() <= mContentView.getTranslationY()) {
                mMonthPager.setTranslationY(mContentView.getTranslationY());
            }
        } else if (dy < 0) {    //手指上滑
            if (-mContentView.getTranslationY() < mContentViewMaxTranslationY) {
                mContentView.setTranslationY(Math.max(mContentView.getTranslationY() + dy, -mContentViewMaxTranslationY));
            }
            //上滑MonthPager的界限 == selectedItem的纵轴顶部坐标
            if (mMonthPager.getTranslationY() + dy > -mMonthViewSelectedItemTop) {
                mMonthPager.setTranslationY(mContentView.getTranslationY());
            } else {
                mMonthPager.setTranslationY(-mMonthViewSelectedItemTop);
            }
        }
    }

    private void hideWeek() {
        mCalendarView.getMonthPager().setVisibility(VISIBLE);
        mCalendarView.scrollToSelectedDate();
        updateSelectedItemTop();
        CalendarItemView calendarItemView = mMonthPager.findViewWithTag(mMonthPager.getCurrentItem());
        if (calendarItemView != null) {
            mMonthPager.setTranslationY(-calendarItemView.getSelectedItemTop());
            mContentView.setTranslationY(-mContentViewMaxTranslationY);
        }
        mCalendarView.getWeekPager().setVisibility(GONE);
    }

    private void showWeek() {
        mContentView.setTranslationY(0);
        mCalendarView.getMonthPager().setVisibility(GONE);
        mCalendarView.getWeekPager().setVisibility(VISIBLE);
        mCalendarView.setTypeToWeek();
        mCalendarView.scrollToSelectedDate();
    }

    public void expand() {
        mContentView.setOverScrollMode(mContentViewOverScrollMode);
        if (mIsAnimating) {
            return;
        }
        mIsAnimating = true;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mContentView,
                "translationY", mContentView.getTranslationY(), 0);
        objectAnimator.setDuration(500);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (Float) animation.getAnimatedValue();
                if (mMonthPager.getTranslationY() <= mContentView.getTranslationY()) {
                    mMonthPager.setTranslationY(currentValue);
                }
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsAnimating = false;
                mCalendarView.setTypeToMonth();
            }
        });
        objectAnimator.start();
    }

    public void shrink() {
        mContentView.setOverScrollMode(mContentViewOverScrollMode);
        if (mIsAnimating) {
            return;
        }

        mIsAnimating = true;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mContentView,
                "translationY", mContentView.getTranslationY(), -mContentViewMaxTranslationY);
        objectAnimator.setDuration(500);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (Float) animation.getAnimatedValue();
                if (currentValue > -mMonthViewSelectedItemTop) {
                    mMonthPager.setTranslationY(currentValue);
                } else {
                    mMonthPager.setTranslationY(-mMonthViewSelectedItemTop);
                }
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsAnimating = false;
                showWeek();
            }
        });
        objectAnimator.start();
    }
}
