package com.airAd.yaqinghui.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class DateContainer extends RelativeLayout{
	private PushClose mPushClose;
	public DateContainer(Context context) {
		super(context);
	}

	public DateContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setPushClose(PushClose pushClose){
		this.mPushClose = pushClose;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if(mPushClose!=null){
			if(mPushClose.isClosed){
				return false;
			}else{
				return super.dispatchTouchEvent(ev);
			}
		}
		return super.dispatchTouchEvent(ev);
	}
}//end class
