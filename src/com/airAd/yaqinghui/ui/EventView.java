package com.airAd.yaqinghui.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airAd.yaqinghui.R;

/**
 * 赛事日程的单项
 * 
 * @author pengfan
 * 
 */
public class EventView extends LinearLayout
{
	private ImageView iconView;
	private TextView titleView;

	public EventView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	public EventView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public EventView(Context context)
	{
		super(context);
		init(context);
	}
	
	public EventView(Context context, int imgResId, int txtResId)
	{
		super(context);
		init(context);
		iconView.setImageResource(imgResId);
		titleView.setText(txtResId);
	}

	private void init(Context context)
	{
		LayoutInflater.from(context).inflate(R.layout.event_view, this);
		iconView = (ImageView)findViewById(R.id.icon);
		titleView = (TextView)findViewById(R.id.title);
	}
}
