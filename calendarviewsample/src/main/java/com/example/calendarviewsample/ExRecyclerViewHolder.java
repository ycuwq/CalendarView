package com.example.calendarviewsample;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * {@link ExRecyclerAdapter} 使用的ViewHolder
 * Created by 杨晨 on 2017/3/18.
 */
public class ExRecyclerViewHolder extends RecyclerView.ViewHolder {

	private SparseArray<View> views;

	ExRecyclerViewHolder(View itemView) {
		super(itemView);
		views = new SparseArray<>();
	}

	@NonNull
	@SuppressWarnings("unchecked")
	public <T extends View> T getView(@IdRes int viewId) {
		View view = views.get(viewId);
		if (view == null) {
			view = itemView.findViewById(viewId);
			views.put(viewId, view);
		}

		T t = null;
		try {
			t = (T) view;
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return t;
	}

	public View getRootView() {
		return itemView;
	}
}
