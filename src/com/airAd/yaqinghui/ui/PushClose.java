package com.airAd.yaqinghui.ui;

import com.airAd.yaqinghui.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class PushClose extends RelativeLayout {
	public static final int DAYS = 11;

	public boolean isClosed = true;
	private LinearLayout mBottomView;
	private LinearLayout mTopView;
	private Scroller mScroller;
	private CanCloseListView mListView;
	private Context mContext;

	private boolean mIsBeingDragged = false;
	private float mLastMotionX;
	private float mLastMotionY;

	DateTextView date1;

	private DateTextView[] dates = new DateTextView[DAYS];
	private DateClick dateClick;

	private DateTextView selectedDate;

	public static final int UNSELECTED_COLOR = Color.rgb(254, 102, 97);
	public static final int SELECTED_COLOR = Color.rgb(50, 63, 78);
	public static final int TEXT_NORMAL_COLOR = Color.rgb(177, 179, 184);

	public PushClose(Context context) {
		super(context);
		init(context);
	}

	public PushClose(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mScroller = new Scroller(getContext());
	}

	public void close() {
		smoothScrollTo(0, 0);
		isClosed = true;
		mListView.isHead = false;
	}

	public void open() {
		int bottomHeight = mBottomView.getChildAt(0).getHeight();
		smoothScrollTo(0, -bottomHeight);
		isClosed = false;
		mListView.isHead = true;
	}

	public void setContent(View top, View bottom) {
		mBottomView = (LinearLayout) findViewById(R.id.bottom);
		mTopView = (LinearLayout) findViewById(R.id.top);
		mBottomView.addView(bottom);
		mTopView.addView(top);
		mListView = (CanCloseListView) mTopView.findViewById(R.id.date_list);
		addDateListener();
	}

	private void addDateListener() {
		dateClick = new DateClick();
		dates[0] = (DateTextView) mBottomView.findViewById(R.id.date_day1);
		dates[1] = (DateTextView) mBottomView.findViewById(R.id.date_day2);
		dates[2] = (DateTextView) mBottomView.findViewById(R.id.date_day3);
		dates[3] = (DateTextView) mBottomView.findViewById(R.id.date_day4);
		dates[4] = (DateTextView) mBottomView.findViewById(R.id.date_day5);
		dates[5] = (DateTextView) mBottomView.findViewById(R.id.date_day6);
		dates[6] = (DateTextView) mBottomView.findViewById(R.id.date_day7);
		dates[7] = (DateTextView) mBottomView.findViewById(R.id.date_day8);
		dates[8] = (DateTextView) mBottomView.findViewById(R.id.date_day9);
		dates[9] = (DateTextView) mBottomView.findViewById(R.id.date_day10);
		dates[10] = (DateTextView) mBottomView.findViewById(R.id.date_day11);
		for (int i = 0; i < dates.length; i++) {
			dates[i].setOnClickListener(dateClick);
		}// end for i
	}

	/**
	 * 点击日期事件响应
	 * 
	 * @author Administrator
	 * 
	 */
	private final class DateClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (isClosed) {
				return;
			}
			if (selectedDate != null) {
				selectedDate.setTextColor(TEXT_NORMAL_COLOR);
				selectedDate.setBackgroundColor(SELECTED_COLOR);
			}
			DateTextView dateText = (DateTextView) v;
			dateText.setTextColor(SELECTED_COLOR);
			dateText.setBackgroundColor(UNSELECTED_COLOR);

			selectedDate = dateText;

			// if(dateText.isHaveActivity){
			// dateText.setNoneActivity();
			// }else{
			// dateText.setHasActivity();
			// }
			close();
		}
	}// end inner class

	@Override
	public void computeScroll() {
		if (!mScroller.isFinished()) {
			if (mScroller.computeScrollOffset()) {
				mTopView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
				postInvalidate();
			}
		}
	}

	public void smoothScrollBy(int dx, int dy) {
		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx,
				dy);
		invalidate();
	}

	public void smoothScrollTo(int fx, int fy) {
		int dx = fx - mScroller.getFinalX();
		int dy = fy - mScroller.getFinalY();
		smoothScrollBy(dx, dy);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			int dy = (int) (mLastMotionY - y);
			if (mScroller.getCurrY() + dy > 0) {
				smoothScrollBy(0, 0);
			} else if (mScroller.getCurrY() + dy < -mBottomView.getChildAt(0)
					.getHeight()) {
				smoothScrollTo(0, -mBottomView.getChildAt(0).getHeight());
			} else {
				smoothScrollBy(0, dy);
			}
			mLastMotionX = x;
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_UP:
			int bottomHeight = mBottomView.getChildAt(0).getHeight();
			if (mTopView.getScrollY() > -bottomHeight / 2) {
				smoothScrollTo(0, 0);
				isClosed = true;
				mListView.isHead = false;
			} else {
				smoothScrollTo(0, -bottomHeight);
				isClosed = false;
				mListView.isHead = true;
			}
			break;
		}
		return true;
	}

}// end class
