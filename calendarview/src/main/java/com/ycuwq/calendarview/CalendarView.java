package com.ycuwq.calendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.ycuwq.calendarview.utils.CalendarUtil;

import org.joda.time.LocalDate;

/**
 * Created by ycuwq on 2018/2/11.
 */
public class CalendarView extends ViewGroup {

    private MonthAdapter mMonthAdapter;
    private WeekAdapter mWeekAdapter;
    private ViewPager mWeekPager, mMonthPager;
    private CalendarViewDelegate mCalendarViewDelegate;
    private WeekInfoView mWeekInfoView;
    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCalendarViewDelegate = new CalendarViewDelegate();
        mCalendarViewDelegate.setSelectedBg(context.getResources().getDrawable(R.drawable.com_ycuwq_calendarview_blue_circle));
        initChild();
        setDateToCurrent();
        mMonthPager.setVisibility(INVISIBLE);
    }


    private void setDateToCurrent() {
        LocalDate localDate = new LocalDate();
        int monthPosition = CalendarUtil.getMonthPosition(1980, 1,
                localDate.getYear(), localDate.getMonthOfYear());
        mMonthPager.setCurrentItem(monthPosition, false);
        int weekPosition = CalendarUtil.getWeekPosition(1980, 1, 1,
                localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth());
        mWeekPager.setCurrentItem(weekPosition, false);
    }

    private void initChild() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mWeekInfoView = new WeekInfoView(getContext(), mCalendarViewDelegate);
        addView(mWeekInfoView, 0, layoutParams);
        mWeekPager = new ViewPager(getContext());
        mWeekAdapter = new WeekAdapter(20000, 1980, 1, mCalendarViewDelegate);
        mWeekPager.setOffscreenPageLimit(1);
        mWeekPager.setAdapter(mWeekAdapter);
        addView(mWeekPager, layoutParams);
        mWeekPager.setTranslationY(-1);
        mMonthAdapter = new MonthAdapter(20000, 1980, 1, mCalendarViewDelegate);
        mMonthPager = new ViewPager(getContext());
        mMonthPager.setOffscreenPageLimit(1);
        mMonthPager.setAdapter(mMonthAdapter);
        addView(mMonthPager, layoutParams);
        mMonthPager.setTranslationY(-1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = mWeekInfoView.getMeasuredHeight();
        if (mMonthPager.getVisibility() == VISIBLE) {
            height += mMonthPager.getMeasuredHeight();
        } else {
            height += mMonthPager.getMeasuredHeight();
        }
       setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int weekInfoHeight = mWeekInfoView.getMeasuredHeight();
        mWeekInfoView.layout(0, 0, r, weekInfoHeight);
        if (mMonthPager.getVisibility() != GONE) {
            mMonthPager.layout(0, weekInfoHeight, r, mMonthPager.getMeasuredHeight());
        }
        if (mWeekPager.getVisibility() != GONE) {
            mWeekPager.layout(0, weekInfoHeight, r, mWeekPager.getMeasuredHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
