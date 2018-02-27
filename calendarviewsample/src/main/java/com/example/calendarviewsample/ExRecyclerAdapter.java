package com.example.calendarviewsample;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * 通用RecyclerViewAdapter
 * Created by 杨晨 on 2017/3/18.
 */
public abstract class ExRecyclerAdapter<T> extends RecyclerView.Adapter<ExRecyclerViewHolder>  {

	private List<T> mList;
	private Context mContext;
	private int layoutRes;

	public ExRecyclerAdapter(@NonNull Context mContext, @LayoutRes int layoutRes) {
		this.mContext = mContext;
		this.layoutRes = layoutRes;
	}

	public void setList(List<T> mList) {
		this.mList = mList;
		notifyDataSetChanged();
	}

	public void addList(List<T> list) {
		int positionStart = mList.size();
		mList.addAll(list);
		int itemCount = list.size() - positionStart;
		notifyItemRangeInserted(positionStart + 1, itemCount);
	}

	@Override
	public ExRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(layoutRes, parent, false);
		return new ExRecyclerViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ExRecyclerViewHolder holder, int position) {
		bindData(holder, mList.get(position), position);
	}

	@Override
	public int getItemCount() {
		return mList != null ? mList.size() : 0;
	}

	public abstract void bindData(ExRecyclerViewHolder holder, T t, int position);
}
