package com.ycuwq.calendarview;

import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * 日期的实体类
 * Created by ycuwq on 2017/5/18.
 */
public class Date {
	//用来标识type类型。
	public final static int TYPE_LAST_MONTH = -1;
	public final static int TYPE_THIS_MONTH = 0;
	public final static int TYPE_NEXT_MONTH = 1;

	private int year;
	private int month;
	private int day;
	private int week;               //是该星期的第几天
	private String holiday;         //节假日

	private String lunarMonth;      //农历月
	private String lunarDay;        //农历日
	private String lunarHoliday;     //农历节日
	private int type;               //类型，0 - 当月，-1 - 上月， 1 - 下月；
	private String term;            //节气
	public Date() {
	}

	public Date(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
		type = TYPE_THIS_MONTH;
	}

	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getHoliday() {
		return holiday;
	}

	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}

	public String getLunarMonth() {
		return lunarMonth;
	}

	public void setLunarMonth(String lunarMonth) {
		this.lunarMonth = lunarMonth;
	}

	public String getLunarDay() {
		return lunarDay;
	}

	public void setLunarDay(String lunarDay) {
		this.lunarDay = lunarDay;
	}

	public String getLunarHoliday() {
		return lunarHoliday;
	}

	public void setLunarHoliday(String lunarHoliday) {
		this.lunarHoliday = lunarHoliday;
	}

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getDate(DateFormat dateFormat) {
		Calendar calendar = Calendar.getInstance();
		//Calendar月份是从第0个月开始算的，所以要减一
		calendar.set(year, month -1, day);
		return dateFormat.format(calendar.getTime());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (obj instanceof Date) {
			Date other = (Date) obj;
			if (other.year == year && other.month == month && other.day == day && other.type == type) {
				return true;
			}
		}
		return false;
	}


	public boolean isBefore(@NonNull Date other) {
		if (year == other.year) {
			return ((month == other.month) ? (day < other.day) : (month < other.month));
		} else {
			return year < other.year;
		}
	}

	public boolean isAfter(@NonNull Date other) {

		if (year == other.year) {
			return (month == other.month) ? (day > other.day) : (month > other.month);
		} else {
			return year > other.year;
		}
	}


	@Override
	public String toString() {
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.applyPattern("00");
		return year + "-" + decimalFormat.format(month) + "-" + decimalFormat.format(day);
	}
}
