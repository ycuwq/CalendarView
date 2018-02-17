package com.ycuwq.calendarview.utils;

import com.ycuwq.calendarview.Date;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * 日期处理的帮助类
 * Created by ycuwq on 2018/2/13.
 */
public class CalendarUtil {

    public static Date getDate(LocalDate localDate, int type) {
        int year = localDate.getYear(), month = localDate.getMonthOfYear(), day = localDate.getDayOfMonth();
        Date date = new Date(year, month, day);
        date.setWeek(localDate.getDayOfWeek());
        date.setHoliday(SolarUtil.getSolarHoliday(year, month, day));
        date.setType(type);
        String[] lunar = LunarUtil.solarToLunar(year, month, day);
        date.setLunarMonth(lunar[0]);
        date.setLunarDay(lunar[1]);
        date.setLunarHoliday(lunar[2]);
        return date;
    }


    public static List<List<Date>> getMonthOfWeekDate(int year, int month) {
        LocalDate localDate = new LocalDate(year, month, 1);
        List<List<Date>> weeks = new ArrayList<>();
        while (localDate.getMonthOfYear() <= month) {
            weeks.add(getWeekDate(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth()));
            localDate = localDate.plusWeeks(1).withDayOfWeek(1);
        }
        return weeks;
    }

    public static List<Date> getWeekDate(int year, int month, int day) {
        List<Date> dates = new ArrayList<>();
        LocalDate localDate = new LocalDate(year, month, day);
        for (int i = 1; i <= 7; i++) {
            LocalDate tempDate = localDate.withDayOfWeek(i);
            int tempMonth = tempDate.getMonthOfYear();
            int type;
            if (tempMonth == month) {
                type = Date.TYPE_THIS_MONTH;
            } else if (tempMonth > month) {
                type = Date.TYPE_NEXT_MONTH;
            } else {
                type = Date.TYPE_LAST_MONTH;
            }
            dates.add(getDate(tempDate, type));
        }
        return dates;
    }
}
