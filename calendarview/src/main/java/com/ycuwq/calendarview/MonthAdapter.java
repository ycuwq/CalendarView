package com.ycuwq.calendarview;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.ycuwq.calendarview.utils.CalendarUtil;

import java.util.LinkedList;

/**
 * Created by ycuwq on 2018/2/17.
 */
public class MonthAdapter extends PagerAdapter {

    private LinkedList<MonthView> mCache = new LinkedList<>();
    private SparseArray<MonthView> mViews = new SparseArray<>();

    private int mCount;

    private int mStartYear, mStartMonth;
    private CalendarViewDelegate mCalendarViewDelegate;

    public MonthAdapter(int count, int startYear, int startMonth, CalendarViewDelegate calendarViewDelegate) {
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
        MonthView monthView;
        if (mCache.isEmpty()) {
            monthView = new MonthView(container.getContext(), mCalendarViewDelegate);
        } else {
            monthView = mCache.removeFirst();
        }
        int[] date = CalendarUtil.positionToDate(position, mStartYear, mStartMonth);
        monthView.setMonth(date[0], date[1]);
        container.addView(monthView);
        return monthView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        mCache.addLast((MonthView) object);
        mViews.remove(position);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
