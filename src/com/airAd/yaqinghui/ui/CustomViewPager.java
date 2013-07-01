package com.airAd.yaqinghui.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不能自己滑动的ViewPager控件
 * 
 * @author Panyi
 * 
 */
public class CustomViewPager extends ViewPager {

	public boolean isCanScroll = false;

	public CustomViewPager(Context context) {
		super(context);
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setScanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isCanScroll == false) {
			return false;
		} else {
			return super.onTouchEvent(ev);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (isCanScroll == false) {
			return false;
		} else {
			return super.onInterceptTouchEvent(ev);
		}
	}
}// end class
