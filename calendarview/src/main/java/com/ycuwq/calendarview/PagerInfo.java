package com.ycuwq.calendarview;

/**
 * 页选中后调用的信息类
 * Created by ycuwq on 2018/3/15.
 */
public class PagerInfo {
    public static final int TYPE_MONTH = 0;
    public static final int TYPE_WEEK = 1;

    private int type;
    private int year;
    private int month;
    private int mondayDay;

    public PagerInfo(int year, int month) {
        setDate(year, month);
    }

    public PagerInfo(int year, int month, int mondayDay) {
        setDate(year, month, mondayDay);
    }

    private void setDate(int year, int month, int mondayDay) {
        this.year = year;
        this.month = month;
        this.mondayDay = mondayDay;
        type = TYPE_WEEK;
    }

    private void setDate(int year, int month) {
        type = TYPE_MONTH;
        this.year = year;
        this.month = month;
    }


    public int getType() {
        return type;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getMondayDay() {
        return mondayDay;
    }
}
