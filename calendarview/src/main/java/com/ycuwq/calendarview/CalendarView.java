package com.ycuwq.calendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ycuwq.calendarview.utils.CalendarUtil;

import org.joda.time.LocalDate;

/**
 * Created by ycuwq on 2018/2/11.
 */
public class CalendarView extends LinearLayout {
    private final String TAG =getClass().getSimpleName();

    private MonthPager mMonthPager;
    private MonthAdapter mMonthAdapter;
    private DayItemAttrs mDayItemAttrs;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        mDayItemAttrs = new DayItemAttrs();
        mDayItemAttrs.setSelectedBg(context.getResources().getDrawable(R.drawable.com_ycuwq_calendarview_blue_circle));
        initChild();
        setDateToCurrent();
    }

    private void setDateToCurrent() {
        LocalDate localDate = new LocalDate();
        int position = CalendarUtil.getMonthPosition(1980, 1,
                localDate.getYear(), localDate.getMonthOfYear());
        mMonthPager.setCurrentItem(position, false);
    }

    private void initChild() {
        mMonthAdapter = new MonthAdapter(2000, 1980, 1, mDayItemAttrs);
        mMonthPager = new MonthPager(getContext());
        mMonthPager.setOffscreenPageLimit(1);
        mMonthPager.setAdapter(mMonthAdapter);
        addView(mMonthPager, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
