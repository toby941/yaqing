/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;

import com.airAd.yaqinghui.MyApplication;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * AlarmService.java
 *
 */
public class AlarmService {
	
	public void addAlarm(int id, long time)
	{
		/*Intent intent = new Intent(MyApplication.getCurrentApp(), AlarmReceiverActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this,
		    id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = 
		    (AlarmManager)getSystemService(Activity.ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
		        pendingIntent);*/
	}
}
