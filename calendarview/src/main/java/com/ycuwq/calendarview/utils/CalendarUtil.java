package com.ycuwq.calendarview.utils;

import com.ycuwq.calendarview.Date;

import org.joda.time.LocalDate;
import org.joda.time.Weeks;

import java.util.ArrayList;
import java.util.Calendar;
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
        setupLunarData(date);
        return date;
    }

    static void setupLunarData(Date date) {
        String[] lunar = LunarUtil.getLunarInfo(date.getYear(), date.getMonth(), date.getDay());
        date.setLunarMonth(lunar[0]);
        date.setLunarDay(lunar[1]);
        date.setLunarHoliday(lunar[2]);
        date.setTerm(lunar[3]);
    }

    public static List<List<Date>> getMonthOfWeekDates(int year, int month) {
//        long curTime = System.currentTimeMillis();
        LocalDate localDate = new LocalDate(year, month, 1);
        List<List<Date>> weeks = new ArrayList<>();
        while (localDate.getMonthOfYear() <= month && localDate.getYear() == year) {
            weeks.add(getWeekDates(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth()));
            localDate = localDate.plusWeeks(1).withDayOfWeek(1);

        }
        return weeks;
    }

    public static List<Date> getMonthDates(int year, int month) {
//        long curTime = System.currentTimeMillis();
        LocalDate localDate = new LocalDate(year, month, 1);
        List<Date> dates = new ArrayList<>();
        while (localDate.getMonthOfYear() <= month && localDate.getYear() == year) {
            dates.addAll(getWeekDates(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth()));
            localDate = localDate.plusWeeks(1).withDayOfWeek(1);
        }
        return dates;
    }


    public static List<Date> getWeekDates(int year, int month, int day) {
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

    /**
     * 获取时期的星期几
     */
    public static int getDayForWeek(int y, int m, int d) {
        Calendar calendar = Calendar.getInstance();
        //Month是从0开始算的，所以要-1
        calendar.set(y, m -1, d);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 根据ViewPager position 得到对应年月
     * @param position  开始年月的延后月份
     */
    public static int[] positionToDate(int position, int startY, int startM) {
        int year = position / 12 + startY;
        int month = position % 12 + startM;

        if (month > 12) {
            month = month % 12;
            year = year + 1;
        }
        return new int[]{year, month};
    }

    /**
     * 获取两个日期的月分之间的差
     * @return 第二个 - 第一个
     */
    public static int getMonthPosition(int year1, int month1, int year2, int month2) {
        int year = year2 - year1;
        int month = month2 - month1;

        return year * 12 + month;
    }

    /**
     * 获取两个日期之间的周数差
     * @param startYear     开始年
     * @param startMonth    开始月
     * @return  周数差。
     */
    public static int getWeekPosition(int startYear, int startMonth, int startDay, int year, int month, int day) {
        LocalDate start = new LocalDate(startYear, startMonth, startDay).withDayOfWeek(1);
        LocalDate end = new LocalDate(year, month, day).withDayOfWeek(1);
        return Weeks.weeksBetween(start, end).getWeeks();
    }

    /**
     * 获取距离开始日期 + 周数的一周的日期。
     * @param startYear     开始年
     * @param startMonth    开始月
     * @param positionWeek  偏移周数
     * @return  返回一周的日期，第一天是周日
     */
    public static List<Date> getWeekDaysForPosition(int startYear, int startMonth, int startDay, int positionWeek) {
        ArrayList<Date> dates = new ArrayList<>();

        LocalDate localDate = new LocalDate(startYear, startMonth, startDay).plusWeeks(positionWeek);

        for (int i = 1; i <= 7; i++) {
            dates.add(getDate(localDate.withDayOfWeek(i), Date.TYPE_THIS_MONTH));
        }
        return dates;
    }

    public static LocalDate getCurrentDate() {
        return new LocalDate();
    }
}
