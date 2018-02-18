package com.ycuwq.calendarview.utils;

import android.util.Log;

import com.ycuwq.calendarview.Date;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

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
        long curTime = System.currentTimeMillis();
        String[] lunar = LunarUtil.solarToLunar(year, month, day);
        Log.d(TAG, "lunar time: " +  (System.currentTimeMillis() - curTime));

        date.setLunarMonth(lunar[0]);
        date.setLunarDay(lunar[1]);
        date.setLunarHoliday(lunar[2]);

        return date;
    }

//    public static Date getDate(int year, int month, int day, int type) {
//        Date date = new Date(year, month, day);
//        date.setHoliday(SolarUtil.getSolarHoliday(year, month, day));
//        date.setType(type);
//        String[] lunar = LunarUtil.solarToLunar(year, month, day);
//        date.setLunarMonth(lunar[0]);
//        date.setLunarDay(lunar[1]);
//        date.setLunarHoliday(lunar[2]);
//        date.setWeek(getDayForWeek(year, month, day));
//        return date;
//    }

    public static List<List<Date>> getMonthOfWeekDate(int year, int month) {
//        long curTime = System.currentTimeMillis();
        LocalDate localDate = new LocalDate(year, month, 1);
        List<List<Date>> weeks = new ArrayList<>();
        while (localDate.getMonthOfYear() <= month && localDate.getYear() == year) {
            weeks.add(getWeekDates(localDate.getYear(), localDate.getMonthOfYear(), localDate.getDayOfMonth()));
            localDate = localDate.plusWeeks(1).withDayOfWeek(1);

        }
        return weeks;
    }

//    /**
//     * 返回当前日期一周的数据
//     * @return
//     */
//    public static List<Date> getWeekDates(int startYear, int startMonth, int startDay) {
//        ArrayList<Date> dates = new ArrayList<>();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
//        calendar.set(startYear, startMonth -1, startDay);
//        int dayOFWeek = calendar.get(Calendar.DAY_OF_WEEK);
//        //获取上周最后一天的日期
//        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - dayOFWeek);
//        for (int i = 0; i < 7; i++) {
//            //从上周的最后一天开始每次+1，
//            calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
//
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH) + 1;
//            int date = calendar.get(Calendar.DATE);
//            int type;
//            //月的类型，当月、上月、下月
//            if (startMonth == 12 && month == 1) {
//                type = Date.TYPE_NEXT_MONTH;
//            } else if (startMonth == 1 && month == 12){
//                type = Date.TYPE_LAST_MONTH;
//            } else if (startMonth > month) {
//                type = Date.TYPE_LAST_MONTH;
//            } else if (startMonth < month) {
//                type = Date.TYPE_NEXT_MONTH;
//            } else {
//                type = Date.TYPE_THIS_MONTH;
//            }
//            dates.add(getDate(year, month, date, type));
//        }
//        return dates;
//    }

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

    public static LocalDate getCurrentDate() {
        return new LocalDate();
    }
}
