package com.airAd.yaqinghui.ui;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airAd.yaqinghui.R;

public class IconListItem extends LinearLayout implements OnTouchListener {
	private ImageView iconImg;
	private TextView titleView;
	private View sigline;

	public IconListItem(Context context) {
		super(context);
		inflate(context, R.layout.icon_list_item, this);
		iconImg = (ImageView) findViewById(R.id.icon);
		titleView = (TextView) findViewById(R.id.title);
		setOnTouchListener(this);
	}
	
	public void setIcon(int bg, int icon, int titleRes) {
		iconImg.setImageResource(icon);
		titleView.setText(titleRes);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			iconImg.setPressed(true);
			titleView.setPressed(true);
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			iconImg.setPressed(false);
			titleView.setPressed(false);
		}
		return false;
	}
}
