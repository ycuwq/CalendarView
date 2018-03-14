package com.ycuwq.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.List;

/**
 * MonthView和WeekView的基类
 * Created by ycuwq on 2018/2/23.
 */
@SuppressLint("ViewConstructor")
class CalendarItemView extends View {

    static final int MAX_ROW = 6;       //最大显示的行数

    static final int MAX_COLUMN = 7;        //显示的列数

    private Paint mPaint;

    private List<Date> mDates;

    private CalendarViewDelegate mCalendarViewDelegate;

    /**
     * 在设置Data后显示的行数
     */
    private int mRow;

    private int mItemWidth, mItemHeight;

    private int mFirstItemDrawX, mFirstItemDrawY;

    /**
     * 该控件绘制的大小
     */
    private Rect mDrawnRect;

    private int mTouchSlop;

    /**
     * 日历一个月理论最大行数是6，当当前月的行数小于6时，为使高度通体，将多的部分平均分到每一个间隔上。
     */
    private int mItemHeightSpace;

    private float mTouchDownX, mTouchDownY;

    private int mSelectedItemPosition = -1;
    private List<Date> mSchemes;
    public CalendarItemView(Context context, @NonNull CalendarViewDelegate calendarViewDelegate) {
        super(context);
        mCalendarViewDelegate = calendarViewDelegate;
        initPaint();
        mDrawnRect = new Rect();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        super.setBackgroundColor(mCalendarViewDelegate.getBackgroundColor());
    }

    public void setDateList(List<Date> dates) {
        //强制要求日期必须是MAX_COLUMN的整数
        if (dates == null || dates.size() == 0 || dates.size() % MAX_COLUMN != 0) {
            throw new IllegalArgumentException("dates must be divisible by" + MAX_COLUMN);
        }
        mSelectedItemPosition = -1;
        //判断在月模式下，当前选中的日期是否在当前日期的同一个月，如果在同一个月尝试查找位置。
        if (dates.size() > MAX_COLUMN) { //周模式dates的大小不能大于MAX_COLUMN
            Date date = dates.get(MAX_COLUMN - 1);
            Date selectedDate = mCalendarViewDelegate.getSelectedDate();
            if (selectedDate != null &&selectedDate.getYear() == date.getYear() &&
                    selectedDate.getMonth() == date.getMonth()) {
                mSelectedItemPosition = dates.indexOf(selectedDate);
            }
        }
        mDates = dates;
        mRow = mDates.size() / MAX_COLUMN;
        mSchemes = null;
        requestLayout();
    }

    public List<Date> getDates() {
        return mDates;
    }

    /**
     * 判断date是否在该View内
     * @param date 给定的日期
     * @return 如果在内返回position，否则返回-1
     */
    public int indexThisView(Date date) {
        if (mDates == null || date == null) {
            return -1;
        }
        if (mDates.size() <= 7) {
            return mDates.indexOf(date);

        } else if (mDates.size() > 7) {
            //这里15只要是中间的任意值就可以，目的是保证获取的是当前月份的日期，不是上月或者下月。
            Date month = mDates.get(15);
            if (month.getYear() == date.getYear() && month.getMonth() == date.getMonth()) {
                return mDates.indexOf(date);
            }
        }
        return -1;
    }

    public void setScheme(List<Date> schemes) {
        if (mSchemes == schemes) {
            return;
        }
        for (Date date : mDates) {
            if (schemes != null && schemes.indexOf(date) >= 0) {
                date.setScheme(true);
            } else {
                date.setScheme(false);
            }
        }
        postInvalidate();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mCalendarViewDelegate.getTopTextSize());
    }


    public void selectDate(Date date) {
        int position = mDates.indexOf(date);
        if (position >=0) {
            selectDate(position);
        } else {
            cancelSelected();
        }
    }

    public void selectDate(int position) {
        if (position >= 0 && position < mDates.size()) {
            if (mSelectedItemPosition != -1 && mCalendarViewDelegate.getSelectedDate() == getDates().get(mSelectedItemPosition)) {
                return;
            }
            mSelectedItemPosition = position;
            postInvalidate();
            if (mCalendarViewDelegate.getOnInnerDateSelectedListener() != null) {
                mCalendarViewDelegate.getOnInnerDateSelectedListener()
                        .onDateSelected((mDates.get(position)));
            }
        }
    }

    public void cancelSelected() {
        if (mSelectedItemPosition > -1) {
            mSelectedItemPosition = -1;
            postInvalidate();
        }
    }

    /**
     *  计算实际的大小
     * @param specMode 测量模式
     * @param specSize 测量的大小
     * @param size     需要的大小
     * @return 返回的数值
     */
    private int measureSize(int specMode, int specSize, int size) {
        if (specMode == MeasureSpec.EXACTLY) {
            return specSize;
        } else {
            return Math.min(specSize, size);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);

        int itemWidth = widthSpecSize / MAX_COLUMN;

        int height;
        if (mRow == 1) {
            height = itemWidth;
        } else {
            height = itemWidth * MAX_ROW;
        }
        setMeasuredDimension(widthMeasureSpec, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawnRect.set(getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        mItemWidth = mDrawnRect.width() / MAX_COLUMN;
        if (mRow == 1) {
            mItemHeight = mDrawnRect.height();
        } else {
            mItemHeight = mDrawnRect.height() / MAX_ROW;
        }

        mFirstItemDrawX = mItemWidth / 2;
        mFirstItemDrawY = (int) ((mItemHeight - (mPaint.ascent() + mPaint.descent())) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mItemHeightSpace = (MAX_ROW - mRow) * mItemHeight / mRow;
        if (mSelectedItemPosition >= 0 && mSelectedItemPosition < mDates.size()) {
            int row = mSelectedItemPosition / MAX_COLUMN;
            int column = mSelectedItemPosition % MAX_COLUMN;
            int top = row * (mItemHeight + mItemHeightSpace);
            int drawX = column * mItemWidth + mItemWidth / 2;
            int drawY = top + mItemHeight / 2;
            mPaint.setColor(mCalendarViewDelegate.getSelectedItemColor());
            canvas.drawCircle(drawX, drawY, mItemWidth / 2, mPaint);

        }
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mCalendarViewDelegate.getTopTextSize());
        for (int i = 0; i < mDates.size(); i++) {
            int row = i / MAX_COLUMN;
            int column = i % MAX_COLUMN;
            Date date = mDates.get(i);
            mPaint.setTextSize(mCalendarViewDelegate.getTopTextSize());
            if (i == mSelectedItemPosition) {
                //设置点击样式
                mPaint.setColor(mCalendarViewDelegate.getSelectedTextColor());
            } else if (date.getType() == Date.TYPE_THIS_MONTH || mRow == 1) { //mRow==1为周模式，周模式不选择改变样式。
                mPaint.setColor(mCalendarViewDelegate.getTopTextColor());
            } else {
                //将非当月的Item样式为Bottom的样式，与当月的区分
                mPaint.setColor(mCalendarViewDelegate.getBottomTextColor());
            }
            int itemDrawX = mFirstItemDrawX + column * mItemWidth;
            int itemDrawY = mFirstItemDrawY + row * (mItemHeight + mItemHeightSpace);
            canvas.drawText(date.getDay() + "", itemDrawX, itemDrawY, mPaint);

            //绘制Bottom的文字
            if (mSelectedItemPosition != i &&
                    (mCalendarViewDelegate.isShowLunar() || mCalendarViewDelegate.isShowHoliday())) {
                mPaint.setColor(mCalendarViewDelegate.getBottomTextColor());
                mPaint.setTextSize(mCalendarViewDelegate.getBottomTextSize());
                String text = null;
                if (mCalendarViewDelegate.isShowLunar()) {
                    text = date.getLunarDay();
                }
                if (mCalendarViewDelegate.isShowHoliday()) {
                    if (!TextUtils.isEmpty(date.getLunarHoliday())) {
                        text = date.getLunarHoliday();
                    }
                    if (!TextUtils.isEmpty(date.getHoliday())) {
                        text = date.getHoliday();
                    }
                }
                if (text != null) {
                    // TODO: 2018/2/24 bottomDrawY 的坐标运算优化。
                    float bottomDrawY = mFirstItemDrawY * 1.4f + row * (mItemHeight + mItemHeightSpace);
                    canvas.drawText(text + "", itemDrawX, bottomDrawY, mPaint);
                }
            }
            if (date.isScheme()) {
                float schemeDrawY = row * (mItemHeight + mItemHeightSpace) + mItemHeight / 4;
                if (mSelectedItemPosition == i) {
                    mPaint.setColor(mCalendarViewDelegate.getSelectedSchemeColor());
                } else {
                    mPaint.setColor(mCalendarViewDelegate.getSchemeColor());
                }
                canvas.drawCircle(itemDrawX, schemeDrawY, mCalendarViewDelegate.getSchemeRadius(), mPaint);
            }

        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = event.getX();
                mTouchDownY = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                float disX = mTouchDownX - event.getX();
                float disY = mTouchDownY - event.getY();
                if (Math.abs(disX) < mTouchSlop && Math.abs(disY) < mTouchSlop) {
                    if (mDrawnRect.contains((int) event.getX(), (int) event.getY())) {
                        int row = (int) (event.getY() / (mItemHeight + mItemHeightSpace));
                        int column = (int) (event.getX() / mItemWidth);
                        int position = row * MAX_COLUMN + column;

                        selectDate(position);
                        return true;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }
    public int getItemHeight() {
        return mItemHeight;
    }

    int getSelectedItemTop() {
        int row = mSelectedItemPosition / MAX_COLUMN;
        return row * (mItemHeight + mItemHeightSpace);
    }
    int getSelectedItemBottom() {
        return getSelectedItemTop() + mItemHeight;
    }
    int getMaxHeight() {
        return mItemHeight * MAX_ROW;
    }

}
