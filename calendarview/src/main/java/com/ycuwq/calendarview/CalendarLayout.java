package com.ycuwq.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

import timber.log.Timber;

/**
 * CalendarView的父容器，实现滚动方法
 * Created by ycuwq on 2018/2/26.
 */
public class CalendarLayout extends LinearLayout {
    private final String TAG = getClass().getSimpleName();

    private CalendarView mCalendarView;
    private RecyclerView mRecyclerView;
    private float mLastDownY;
    private int mTouchSlop, mMaximumVelocity;
    private int mMaximumTranslateY;
    private CalendarItemPager mMonthPager;
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
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) instanceof CalendarView) {
                mCalendarView = (CalendarView) getChildAt(i);
                mMonthPager = mCalendarView.getMonthPager();
            }
            if (getChildAt(i) instanceof RecyclerView) {
                mRecyclerView = (RecyclerView) getChildAt(i);
            }
        }
        // TODO: 2018/2/28 如果没有子View抛出异常

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastDownY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int maximumTranslateY = mCalendarView.getMaximumTranslateY();
                float dy = y - mLastDownY;
                Timber.d("dy%s", dy);

                if (dy < 0 && mRecyclerView.getTranslationY() <= - maximumTranslateY) {
                    return false;
                }
                if (dy < 0 && mRecyclerView.getTranslationY() == 0 &&
                        mCalendarView.getCalendarType() == CalendarView.TYPE_WEEK) {
                    return false;
                }
                if (dy > 0 && mRecyclerView.getTranslationY() >= maximumTranslateY) {
                    if (mRecyclerView.computeVerticalScrollOffset() != 0) {
                        return false;
                    }
                }
                if (dy > 0 && mRecyclerView.getTranslationY() == 0) {
                    return false;
                }
                if (Math.abs(dy) > mTouchSlop) {
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
        Timber.d("onTouchEvent");
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastDownY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mLastDownY;
                mMonthPager.setTranslationY(mMonthPager.getTranslationY() + dy);
                mRecyclerView.setTranslationY(mRecyclerView.getTranslationY() + dy);
                mLastDownY = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.onTouchEvent(event);
    }
}
