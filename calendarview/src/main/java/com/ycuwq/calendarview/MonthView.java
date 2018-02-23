package com.ycuwq.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;

import com.ycuwq.calendarview.utils.CalendarUtil;

import java.util.List;

/**
 * 一月的数据
 * Created by ycuwq on 2018/2/12.
 */
@SuppressLint("ViewConstructor")
class MonthView extends BaseView {

    public MonthView(Context context, CalendarViewDelegate calendarViewDelegate) {
        super(context, calendarViewDelegate);
    }

    public void setMonth(int year, int month) {
        List<Date> lists = CalendarUtil.getMonthOfWeekDate2(year, month);
        setDateList(lists);
    }
}
