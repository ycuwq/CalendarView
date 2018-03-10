package com.ycuwq.calendarview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
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

    private CalendarViewDelegate mDelegate;
	public WeekInfoView(Context context, CalendarViewDelegate delegate) {
		super(context);
		mDelegate = delegate;
        initPaint();
        super.setBackgroundColor(mDelegate.getWeekInfoBackgroundColor());
	}


	private void initPaint() {
		mTextPaint = new TextPaint();
		mTextPaint.setTextSize(mDelegate.getWeekInfoTextSize());
		mTextPaint.setColor(mDelegate.getWeekInfoTextColor());
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
		mTextPaint.setTextSize(mDelegate.getWeekInfoTextSize());
		mTextPaint.setColor(mDelegate.getWeekInfoTextColor());
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
}
