package com.ycuwq.calendarview;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ycuwq on 2018/2/25.
 */
abstract class BaseAdapter extends PagerAdapter {
    private LinkedList<ItemView> mCache = new LinkedList<>();
    private SparseArray<ItemView> mViews = new SparseArray<>();

    private int mCount;

    private int mStartYear, mStartMonth;
    private CalendarViewDelegate mCalendarViewDelegate;

    public BaseAdapter(int count, int startYear, int startMonth, CalendarViewDelegate calendarViewDelegate) {
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
        ItemView itemView;
        if (mCache.isEmpty()) {
            itemView = new ItemView(container.getContext(), mCalendarViewDelegate);
        } else {
            itemView = mCache.removeFirst();
        }
        itemView.setDateList(getDateList(mStartYear, mStartMonth, position));
        container.addView(itemView);
        return itemView;
    }

    public abstract List<Date> getDateList(int startYear, int startMonth, int position);

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
        mCache.addLast((ItemView) object);
        mViews.remove(position);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
