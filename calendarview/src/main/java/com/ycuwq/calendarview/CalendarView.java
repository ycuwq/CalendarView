package com.ycuwq.calendarview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.ycuwq.calendarview.utils.CalendarUtil;

import org.joda.time.LocalDate;

/**
 * Created by ycuwq on 2018/2/11.
 */
public class CalendarView extends ViewGroup {

    public static final int TYPE_MONTH = 0;
    public static final int TYPE_WEEK = 1;

    private MonthAdapter mMonthAdapter;
    private WeekAdapter mWeekAdapter;
    private CalendarItemPager mWeekPager, mMonthPager;
    private CalendarViewDelegate mCalendarViewDelegate;
    private WeekInfoView mWeekInfoView;
    private int mCalendarType = 0;

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCalendarViewDelegate = new CalendarViewDelegate();
        mCalendarViewDelegate.setSelectedBg(context.getResources().getDrawable(R.drawable.com_ycuwq_calendarview_blue_circle));
        initChild();
        setDateToCurrent();
        mWeekPager.setVisibility(INVISIBLE);
        mMonthPager.setVisibility(VISIBLE);
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
        mWeekInfoView.setTranslationZ(1);
        mWeekPager = new CalendarItemPager(getContext());
        mWeekAdapter = new WeekAdapter(20000, 1980, 1, mCalendarViewDelegate);
        mWeekPager.setOffscreenPageLimit(1);
        mWeekPager.setAdapter(mWeekAdapter);
        addView(mWeekPager, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mWeekPager.setTranslationY(-1);
        mMonthAdapter = new MonthAdapter(20000, 1980, 1, mCalendarViewDelegate);
        mMonthPager = new CalendarItemPager(getContext());
        mMonthPager.setOffscreenPageLimit(1);
        mMonthPager.setAdapter(mMonthAdapter);
        addView(mMonthPager, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mMonthPager.setTranslationY(-1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = mWeekInfoView.getMeasuredHeight();
        if (mMonthPager.getVisibility() != GONE) {
            height += mMonthPager.getMeasuredHeight();
        } else {
            height += mWeekPager.getMeasuredHeight();
        }
       setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int weekInfoHeight = mWeekInfoView.getMeasuredHeight();
        mWeekInfoView.layout(0, 0, r, weekInfoHeight);
        if (mMonthPager.getVisibility() != GONE) {
            mMonthPager.layout(0, weekInfoHeight, r, weekInfoHeight + mMonthPager.getMeasuredHeight());
        }
        if (mWeekPager.getVisibility() != GONE) {
            mWeekPager.layout(0, weekInfoHeight, r, weekInfoHeight + mWeekPager.getMeasuredHeight());
        }
    }

    CalendarItemPager getWeekPager() {
        return mWeekPager;
    }

    CalendarItemPager getMonthPager() {
        return mMonthPager;
    }

    /**
     * 获取日历最大可以滑动的距离
     * 最大滑动距离 = Month的高度 - Week的高度
     * @return
     */
    int getMaximumTranslateY() {
        CalendarItemView calendarItemView;
        if (mMonthPager.getVisibility() != GONE) {
            calendarItemView = mMonthAdapter.getCurrentView();
        } else {
            calendarItemView = mWeekAdapter.getCurrentView();
        }
        return calendarItemView.getItemHeight() * (CalendarItemView.MAX_ROW - 1);
    }

    CalendarViewDelegate getCalendarViewDelegate() {
        return mCalendarViewDelegate;
    }

    public int getCalendarType() {
        return mCalendarType;
    }

    public void setCalendarType(int calendarType) {
        if (calendarType == TYPE_MONTH) {
            setTypeToMonth();
        } else {
            setTypeToWeek();
        }
    }
    public void setTypeToWeek() {
        mCalendarType = TYPE_WEEK;
        mMonthPager.setVisibility(GONE);
        mWeekPager.setVisibility(VISIBLE);
    }

    public void setTypeToMonth() {
        mCalendarType = TYPE_MONTH;
        mMonthPager.setVisibility(VISIBLE);
        mWeekPager.setVisibility(GONE);
    }
}
