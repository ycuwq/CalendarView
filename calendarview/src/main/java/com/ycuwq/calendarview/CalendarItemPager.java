package com.ycuwq.calendarview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Month和Week的页视图
 * Created by ycuwq on 2018/2/28.
 */
public class CalendarItemPager extends ViewPager {

    public CalendarItemPager(@NonNull Context context) {
        super(context);
    }

    void scrollToDate(int year, int month, int date, boolean smoothScroll) {

    }

    void updateScheme() {
        for (int i = 0; i < getChildCount(); i++) {
            CalendarItemView view = (CalendarItemView) getChildAt(i);
            view.updateScheme();
        }
    }

    /**
     * ViewPager高度不能适应子View的大小，所以重写onMeasure
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        //下面遍历所有child的高度
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) //采用最大的view的高度。
                height = h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
