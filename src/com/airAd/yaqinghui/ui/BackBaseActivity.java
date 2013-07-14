/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.airAd.yaqinghui.BaseActivity;
import com.airAd.yaqinghui.R;

/**
 * BackBaseActivity.java
 *
 * @author liyuhang
 */
public class BackBaseActivity extends BaseActivity {

	private RelativeLayout contentView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.activity_main_frame);
		contentView = (RelativeLayout) findViewById(R.id.act_main_frame);
	}

	@Override
	public void setContentView(int layoutResId) {
		getLayoutInflater().inflate(layoutResId, contentView);
	}
	
	public void leftBtnClick(View view)
	{
		onBackPressed();
	}
}
