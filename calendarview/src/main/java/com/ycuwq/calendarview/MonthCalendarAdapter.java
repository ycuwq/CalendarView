package com.ycuwq.calendarview;

import com.ycuwq.calendarview.utils.CalendarUtil;

import java.util.List;

/**
 * Created by ycuwq on 2018/2/17.
 */
public class MonthCalendarAdapter extends BaseCalendarAdapter {

    public MonthCalendarAdapter(int count, int startYear, int startMonth, CalendarViewDelegate calendarViewDelegate) {
        super(count, startYear, startMonth, calendarViewDelegate);
    }

    @Override
    public List<Date> getDateList(int startYear, int startMonth, int position) {
        int[] date = CalendarUtil.positionToDate(position, startYear, startMonth);

        return CalendarUtil.getMonthDates(date[0], date[1]);
    }

}
