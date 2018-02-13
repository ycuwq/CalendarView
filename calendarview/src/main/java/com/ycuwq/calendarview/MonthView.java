package com.ycuwq.calendarview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.ycuwq.calendarview.utils.CalendarUtil;

import java.util.List;

/**
 * Created by ycuwq on 2018/2/12.
 */

class MonthView extends ViewGroup {

    protected static final int MAX_ROW = 6;       //最大显示的行数

    protected static final int COLUMN = 7;        //显示的列数

    private DayItemAttrs mDayItemAttrs;

    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDayItemAttrs = new DayItemAttrs();
        mDayItemAttrs.setSelectedBg(context.getResources().getDrawable(R.drawable.com_ycuwq_calendarview_blue_circle));
        setMonth(2018, 2);
    }

    public void setMonth(int year, int month) {
        List<List<Date>> lists = CalendarUtil.getMonthOfWeekDate(year, month);
        removeAllViews();
        for (List<Date> weekDays : lists) {
            WeekView weekView = new WeekView(getContext(), weekDays, mDayItemAttrs);
            addView(weekView);
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


}
