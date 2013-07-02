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
	private ImageView iconImg;
	private TextView titleView;
	private ImageView glare;
	private View sigline;

	public IconListItem(Context context) {
		super(context);
		inflate(context, R.layout.icon_list_item, this);
		iconImg = (ImageView) findViewById(R.id.icon);
		titleView = (TextView) findViewById(R.id.title);
		glare = (ImageView)findViewById(R.id.glare);
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
			glare.setVisibility(View.VISIBLE);
		} else if (event.getAction() == MotionEvent.ACTION_UP
				|| event.getAction() == MotionEvent.ACTION_CANCEL) {
			iconImg.setPressed(false);
			titleView.setPressed(false);
			glare.setVisibility(View.INVISIBLE);
		}
		return false;
	}
}
