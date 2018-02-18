package com.ycuwq.calendarview.utils;

import com.ycuwq.calendarview.Date;

import org.junit.Test;

import java.util.List;
import static org.junit.Assert.assertEquals;
/**
 * Created by ycuwq on 2018/2/13.
 */
public class CalendarUtilTest {
    @Test
    public void getMonthOfWeekDate() throws Exception {
        List<List<Date>> lists = CalendarUtil.getMonthOfWeekDate(2018, 2);
        assertEquals(5, lists.size());

        List<List<Date>> lists2 = CalendarUtil.getMonthOfWeekDate(2017, 12);
        assertEquals(5, lists2.size());

        List<List<Date>> lists3 = CalendarUtil.getMonthOfWeekDate(2018, 1);
        assertEquals(6, lists3.size());
    }

    @Test
    public void getWeekDate() throws Exception {
        List<Date> dates = CalendarUtil.getWeekDates(2018, 2, 1);
        assertEquals(29, dates.get(0).getDay());
        assertEquals(4, dates.get(6).getDay());
        List<Date> dates2 = CalendarUtil.getWeekDates(2018, 2, 13);
        assertEquals(12, dates2.get(0).getDay());
        assertEquals(18, dates2.get(6).getDay());
        List<Date> dates3 = CalendarUtil.getWeekDates(2018, 2, 28);
        assertEquals(26, dates3.get(0).getDay());
        assertEquals(4, dates3.get(6).getDay());
    }

}