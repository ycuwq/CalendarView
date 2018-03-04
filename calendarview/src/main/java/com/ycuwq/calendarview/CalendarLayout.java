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

import timber.log.Timber;

/**
 * CalendarView的父容器，实现滚动方法
 * Created by ycuwq on 2018/2/26.
 */
public class CalendarLayout extends LinearLayout {
    private final String TAG = getClass().getSimpleName();

    private CalendarView mCalendarView;
    private View mContentView;
    private float mLastDownY;
    private int mTouchSlop, mMaximumVelocity;
    private CalendarItemPager mMonthPager;

    private int mContentViewMaxTranslationY;
    private boolean mIsAnimating;
    private int mContentViewOverScrollMode;
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
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 2) {
            if (getChildAt(0) instanceof CalendarView) {
                mCalendarView = (CalendarView) getChildAt(0);

                mMonthPager = mCalendarView.getMonthPager();
            }
            mContentView = getChildAt(1);
            mContentViewOverScrollMode = mContentView.getOverScrollMode();
        }
        // TODO: 2018/2/28 如果子View不等于2抛出异常

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
        //在m将ContentView扩大，否则ContentView在上滑的时候下边会有空隙。
        if (mCalendarView.getCalendarType() == CalendarView.TYPE_MONTH) {
            int monthPagerHeight = mCalendarView.getMonthPager().getMeasuredHeight();
            mContentViewMaxTranslationY = monthPagerHeight - monthPagerHeight / CalendarItemView.MAX_ROW;
            int contentViewHeight = mContentView.getMeasuredHeight() + mContentViewMaxTranslationY;
            int heightSpec = MeasureSpec.makeMeasureSpec(contentViewHeight, MeasureSpec.EXACTLY);
            mContentView.measure(widthMeasureSpec, heightSpec);
        }
        //如果mContentViewMaxTranslation 为0的话，计算该值。
        if (mContentViewMaxTranslationY == 0 && mCalendarView.getCalendarType() == CalendarView.TYPE_WEEK) {
            int weekPagerHeight = mCalendarView.getWeekPager().getMeasuredHeight();
            mContentViewMaxTranslationY = weekPagerHeight * (CalendarItemView.MAX_ROW - 1);
        }

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Timber.d("onInterceptTouchEvent");
        if (mIsAnimating) {
            return true;
        }
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastDownY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastDownY;
                if (Math.abs(dy) < mTouchSlop) {
                    return false;
                }
                if (mCalendarView.getCalendarType() == CalendarView.TYPE_MONTH) {
                    if (dy > 0) {
                        return mContentView.getTranslationY() < 0;
                    } else {
                        return true;
                    }
                } else if (mCalendarView.getCalendarType() == CalendarView.TYPE_WEEK) {
                    if (dy < 0) {
                        return false;
                    } else {
                        return true;
                    }
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
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastDownY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastDownY;
//                int calendarItemRowHeight = mCalendarView.getCalendarViewDelegate().getCalendarItemRowHeight();
//                mContentViewMaxTranslationY = calendarItemRowHeight * (CalendarItemView.MAX_ROW - 1);
                ////RecyclerView在月视图下的最大位移距 == MonthView的高 - WeekView的高
                if (mCalendarView.getCalendarType() == CalendarView.TYPE_MONTH) {
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
                    } else {
                        //上滑
                        CalendarItemView calendarItemView = mCalendarView.getCurrentCalendarItemView();
                        if (calendarItemView == null) {
                            return false;
                        }
                        int selectedItemTop = calendarItemView.getSelectedItemTop();
                        if (-mMonthPager.getTranslationY() >= selectedItemTop &&
                                -mContentView.getTranslationY() >= mContentViewMaxTranslationY) {
                            mContentView.setTranslationY(0);
                            mCalendarView.setTypeToWeek();
                            return false;
                        }

                        if (-mContentView.getTranslationY() < mContentViewMaxTranslationY) {
                            mContentView.setTranslationY(Math.max(mContentView.getTranslationY() + dy, -mContentViewMaxTranslationY));
                        }
                        //上滑MonthPager的界限 == selectedItem的纵轴顶部坐标
                        if (-mMonthPager.getTranslationY() < selectedItemTop) {
                            mMonthPager.setTranslationY(mContentView.getTranslationY());
                        } else {
                            mMonthPager.setTranslationY(-selectedItemTop);
                        }
                    }
                } else {
                    if (dy > 0) {
                        if (mContentView.getTranslationY() == 0 && isScrollToTop()) {
                            hideWeek();
                            CalendarItemView calendarItemView = mCalendarView.getCurrentCalendarItemView();
                            if (calendarItemView != null) {
                                mMonthPager.setTranslationY(-calendarItemView.getSelectedItemTop());
                                mContentView.setTranslationY(-mContentViewMaxTranslationY);
                            }
                        } else {
                            mContentView.onTouchEvent(event);
                        }
                    } else {
                        mContentView.onTouchEvent(event);
                    }
                }
                mLastDownY = y;
                return true;
            case MotionEvent.ACTION_UP:
                //如果 > 0 意味着没有滚动完成
                if (mCalendarView.getCalendarType() == CalendarView.TYPE_MONTH &&
                        mContentView.getTranslationY() < 0) {
                    if (mContentView.getTranslationY() > -100) {
                        expand();
                    } else {
                        shrink();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void hideWeek() {
        mCalendarView.getWeekPager().setVisibility(GONE);
        mCalendarView.getMonthPager().setVisibility(VISIBLE);
    }

    public void expand() {
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

    public  void shrink() {
        if (mIsAnimating) {
            return;
        }
        CalendarItemView calendarItemView = mCalendarView.getCurrentCalendarItemView();
        if (calendarItemView == null) {
            return;
        }
        final int selectedItemTop = calendarItemView.getSelectedItemTop();
        mIsAnimating = true;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mContentView,
                "translationY", mContentView.getTranslationY(), -mContentViewMaxTranslationY);
        objectAnimator.setDuration(500);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float currentValue = (Float) animation.getAnimatedValue();
                if (currentValue > -selectedItemTop) {
                    mMonthPager.setTranslationY(currentValue);
                } else {
                    mMonthPager.setTranslationY(-selectedItemTop);
                }
            }
        });
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsAnimating = false;
                mContentView.setTranslationY(0);
                mCalendarView.setTypeToWeek();
            }
        });
        objectAnimator.start();
    }
}
