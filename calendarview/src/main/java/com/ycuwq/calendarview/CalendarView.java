package com.ycuwq.calendarview;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.ycuwq.calendarview.utils.CalendarUtil;

import org.joda.time.LocalDate;

import java.util.Calendar;
import java.util.List;

/**
 * 日历的主体类
 * Created by ycuwq on 2018/2/11.
 */
@SuppressWarnings("unused")
public class CalendarView extends ViewGroup {

    public static final int TYPE_MONTH = 0;
    public static final int TYPE_WEEK = 1;

    private MonthCalendarAdapter mMonthAdapter;
    private WeekCalendarAdapter mWeekAdapter;
    private CalendarItemPager mWeekPager, mMonthPager;
    private CalendarViewDelegate mCalendarViewDelegate;
    private WeekInfoView mWeekInfoView;
    private int mCalendarType = 0;

    private OnDateSelectedListener mOnDateSelectedListener;

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //切换Page后，将选择的日期变成当前页的日期。
            CalendarItemView calendarItemView;
            if (mCalendarType == TYPE_MONTH) {
                calendarItemView = mMonthPager.findViewWithTag(position);
            } else {
                calendarItemView = mWeekPager.findViewWithTag(position);
            }
            if (calendarItemView == null) {
                return;
            }
            Date lastSelectedDate = mCalendarViewDelegate.getSelectedDate();
            if (getCalendarType() == TYPE_MONTH) {
                //这里15只要是中间的任意值就可以，目的是保证获取的是当前月份的日期，不是上月或者下月。
                Date date = calendarItemView.getDates().get(15);
                int day = lastSelectedDate.getDay();
                //在翻页的时候有可能会出现之前选中的日期超过当前月的最大天数，如果超过的话选择当前月的最后一天替代。
                if (lastSelectedDate.getDay() > 28) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(lastSelectedDate.getYear(), lastSelectedDate.getMonth() - 1, 1);
                    int maxDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    day = maxDayOfMonth < lastSelectedDate.getDay() ? maxDayOfMonth :
                            lastSelectedDate.getDay();
                }
                calendarItemView.selectDate(new Date(date.getYear(), date.getMonth(), day));
            } else {
                //周日期可直接用第几个View跳转。week的范围是1-7，所以减一
                calendarItemView.selectDate(lastSelectedDate.getWeek() - 1);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public CalendarView(Context context) {
        this(context, null);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCalendarViewDelegate = new CalendarViewDelegate(context, attrs);
        mCalendarViewDelegate.setOnInnerDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                mCalendarViewDelegate.setSelectedDate(date);
                if (mCalendarType == TYPE_WEEK) {
                    scrollMonthToDate(date.getYear(), date.getMonth(), date.getDay(), false);
                } else {
                    scrollWeekToDate(date.getYear(), date.getMonth(), date.getDay(), false);
                }
                if (mOnDateSelectedListener != null) {
                    mOnDateSelectedListener.onDateSelected(date);
                }
            }
        });

        initChild();
        setDateToCurrent();
        mWeekPager.setVisibility(INVISIBLE);
        mMonthPager.setVisibility(VISIBLE);
    }

    private void setDateToCurrent() {
        final LocalDate localDate = new LocalDate();
        mCalendarViewDelegate.setSelectedDate(new Date(localDate.getYear(),
                localDate.getMonthOfYear(), localDate.getDayOfMonth()));
        post(new Runnable() {
            @Override
            public void run() {
                scrollToDate(localDate.getYear(), localDate.getMonthOfYear(),
                        localDate.getDayOfMonth(), false);
            }
        });

    }

    private void initChild() {
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mWeekInfoView = new WeekInfoView(getContext(), mCalendarViewDelegate);
        addView(mWeekInfoView, 0, layoutParams);
        mWeekInfoView.setTranslationZ(1);

        mMonthAdapter = new MonthCalendarAdapter(20000, mCalendarViewDelegate.getStartYear(),
                mCalendarViewDelegate.getStartMonth(), mCalendarViewDelegate);
        mMonthPager = new CalendarItemPager(getContext());
        mMonthPager.setOffscreenPageLimit(1);
        mMonthPager.setAdapter(mMonthAdapter);
        addView(mMonthPager, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        mWeekPager = new CalendarItemPager(getContext());
        mWeekAdapter = new WeekCalendarAdapter(20000, mCalendarViewDelegate.getStartYear(),
                mCalendarViewDelegate.getStartMonth(), mCalendarViewDelegate);
        mWeekPager.setOffscreenPageLimit(1);
        mWeekPager.setAdapter(mWeekAdapter);
        addView(mWeekPager, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mMonthPager.addOnPageChangeListener(mOnPageChangeListener);
        mWeekPager.addOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = mWeekInfoView.getMeasuredHeight();
        if (mMonthPager.getVisibility() != GONE) {
            height += mMonthPager.getMeasuredHeight();
        } else {
            height += mWeekPager.getMeasuredHeight();
        }
       setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int weekInfoHeight = mWeekInfoView.getMeasuredHeight();
        mWeekInfoView.layout(0, 0, r, weekInfoHeight);
        if (mMonthPager.getVisibility() != GONE) {
            mMonthPager.layout(0, weekInfoHeight, r, weekInfoHeight + mMonthPager.getMeasuredHeight());
        }
        if (mWeekPager.getVisibility() != GONE) {
            mWeekPager.layout(0, weekInfoHeight, r, weekInfoHeight + mWeekPager.getMeasuredHeight());
        }
    }

    CalendarItemPager getWeekPager() {
        return mWeekPager;
    }

    CalendarItemPager getMonthPager() {
        return mMonthPager;
    }

    CalendarViewDelegate getCalendarViewDelegate() {
        return mCalendarViewDelegate;
    }

    void scrollToSelectedDate() {
        scrollToDate(mCalendarViewDelegate.getSelectedDate(), false);
    }


    void scrollToDate(Date date, boolean smoothScroll) {
        scrollToDate(date.getYear(), date.getMonth(), date.getDay(), smoothScroll);
    }

    void scrollWeekToDate(int year, int month, int day, boolean smoothScroll) {
        int position = CalendarUtil.getWeekPosition(mCalendarViewDelegate.getStartYear(),
                mCalendarViewDelegate.getStartMonth(), 1,
                year, month, day);
        mWeekPager.setCurrentItem(position, smoothScroll);
        CalendarItemView calendarItemView = mWeekPager.findViewWithTag(position);
        if (calendarItemView != null) {
            calendarItemView.selectDate(new Date(year, month, day));
        }
    }
    void scrollMonthToDate(int year, int month, int day, boolean smoothScroll) {
        int position = CalendarUtil.getMonthPosition(mCalendarViewDelegate.getStartYear(),
                mCalendarViewDelegate.getStartMonth(), year, month);
        mMonthPager.setCurrentItem(position, smoothScroll);
        CalendarItemView calendarItemView = mMonthPager.findViewWithTag(position);
        if (calendarItemView != null) {
            calendarItemView.selectDate(new Date(year, month, day));
        }
    }

    @Nullable
    CalendarItemView getCurrentCalendarItemView() {
        CalendarItemView calendarItemView;
        if (mCalendarType == TYPE_MONTH) {
            calendarItemView = mMonthPager.findViewWithTag(mMonthPager.getCurrentItem());
        } else {
            calendarItemView = mWeekPager.findViewWithTag(mWeekPager.getCurrentItem());
        }
        return calendarItemView;
    }

    public int getCalendarType() {
        return mCalendarType;
    }

    /**
     * 设置日历模式
     * @param calendarType TYPE_MONTH 或则 TYPE_WEEK
     */
    public void setCalendarType(int calendarType) {
        if (calendarType == TYPE_MONTH) {
            setTypeToMonth();
        } else {
            setTypeToWeek();
        }
    }

    /**
     * 设置日历模式到周模式
     */
    public void setTypeToWeek() {
        if (mCalendarType == TYPE_WEEK) {
            return;
        }
        mCalendarType = TYPE_WEEK;
        mMonthPager.setTranslationY(0);
        mMonthPager.setVisibility(GONE);
        mWeekPager.setVisibility(VISIBLE);
        scrollToSelectedDate();
    }

    /**
     * 设置日历模式到月模式
     */
    public void setTypeToMonth() {
        if (mCalendarType == TYPE_MONTH) {
            return;
        }
        mCalendarType = TYPE_MONTH;
        mMonthPager.setTranslationY(0);
        mMonthPager.setVisibility(VISIBLE);
        mWeekPager.setVisibility(GONE);
        scrollToSelectedDate();
    }

    /**
     * 设置事项标记
     * @param schemes 事件标记列表
     */
    public void setSchemes(List<Date> schemes) {
        mCalendarViewDelegate.setSchemes(schemes);
        mMonthPager.updateScheme();
        mWeekPager.updateScheme();
    }

    /**
     * 跳转到日期
     * @param year 年
     * @param month 月
     * @param day 日
     * @param smoothScroll 是否平滑滚动
     */
    public void scrollToDate(int year, int month, int day, boolean smoothScroll) {
        if (mMonthPager.getVisibility() == VISIBLE) {
            scrollMonthToDate(year, month, day, smoothScroll);
            scrollWeekToDate(year, month, day, false);
        } else {
            scrollMonthToDate(year, month, day, false);
            scrollWeekToDate(year, month, day, smoothScroll);
        }
    }

    private void updateCalendar() {
        CalendarItemView calendarItemView = getCurrentCalendarItemView();
        if (calendarItemView != null) {
            calendarItemView.postInvalidate();
        }

    }

    public void setShowLunar(boolean showLunar) {
        mCalendarViewDelegate.setShowLunar(showLunar);
        updateCalendar();
    }

    public void setShowHoliday(boolean showHoliday) {
        mCalendarViewDelegate.setShowHoliday(showHoliday);
        updateCalendar();
    }

    public void setTopTextSize(int textSize) {
        mCalendarViewDelegate.setTopTextSize(textSize);
        updateCalendar();
    }

    public void setTopTextColor(@ColorInt int color) {
        mCalendarViewDelegate.setTopTextColor(color);
        updateCalendar();
    }

    public void setBottomTextSize(int textSize) {
        mCalendarViewDelegate.setBottomTextSize(textSize);
        updateCalendar();
    }

    public void setBottomTextColor(@ColorInt int color) {
        mCalendarViewDelegate.setBottomTextColor(color);
        updateCalendar();
    }

    public void setSelectedItemColor(@ColorInt int color) {
        mCalendarViewDelegate.setSelectedItemColor(color);
        updateCalendar();
    }

    public void setSelectedTextColor(@ColorInt int color) {
        mCalendarViewDelegate.setSelectedTextColor(color);
        updateCalendar();
    }

    public void setSchemeColor(@ColorInt int color) {
        mCalendarViewDelegate.setSchemeColor(color);
        updateCalendar();
    }

    public void setSchemeRadius(int radius) {
        mCalendarViewDelegate.setSchemeRadius(radius);
        updateCalendar();
    }

    public void setSelectedSchemeColor(@ColorInt int color) {
        mCalendarViewDelegate.setSelectedSchemeColor(color);
    }

    public void setWeekInfoBackgroundColor(@ColorInt int color) {
        mCalendarViewDelegate.setWeekInfoBackgroundColor(color);
        mWeekInfoView.postInvalidate();
    }

    public void setWeekInfoTextSize(int weekInfoTextSize) {
        mCalendarViewDelegate.setWeekInfoTextSize(weekInfoTextSize);
        mWeekInfoView.postInvalidate();
    }

    public void setWeekInfoTextColor(@ColorInt int color) {
        mCalendarViewDelegate.setWeekInfoTextColor(color);
        mWeekInfoView.postInvalidate();
    }

    public void setOnDateSelectedListener(OnDateSelectedListener onDateSelectedListener) {
        mOnDateSelectedListener = onDateSelectedListener;
    }

    public interface OnDateSelectedListener {
        void onDateSelected(Date date);
    }

}
