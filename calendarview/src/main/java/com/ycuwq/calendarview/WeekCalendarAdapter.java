package com.ycuwq.calendarview;

import com.ycuwq.calendarview.utils.CalendarUtil;

import java.util.List;

/**
 * Created by ycuwq on 2018/2/25.
 */
public class WeekCalendarAdapter extends BaseCalendarAdapter {
    public WeekCalendarAdapter(int count, int startYear, int startMonth, CalendarViewDelegate calendarViewDelegate) {
        super(count, startYear, startMonth, calendarViewDelegate);
    }

    @Override
    public List<Date> getDateList(int startYear, int startMonth, int position) {
        return CalendarUtil.getWeekDaysForPosition(startYear, startMonth, 1, position);
    }

}
