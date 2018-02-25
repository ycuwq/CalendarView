package com.ycuwq.calendarview;

import com.ycuwq.calendarview.utils.CalendarUtil;

import java.util.List;

/**
 * Created by ycuwq on 2018/2/17.
 */
public class MonthAdapter extends BaseAdapter {


    public MonthAdapter(int count, int startYear, int startMonth, CalendarViewDelegate calendarViewDelegate) {
        super(count, startYear, startMonth, calendarViewDelegate);
    }

    @Override
    public List<Date> getDateList(int startYear, int startMonth, int position) {
        int[] date = CalendarUtil.positionToDate(position, startYear, startMonth);
        List<Date> dates = CalendarUtil.getMonthOfWeekDate2(date[0], date[1]);
        return dates;
    }
}
