package com.airAd.yaqinghui.ui;

import com.airAd.yaqinghui.R;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IconListItem extends LinearLayout implements OnTouchListener {
	private LinearLayout mainLayout;
	private ImageView iconImg;
	private TextView titleView;
	private View sigline;

	public IconListItem(Context context) {
		super(context);
		inflate(context, R.layout.icon_list_item, this);
		mainLayout = (LinearLayout) findViewById(R.id.innerLayout);
		iconImg = (ImageView) findViewById(R.id.icon);
		titleView = (TextView) findViewById(R.id.title);
		setOnTouchListener(this);
	}
	
	public void setIcon(int bg, int icon, int titleRes) {
		mainLayout.setBackgroundResource(bg);
		iconImg.setImageResource(icon);
		titleView.setText(titleRes);
	}
	
	public void setIcon(int bg, int icon, int titleRes,boolean go) {
		mainLayout.setBackgroundResource(bg);
		iconImg.setImageResource(icon);
		titleView.setText(titleRes);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			mainLayout.setPressed(true);
			iconImg.setPressed(true);
			titleView.setPressed(true);
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			mainLayout.setPressed(false);
			iconImg.setPressed(false);
			titleView.setPressed(false);
		}
		return false;
	}
}
