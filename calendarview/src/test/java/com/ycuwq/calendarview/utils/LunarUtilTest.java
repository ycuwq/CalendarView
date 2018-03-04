package com.ycuwq.calendarview.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ycuwq on 2018/2/18.
 */
public class LunarUtilTest {
    @Test
    public void solarToLunar() throws Exception {
        int[] lunar = LunarUtil.solarToLunar(2018, 3, 5);
        assertEquals(lunar[0], 2018);
        assertEquals(lunar[1], 1);
        assertEquals(lunar[2], 18);
        assertEquals("", LunarUtil.getLunarHoliday(lunar[0], lunar[1], lunar[2]));

        lunar = LunarUtil.solarToLunar(2018, 3, 2);
        assertEquals(lunar[0], 2018);
        assertEquals(lunar[1], 1);
        assertEquals(lunar[2], 15);
        assertEquals("元宵节", LunarUtil.getLunarHoliday(lunar[0], lunar[1], lunar[2]));

        assertEquals("惊蛰", LunarUtil.getSolarTerm(2018, 3,5));
        assertEquals("夏至", LunarUtil.getSolarTerm(2017, 6,21));
    }
}