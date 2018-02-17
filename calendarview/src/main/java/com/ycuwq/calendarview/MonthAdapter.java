package com.ycuwq.calendarview;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import static android.content.ContentValues.TAG;

/**
 * Created by ycuwq on 2018/2/17.
 */

public class MonthAdapter extends PagerAdapter{
    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem: ");
        MonthView monthView;
        monthView = new MonthView(container.getContext());
        monthView.setMonth(2018, 2);
        container.addView(monthView);
        return monthView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
