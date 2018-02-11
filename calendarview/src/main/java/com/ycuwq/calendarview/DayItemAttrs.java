package com.ycuwq.calendarview;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

/**
 * 包装的MonthItemView所需要的属性
 * Created by yangchen on 2017/6/21.
 */
class DayItemAttrs {
	private boolean showLunar = true;          //是否显示农历
	private boolean showHoliday = true;        //是否显示节假日
	private int textSizeTop = 48;                              //日历的日期字体
	private int textSizeBottom = 32;                            //节日，阴历的字体
	private @ColorInt
    int textColorTop = Color.BLACK;        //日历的日期颜色
	private @ColorInt
    int textColorBottom = Color.parseColor("#999999");   //节日，阴历的日期颜色

	private @ColorInt
    int backgroundColor = Color.WHITE;    //背景颜色
	private Drawable clickBg;
	private @ColorInt
    int clickTextColor = Color.WHITE;


	boolean isShowLunar() {
		return showLunar;
	}

	void setShowLunar(boolean showLunar) {
		this.showLunar = showLunar;
	}

	boolean isShowHoliday() {
		return showHoliday;
	}

	void setShowHoliday(boolean showHoliday) {
		this.showHoliday = showHoliday;
	}

	int getTextSizeTop() {
		return textSizeTop;
	}

	void setTextSizeTop(int textSizeTop) {
		this.textSizeTop = textSizeTop;
	}

	int getTextSizeBottom() {
		return textSizeBottom;
	}

	void setTextSizeBottom(int textSizeBottom) {
		this.textSizeBottom = textSizeBottom;
	}

	int getTextColorTop() {
		return textColorTop;
	}

	void setTextColorTop(int textColorTop) {
		this.textColorTop = textColorTop;
	}

	int getTextColorBottom() {
		return textColorBottom;
	}

	void setTextColorBottom(int textColorBottom) {
		this.textColorBottom = textColorBottom;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	Drawable getClickBg() {
		return clickBg;
	}

	void setClickBg(Drawable clickBg) {
		this.clickBg = clickBg;
	}

	int getClickTextColor() {
		return clickTextColor;
	}

	void setClickTextColor(int clickTextColor) {
		this.clickTextColor = clickTextColor;
	}
}
