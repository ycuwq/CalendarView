package com.ycuwq.calendarview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

/**
 * 由于View的样式参数传递的层级太多，这里包装了CalendarView的参数，方便传递。
 * Created by yangchen on 2017/6/21.
 */
class CalendarViewDelegate {

	private boolean mShowLunar = true;          //是否显示农历

	private boolean mShowHoliday = true;        //是否显示节假日

	private int mTextSizeTop = 48;                              //日历的日期字体

	private int mTextSizeBottom = 32;                            //节日，阴历的字体

	private @ColorInt int mTextColorTop = Color.BLACK;        //日历的日期颜色

	private @ColorInt int mTextColorBottom = Color.parseColor("#999999");   //节日，阴历的日期颜色

	private @ColorInt

    int mBackgroundColor = Color.WHITE;    //背景颜色

	private Drawable mSelectedBg;

	private @ColorInt

    int mClickTextColor = Color.WHITE;

	private CalendarView.OnInnerDateSelectedListener mOnInnerDateSelectedListener;

	private int mCalendarMaximumTranslateY;

    /**
     * 当前选中的日期
     */
	private Date mSelectedDate;

	private int startYear, startMonth;

    /**
     * CalendarItem 的高度，也是WeekPager的高度
     */
	private int mCalendarItemRowHeight;

    public CalendarViewDelegate() {
        startYear = 1980;
        startMonth = 1;
    }

    boolean isShowLunar() {
		return mShowLunar;
	}

	void setShowLunar(boolean showLunar) {
		this.mShowLunar = showLunar;
	}

	boolean isShowHoliday() {
		return mShowHoliday;
	}

	void setShowHoliday(boolean showHoliday) {
		this.mShowHoliday = showHoliday;
	}

	int getTextSizeTop() {
		return mTextSizeTop;
	}

	void setTextSizeTop(int textSizeTop) {
		this.mTextSizeTop = textSizeTop;
	}

	int getTextSizeBottom() {
		return mTextSizeBottom;
	}

	void setTextSizeBottom(int textSizeBottom) {
		this.mTextSizeBottom = textSizeBottom;
	}

	int getTextColorTop() {
		return mTextColorTop;
	}

	void setTextColorTop(int textColorTop) {
		this.mTextColorTop = textColorTop;
	}

	int getTextColorBottom() {
		return mTextColorBottom;
	}

	void setTextColorBottom(int textColorBottom) {
		this.mTextColorBottom = textColorBottom;
	}

	public int getBackgroundColor() {
		return mBackgroundColor;
	}

	void setBackgroundColor(int backgroundColor) {
		this.mBackgroundColor = backgroundColor;
	}

	Drawable getSelectedBg() {
		return mSelectedBg;
	}

	void setSelectedBg(Drawable selectedBg) {
		this.mSelectedBg = selectedBg;
	}

	int getClickTextColor() {
		return mClickTextColor;
	}

	void setClickTextColor(int clickTextColor) {
		this.mClickTextColor = clickTextColor;
	}

    public int getCalendarMaximumTranslateY() {
        return mCalendarMaximumTranslateY;
    }

    public void setCalendarMaximumTranslateY(int calendarMaximumTranslateY) {
        mCalendarMaximumTranslateY = calendarMaximumTranslateY;
    }

	public CalendarView.OnInnerDateSelectedListener getOnInnerDateSelectedListener() {
		return mOnInnerDateSelectedListener;
	}

	public void setOnInnerDateSelectedListener(CalendarView.OnInnerDateSelectedListener onInnerDateSelectedListener) {
		mOnInnerDateSelectedListener = onInnerDateSelectedListener;
	}

    public Date getSelectedDate() {
        return mSelectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        mSelectedDate = selectedDate;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getStartMonth() {
        return startMonth;
    }

    public void setStartMonth(int startMonth) {
        this.startMonth = startMonth;
    }

    public int getCalendarItemRowHeight() {
        return mCalendarItemRowHeight;
    }

    public void setCalendarItemRowHeight(int calendarItemRowHeight) {
        mCalendarItemRowHeight = calendarItemRowHeight;
    }
}
