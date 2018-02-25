package com.ycuwq.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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
class ItemView extends View {

    protected static final int MAX_ROW = 6;       //最大显示的行数

    protected static final int MAX_COLUMN = 7;        //显示的列数

    private final String TAG = getClass().getSimpleName();


    private Paint mPaint;

    private List<Date> mDates;

    private CalendarViewDelegate mCalendarViewDelegate;
    //显示的行数
    private int mRow;
    private int mItemWidth, mItemHeight;
    private int mFirstItemDrawX, mFirstItemDrawY;
    private Rect mDrawnRect;
    private int mTouchSlop;

    /**
     * 日历一个月理论最大行数是6，当当前月的行数小于6时，为使高度通体，将多的部分平均分到每一个间隔上。
     */
    private int mItemHeightSpace;

    private float mTouchDownX, mTouchDownY;

    private int mSelectedItemPosition = -1;

    private OnDaySelectedListener mOnDaySelectedListener;

    public ItemView(Context context, @NonNull CalendarViewDelegate calendarViewDelegate) {
        super(context);
        mCalendarViewDelegate = calendarViewDelegate;
        initPaint();
        mDrawnRect = new Rect();
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setDateList(List<Date> dates) {
        if (dates == null || dates.size() == 0 || dates.size() % MAX_COLUMN != 0) {
            throw new IllegalArgumentException("dates must be divisible by" + MAX_COLUMN);
        }
        mSelectedItemPosition = -1;
        mDates = dates;
        mRow = mDates.size() / MAX_COLUMN;
        requestLayout();
    }


    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(mCalendarViewDelegate.getTextSizeTop());
    }


    public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener) {
        mOnDaySelectedListener = onDaySelectedListener;
    }

    public void selectDate(Date date) {
        int position = mDates.indexOf(date);
        if (position >=0) {
            selectedDate(position);
        } else {
            cancelSelected();
        }
    }

    public void selectedDate(int position) {
        if (position >= 0 && position < mDates.size()) {
            mSelectedItemPosition = position;
            postInvalidate();
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

        int height = itemWidth * MAX_ROW;
        setMeasuredDimension(widthMeasureSpec, height);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawnRect.set(getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        mItemWidth = mDrawnRect.width() / MAX_COLUMN;
        mItemHeight = mDrawnRect.height() / MAX_ROW;

        mItemHeightSpace = (MAX_ROW - mRow) * mItemHeight / mRow;

        mFirstItemDrawX = mItemWidth / 2;
        mFirstItemDrawY = (int) ((mItemHeight - (mPaint.ascent() + mPaint.descent())) / 2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mSelectedItemPosition >= 0 && mSelectedItemPosition < mDates.size()) {
            int row = mSelectedItemPosition / MAX_COLUMN;
            int column = mSelectedItemPosition % MAX_COLUMN;
            int top = row * (mItemHeight + mItemHeightSpace);
            Drawable clickBg = mCalendarViewDelegate.getSelectedBg();
            clickBg.setBounds(column * mItemWidth, top,
                    (column + 1) * mItemWidth, top + mItemHeight);
            clickBg.draw(canvas);
        }
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(mCalendarViewDelegate.getTextSizeTop());
        for (int i = 0; i < mDates.size(); i++) {
            int row = i / MAX_COLUMN;
            int column = i % MAX_COLUMN;
            Date date = mDates.get(i);
            mPaint.setTextSize(mCalendarViewDelegate.getTextSizeTop());
            if (i == mSelectedItemPosition) {
                //设置点击样式
                mPaint.setColor(mCalendarViewDelegate.getClickTextColor());
            } else if (date.getType() == Date.TYPE_THIS_MONTH || mRow == 1) { //mRow==1为周模式，周模式不选择改变样式。
                mPaint.setColor(mCalendarViewDelegate.getTextColorTop());
            } else {
                //将非当月的Item样式为Bottom的样式，与当月的区分
                mPaint.setColor(mCalendarViewDelegate.getTextColorBottom());
            }
            int itemDrawX = mFirstItemDrawX + column * mItemWidth;
            int itemDrawY = mFirstItemDrawY + row * (mItemHeight + mItemHeightSpace);
            canvas.drawText(date.getDay() + "", itemDrawX, itemDrawY, mPaint);

            //绘制Bottom的文字
            if (mSelectedItemPosition != i &&
                    (mCalendarViewDelegate.isShowLunar() || mCalendarViewDelegate.isShowHoliday())) {
                mPaint.setColor(mCalendarViewDelegate.getTextColorBottom());
                mPaint.setTextSize(mCalendarViewDelegate.getTextSizeBottom());
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
                        selectedDate(position);
                        if (mOnDaySelectedListener != null) {
                            mOnDaySelectedListener.onDaySelected(mDates.get(position), position);
                        }
                        return true;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }
    interface OnDaySelectedListener {
        void onDaySelected(Date date, int position);
    }
}
