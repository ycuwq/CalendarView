package com.ycuwq.calendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 一周的数据
 * Created by ycuwq on 2018/2/11.
 */
public class WeekView extends View {

    private final String TAG = getClass().getSimpleName();

    private static final int WEEK_SIZE = 7;

    private Paint mPaint;

    private List<Date> mDates;

    private DayItemAttrs mDayItemAttrs;

    private int mTextMaxWidth, mTextMaxHeight;
    private int mItemWidth;
    private int mFirstItemDrawX, mFirstItemDrawY;
    private Rect mDrawnRect;
    public WeekView(Context context) {
        super(context);
    }

    public WeekView(Context context, @Nullable AttributeSet attrs) {
        super(context);
        List<Date> list = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            Date date = new Date(2018, 2, i);
            date.setHoliday("ABSDA");
            list.add(date);
        }
        mDates = list;
        mDayItemAttrs = new DayItemAttrs();
        initPaint();
        computeTextSize();
        mDrawnRect = new Rect();
    }

    public WeekView(Context context, @NonNull List<Date> dates, @NonNull DayItemAttrs dayItemAttrs) {
        super(context);

        mDates = dates;
        mDayItemAttrs = dayItemAttrs;
        initPaint();
        computeTextSize();
        mDrawnRect = new Rect();
    }

    public void setDateList(List<Date> dateList) {
        mDates = dateList;
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.LINEAR_TEXT_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    private void computeTextSize() {
        mPaint.setTextSize(mDayItemAttrs.getTextSizeTop());
        mTextMaxWidth = (int) mPaint.measureText("00");
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
        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = mTextMaxWidth * WEEK_SIZE;
        int height = specWidthSize / WEEK_SIZE;
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(measureSize(specWidthMode, specWidthSize, width),
                measureSize(specHeightMode, specHeightSize, height));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mDrawnRect.set(getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        mItemWidth = mDrawnRect.width() / WEEK_SIZE;
        mFirstItemDrawX = mItemWidth / 2;
        mFirstItemDrawY = (int) ((mDrawnRect.height() - (mPaint.ascent() + mPaint.descent())) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mDayItemAttrs.getTextColorTop());
        mPaint.setTextSize(mDayItemAttrs.getTextSizeTop());
        for (int i = 0; i < mDates.size(); i++) {
            Date date = mDates.get(i);
            int itemDrawX = mFirstItemDrawX + i * mItemWidth;
            canvas.drawText(date.getDay() + "", itemDrawX, mFirstItemDrawY * 0.8f, mPaint);
        }
        if (mDayItemAttrs.isShowLunar() || mDayItemAttrs.isShowHoliday()) {
            mPaint.setColor(mDayItemAttrs.getTextColorBottom());
            mPaint.setTextSize(mDayItemAttrs.getTextSizeBottom());
            for (int i = 0; i < mDates.size(); i++) {
                Date date = mDates.get(i);
                String text = null;
                if (mDayItemAttrs.isShowLunar()) {
                    text = date.getLunarDay();
                }
                if (mDayItemAttrs.isShowHoliday()) {
                    if (!TextUtils.isEmpty(date.getLunarHoliday())) {
                        text = date.getLunarHoliday();
                    }
                    if (!TextUtils.isEmpty(date.getHoliday())) {
                        text = date.getHoliday();
                    }
                }
                if (text != null) {
                    int itemDrawX = mFirstItemDrawX + i * mItemWidth;
                    canvas.drawText(text + "", itemDrawX, mFirstItemDrawY * 1.2f, mPaint);
                }
            }
        }
    }
}
