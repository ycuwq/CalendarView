package com.ycuwq.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ycuwq.calendarview.utils.CalendarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 一月的数据
 * Created by ycuwq on 2018/2/12.
 */
@SuppressLint("ViewConstructor")
class MonthView extends ViewGroup implements WeekView.OnDaySelectedListener{

    protected static final int MAX_ROW = 6;       //最大显示的行数

    protected static final int COLUMN = 7;        //显示的列数

    private CalendarViewDelegate mCalendarViewDelegate;

    private List<WeekView> mWeekViews;

    private OnDaySelectedListener mOnDaySelectedListener;

    public MonthView(Context context, CalendarViewDelegate calendarViewDelegate) {
        super(context);
        mCalendarViewDelegate = calendarViewDelegate;
        mWeekViews = new ArrayList<>();
    }

    public void setMonth(int year, int month) {
        List<List<Date>> lists = CalendarUtil.getMonthOfWeekDate(year, month);
        removeAllViews();
        mWeekViews.clear();
        for (int i = 0; i < lists.size(); i++) {
            List<Date> weekDays = lists.get(i);
            WeekView weekView = new WeekView(getContext(), weekDays, mCalendarViewDelegate, i);
            weekView.setOnDaySelectedListener(this);
            addView(weekView);
            mWeekViews.add(weekView);
        }
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int itemWidth = widthSpecSize / COLUMN;
        int itemHeight = itemWidth;

        int height = itemWidth * MAX_ROW;
        setMeasuredDimension(widthMeasureSpec, height);

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(MeasureSpec.makeMeasureSpec(widthSpecSize, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() == 0) {
            return;
        }

        View childView = getChildAt(0);
        int itemWidth = childView.getMeasuredWidth();
        int itemHeight = childView.getMeasuredHeight();
        //当月中的周数小于最大的周数时，将剩余的空间补充到每个item的高度上，使总高度统一。
        int dy = (MAX_ROW - getChildCount()) * itemHeight / getChildCount();

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int left = 0;
            int top = (itemHeight + dy) * i;
            int right = itemWidth;
            int bottom = top + itemHeight;
            view.layout(left, top, right, bottom);
        }
    }


    @Override
    public void onDaySelected(Date date, int position, int weekOrder) {
        for (int i = 0; i < mWeekViews.size(); i++) {
            WeekView weekView = mWeekViews.get(i);
            if (i == weekOrder) {
                weekView.selectedDate(position);
            } else {
                weekView.cancelSelected();
            }
        }

        if (mOnDaySelectedListener != null) {
            mOnDaySelectedListener.onDaySelected(date);
        }
    }

    public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener) {
        mOnDaySelectedListener = onDaySelectedListener;
    }

    interface OnDaySelectedListener {
        void onDaySelected(Date date);
    }

}
