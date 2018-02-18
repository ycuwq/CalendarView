package com.ycuwq.calendarview.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ycuwq on 2018/2/18.
 */
public class LunarUtilTest {
    @Test
    public void solarToLunar() throws Exception {
        String[] lunar = LunarUtil.solarToLunar(2017, 5, 30);
        assertEquals(lunar[0], "五月");
        assertEquals(lunar[1], "初五");
    }
}