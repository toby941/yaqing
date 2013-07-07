package com.airAd.yaqinghui.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.yaqinghui.R;

/**
 * 赛事日程的单项
 * 
 * @author pengfan
 * 
 */
public class EventView extends LinearLayout
{
	private ImageView mIconView;
	private TextView mTitleView;
	private String mImgUrl;

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
	
	public EventView(Context context, String imgUrl, String txt)
	{
		super(context);
		init(context);
		mImgUrl = imgUrl;
		mTitleView.setText(txt);
	}

	private void init(Context context)
	{
		LayoutInflater.from(context).inflate(R.layout.event_view, this);
		mIconView = (ImageView)findViewById(R.id.icon);
		mTitleView = (TextView)findViewById(R.id.title);
	}
	
	public void load(ImageFetcher fetcher)
	{
		if(mImgUrl != null)
		{
			fetcher.loadImage(mImgUrl, mIconView);
		}
	}
}
