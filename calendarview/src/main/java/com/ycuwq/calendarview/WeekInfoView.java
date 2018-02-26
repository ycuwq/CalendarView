package com.ycuwq.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.view.View;

import com.ycuwq.calendarview.utils.DensityUtil;


/**
 * 自定义显示CalendarView上方的日期提示
 * Created by 杨晨 on 2017/5/15.
 */
@SuppressLint("ViewConstructor")
@SuppressWarnings("unused")
public class WeekInfoView extends View {
	private Context mContext;

	private String[] mWeekArray = {"一", "二", "三", "四", "五", "六", "日"};
	private TextPaint mTextPaint;
	private @ColorInt int mTextColor = Color.BLACK;
	private @ColorInt int mBackgroundColor = Color.WHITE;
	private int mTextSize = 14;
    private CalendarViewDelegate mDelegate;
	public WeekInfoView(Context context, CalendarViewDelegate delegate) {
		super(context);
		mDelegate = delegate;
        initPaint();
        super.setBackgroundColor(mBackgroundColor);
	}


	private void initPaint() {
		mTextPaint = new TextPaint();
		mTextPaint.setTextSize(DensityUtil.sp2px(getContext(), mTextSize));
		mTextPaint.setColor(mTextColor);
		mTextPaint.setAntiAlias(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int width = widthSize;
		int height = heightSize;
		if (widthMode == MeasureSpec.AT_MOST) {
			width = MeasureSpec.makeMeasureSpec((1 << 30) -1, MeasureSpec.AT_MOST);
		}
		if (heightMode == MeasureSpec.AT_MOST) {
			height = DensityUtil.dp2px(getContext(), 40);
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int itemWidth = getWidth() / 7;
		int itemHeight = getHeight();

		for (int i = 0; i < mWeekArray.length; i++) {
			String text = mWeekArray[i];
			//计算开始宽度， 用（itemWidth宽度 - 字体宽度） / 2 再加上每个item初始坐标
			int startX =  itemWidth * i + (int)((itemWidth - mTextPaint.measureText(text)) / 2);
			int startY = (int) (itemHeight / 2 - (mTextPaint.descent() + mTextPaint.ascent()) / 2);
			canvas.drawText(text, startX, startY, mTextPaint);
		}

	}

	public String[] getWeekArray() {
		return mWeekArray;
	}

	public void setWeekArray(String[] mWeekArray) {
		this.mWeekArray = mWeekArray;
	}

	public int getTextColor() {
		return mTextColor;
	}

	public void setTextColor(int textColor) {
		this.mTextColor = textColor;
		mTextPaint.setColor(textColor);
		invalidate();
	}

	public int getBackgroundColor() {
		return mBackgroundColor;
	}

	@Override
	public void setBackgroundColor(int backgroundColor) {
		this.mBackgroundColor = backgroundColor;
		super.setBackgroundColor(backgroundColor);
		invalidate();
	}

	public int getTextSize() {
		return mTextSize;
	}

	public void setTextSize(int textSize) {
		this.mTextSize = mTextSize;
		mTextPaint.setTextSize(DensityUtil.sp2px(getContext(), textSize));
		invalidate();
	}
}
