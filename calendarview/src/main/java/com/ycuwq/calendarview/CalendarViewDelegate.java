package com.ycuwq.calendarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;

import com.ycuwq.calendarview.utils.DensityUtil;

import java.util.List;

/**
 * 由于View的样式参数传递的层级太多，这里包装了CalendarView的参数，方便传递。
 * Created by yangchen on 2017/6/21.
 */
class CalendarViewDelegate {

    private boolean mShowLunar;          //是否显示农历

    private boolean mShowHoliday;        //是否显示节假日

    private int mTopTextSize;                              //日历的日期字体

    private int mBottomTextSize;                            //节日，阴历的字体

    private @ColorInt int mTopTextColor;        //日历的日期颜色

    private @ColorInt int mBottomTextColor;   //节日，阴历的日期颜色

    private @ColorInt int mBackgroundColor;    //背景颜色

    private @ColorInt int mSelectedItemColor;

    private @ColorInt int mSelectedTextColor;

    private CalendarView.OnDateSelectedListener mOnInnerDateSelectedListener;

    private List<Date> mSchemes;

    private @ColorInt int mSchemeColor;

    private int mSchemeRadius;

    private @ColorInt int mSelectedSchemeColor;

    private @ColorInt int mWeekInfoBackgroundColor;

    private @ColorInt int mWeekInfoTextColor;

    private int mWeekInfoTextSize;

    private Date mSelectedDate;
    private int startYear, startMonth;

    CalendarViewDelegate(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        startYear = a.getInt(R.styleable.CalendarView_startYear, 1980);
        startMonth = a.getInt(R.styleable.CalendarView_startMonth, 1);
        mShowLunar = a.getBoolean(R.styleable.CalendarView_showLunar, true);
        mShowHoliday = a.getBoolean(R.styleable.CalendarView_showHoliday, true);
        mTopTextSize = a.getDimensionPixelSize(R.styleable.CalendarView_topTextSize,
                DensityUtil.dp2px(context, 16));
        mTopTextColor = a.getColor(R.styleable.CalendarView_topTextColor, Color.BLACK);

        mBottomTextSize = a.getDimensionPixelSize(R.styleable.CalendarView_bottomTextSize,
                DensityUtil.dp2px(context, 12));
        mBottomTextColor = a.getColor(R.styleable.CalendarView_bottomTextColor, Color.parseColor("#999999"));
        mSelectedItemColor = a.getColor(R.styleable.CalendarView_selectedItemColor, Color.GRAY);
        mSelectedTextColor = a.getColor(R.styleable.CalendarView_selectedItemTextColor, Color.WHITE);

        mSchemeRadius = a.getDimensionPixelSize(R.styleable.CalendarView_schemeRadius,
                DensityUtil.dp2px(context, 2));
        mSchemeColor = a.getColor(R.styleable.CalendarView_schemeColor, mSelectedItemColor);
        mSelectedSchemeColor = a.getColor(R.styleable.CalendarView_selectedSchemeColor, mSelectedTextColor);

        mBackgroundColor = a.getColor(R.styleable.CalendarView_monthBackgroundColor, Color.WHITE);

        mWeekInfoBackgroundColor = a.getColor(R.styleable.CalendarView_weekInfoBackgroundColor, Color.WHITE);
        mWeekInfoTextColor = a.getColor(R.styleable.CalendarView_weekInfoTextColor, Color.BLACK);
        mWeekInfoTextSize = a.getColor(R.styleable.CalendarView_weekInfoTextSize,
                DensityUtil.dp2px(context, 16));
        a.recycle();
    }

    public boolean isShowLunar() {
        return mShowLunar;
    }

    public void setShowLunar(boolean showLunar) {
        mShowLunar = showLunar;
    }

    public boolean isShowHoliday() {
        return mShowHoliday;
    }

    public void setShowHoliday(boolean showHoliday) {
        mShowHoliday = showHoliday;
    }

    public int getTopTextSize() {
        return mTopTextSize;
    }

    public void setTopTextSize(int topTextSize) {
        mTopTextSize = topTextSize;
    }

    public int getBottomTextSize() {
        return mBottomTextSize;
    }

    public void setBottomTextSize(int bottomTextSize) {
        mBottomTextSize = bottomTextSize;
    }

    public int getTopTextColor() {
        return mTopTextColor;
    }

    public void setTopTextColor(int topTextColor) {
        mTopTextColor = topTextColor;
    }

    public int getBottomTextColor() {
        return mBottomTextColor;
    }

    public void setBottomTextColor(int bottomTextColor) {
        mBottomTextColor = bottomTextColor;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public int getSelectedItemColor() {
        return mSelectedItemColor;
    }

    public void setSelectedItemColor(int selectedItemColor) {
        mSelectedItemColor = selectedItemColor;
    }

    public int getSelectedTextColor() {
        return mSelectedTextColor;
    }

    public void setSelectedTextColor(int selectedTextColor) {
        mSelectedTextColor = selectedTextColor;
    }

    public CalendarView.OnDateSelectedListener getOnInnerDateSelectedListener() {
        return mOnInnerDateSelectedListener;
    }

    public void setOnInnerDateSelectedListener(CalendarView.OnDateSelectedListener onInnerDateSelectedListener) {
        mOnInnerDateSelectedListener = onInnerDateSelectedListener;
    }

    public List<Date> getSchemes() {
        return mSchemes;
    }

    public void setSchemes(List<Date> schemes) {
        mSchemes = schemes;
    }

    public int getSchemeColor() {
        return mSchemeColor;
    }

    public void setSchemeColor(int schemeColor) {
        mSchemeColor = schemeColor;
    }

    public int getSchemeRadius() {
        return mSchemeRadius;
    }

    public void setSchemeRadius(int schemeRadius) {
        mSchemeRadius = schemeRadius;
    }

    public int getSelectedSchemeColor() {
        return mSelectedSchemeColor;
    }

    public void setSelectedSchemeColor(int selectedSchemeColor) {
        mSelectedSchemeColor = selectedSchemeColor;
    }

    public int getWeekInfoBackgroundColor() {
        return mWeekInfoBackgroundColor;
    }

    public void setWeekInfoBackgroundColor(int weekInfoBackgroundColor) {
        mWeekInfoBackgroundColor = weekInfoBackgroundColor;
    }

    public int getWeekInfoTextColor() {
        return mWeekInfoTextColor;
    }

    public void setWeekInfoTextColor(int weekInfoTextColor) {
        mWeekInfoTextColor = weekInfoTextColor;
    }

    public int getWeekInfoTextSize() {
        return mWeekInfoTextSize;
    }

    public void setWeekInfoTextSize(int weekInfoTextSize) {
        mWeekInfoTextSize = weekInfoTextSize;
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
}
