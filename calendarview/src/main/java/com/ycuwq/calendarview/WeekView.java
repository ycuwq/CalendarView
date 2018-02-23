package com.ycuwq.calendarview;

import android.content.Context;
import android.support.annotation.NonNull;

import com.ycuwq.calendarview.utils.CalendarUtil;

import java.util.List;

/**
 * 一周的数据
 * Created by ycuwq on 2018/2/11.
 */
public class WeekView extends BaseView {

    public WeekView(Context context, @NonNull CalendarViewDelegate calendarViewDelegate) {
        super(context, calendarViewDelegate);
    }

    public void setWeek(int year, int month, int day) {
        List<Date> lists = CalendarUtil.getWeekDates(year, month, day);
        setDateList(lists);
    }
}
