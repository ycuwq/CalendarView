package com.ycuwq.calendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by ycuwq on 2018/2/11.
 */
public class CalendarView extends LinearLayout {
    private final String TAG =getClass().getSimpleName();

    private MonthPager mMonthPager;
    private MonthAdapter mMonthAdapter;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        initChild();
    }

    private void initChild() {
        mMonthAdapter = new MonthAdapter();
        mMonthPager = new MonthPager(getContext());
        mMonthPager.setAdapter(mMonthAdapter);
        addView(mMonthPager, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        Log.d(TAG, "onMeasure: ");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
