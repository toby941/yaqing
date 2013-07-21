package com.airAd.yaqinghui.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class CanCloseListView extends ListView {
	private Context mContext;

	private GestureDetector mGestureDetector;
	private float dirs;

	private float lastPos;
	private float curPos;
	public boolean isHead = true;

	public CanCloseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		this.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		this.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (CanCloseListView.this.getChildAt(0) != null) {
					if (firstVisibleItem == 0
							&& Math.abs(CanCloseListView.this.getChildAt(0)
									.getTop()) <= 2) {
						isHead = true;
					}
				}
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (isHead) {
			return false;
		}

		return super.dispatchTouchEvent(ev);
	}
}
