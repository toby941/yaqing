/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;

import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import cn.jpush.android.service.AlarmReceiver;

import com.airAd.yaqinghui.MyApplication;


/**
 * AlarmService.java
 *
 */
public class AlarmService {
	
	public static final String ALARM_CONTENT = "alarm_content";
	public static AlarmService instance;
	
	public void addAlarm(int id, long time, String content)
	{
		Intent intent = new Intent(MyApplication.getCurrentApp(), AlarmReceiver.class);
		intent.putExtra(ALARM_CONTENT, content);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(MyApplication.getCurrentApp(),
		    id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = 
		    (AlarmManager)MyApplication.getCurrentApp().getSystemService(Activity.ALARM_SERVICE);
		Date date = new Date();
		am.set(AlarmManager.RTC_WAKEUP, date.getTime() + 3000, pendingIntent);
	}
	
	public void removeAlarm(int id)
	{
		Intent intent = new Intent(MyApplication.getCurrentApp(), AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(MyApplication.getCurrentApp(),
		    id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = 
		    (AlarmManager)MyApplication.getCurrentApp().getSystemService(Activity.ALARM_SERVICE);
		am.cancel(pendingIntent);
	}
	
	public static AlarmService getInstance()
	{
		if(instance == null)
			instance = new AlarmService();
		return instance;
	}
}
