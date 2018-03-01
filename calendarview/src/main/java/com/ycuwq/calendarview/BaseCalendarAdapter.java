package com.ycuwq.calendarview;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * MonthAdapter和WeekAdapter的基类。
 * Created by ycuwq on 2018/2/25.
 */
abstract class BaseCalendarAdapter extends PagerAdapter {
    private LinkedList<CalendarItemView> mCache = new LinkedList<>();
    private SparseArray<CalendarItemView> mViews = new SparseArray<>();

    private int mCount;

    private int mStartYear, mStartMonth;
    private CalendarViewDelegate mCalendarViewDelegate;
    private CalendarItemView mCurrentView;

    BaseCalendarAdapter(int count, int startYear, int startMonth, CalendarViewDelegate calendarViewDelegate) {
        mCount = count;
        mStartYear = startYear;
        mStartMonth = startMonth;
        mCalendarViewDelegate = calendarViewDelegate;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        CalendarItemView calendarItemView;
        if (mCache.isEmpty()) {
            calendarItemView = new CalendarItemView(container.getContext(), mCalendarViewDelegate);
        } else {
            calendarItemView = mCache.removeFirst();
        }
        calendarItemView.setDateList(getDateList(mStartYear, mStartMonth, position));
        container.addView(calendarItemView);
        return calendarItemView;
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        mCurrentView = (CalendarItemView) object;
    }

    protected abstract List<Date> getDateList(int startYear, int startMonth, int position);

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        mCache.addLast((CalendarItemView) object);
        mViews.remove(position);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public CalendarItemView getCurrentView() {
        return mCurrentView;
    }
}
