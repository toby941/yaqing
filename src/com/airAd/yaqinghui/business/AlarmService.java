/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.business;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.alarm.EventAlarmReceiver;
import com.airAd.yaqinghui.business.model.ScheduleItem;
import com.airAd.yaqinghui.common.Config;


/**
 * AlarmService.java
 *
 */
public class AlarmService {
	
	public static final String ALARM_CONTENT = "alarm_content";
	public static final String ALARM_ID = "alarm_id";
	public static final String DAILY_REPEAT = "dailyRepeat";
	
	public static final int ID_START = 246;
	public static AlarmService instance;
	
	public void addAlarm(int id, long time, String content)
	{
		id = ID_START + id + 1;
		Intent intent = new Intent(MyApplication.getCurrentApp(), EventAlarmReceiver.class);
		intent.putExtra(ALARM_CONTENT, content);
		intent.putExtra(ALARM_ID, id);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(MyApplication.getCurrentApp(),
		    id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = 
		    (AlarmManager)MyApplication.getCurrentApp().getSystemService(Activity.ALARM_SERVICE);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		
		c.add(Calendar.MINUTE, getTimeBefore() * -1);
		long setTime = SystemClock.elapsedRealtime() + c.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
		//c.set(Calendar.MINUTE, 40);
		am.cancel(pendingIntent);
		am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, setTime, pendingIntent);
	}
	
	public void removeAlarm(int id)
	{
		id = ID_START + id + 1;
		Intent intent = new Intent(MyApplication.getCurrentApp(), EventAlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(MyApplication.getCurrentApp(),
				id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = 
		    (AlarmManager)MyApplication.getCurrentApp().getSystemService(Activity.ALARM_SERVICE);
		am.cancel(pendingIntent);
	}
	
	public void resetAllAlarm()
	{
		AlarmManager am = 
		    (AlarmManager)MyApplication.getCurrentApp().getSystemService(Activity.ALARM_SERVICE);
		Calendar c = Calendar.getInstance();
		
		int timeBefore = getTimeBefore();
		
		ScheduleService service = new ScheduleService();
		List<ScheduleItem> list = service.getScheduleItems(MyApplication.getCurrentUser().getId());
		for(ScheduleItem item : list)
		{
			int id = ID_START + (int)(item.getCid() / 1);
			String content = item.getTitle();
			long time = item.getStartTimel();
			
			Intent intent = new Intent(MyApplication.getCurrentApp(), EventAlarmReceiver.class);
			intent.putExtra(ALARM_CONTENT, content);
			intent.putExtra(ALARM_ID, id);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(MyApplication.getCurrentApp(),
			    id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
			
			c.setTimeInMillis(time);
			c.add(Calendar.MINUTE, timeBefore * -1);
			long setTime = SystemClock.elapsedRealtime() + c.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
			am.cancel(pendingIntent);
			am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, setTime, pendingIntent);
		}
	}
	
	public static boolean isEventAlarmOpen()
	{
		SharedPreferences sp = MyApplication.getCurrentApp().getSharedPreferences(Config.PACKAGE,
				Context.MODE_PRIVATE);
		return sp.getBoolean(Config.EVENT_REMIND_ISOPEN, true);
	}
	
	public static int getTimeBefore()
	{
		SharedPreferences sp = MyApplication.getCurrentApp().getSharedPreferences(
				Config.PACKAGE, Context.MODE_PRIVATE);
		int which = sp.getInt(Config.EVENT_REMIND_BEFORE, 0);
		int timeBefore = 0;
		switch (which) {
		case 0:
			timeBefore = 30;
			break;
		case 1:
			timeBefore = 60;
			break;
		case 2:
			timeBefore = 120;
			break;
		}
		return timeBefore;
	}
	
	public static AlarmService getInstance()
	{
		if(instance == null)
			instance = new AlarmService();
		return instance;
	}
	
	public void setDailyRepeatAlarm()
	{
		Intent intent = new Intent(MyApplication.getCurrentApp(), EventAlarmReceiver.class);
		intent.putExtra(ALARM_CONTENT, MyApplication.getCurrentApp().getResources().getString(R.string.dialy_remind));
		intent.putExtra(ALARM_ID, ID_START);
		intent.putExtra(DAILY_REPEAT, true);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(MyApplication.getCurrentApp(),
			ID_START, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = 
		    (AlarmManager)MyApplication.getCurrentApp().getSystemService(Activity.ALARM_SERVICE);
		
		SharedPreferences sp = MyApplication.getCurrentApp().getSharedPreferences(
				Config.PACKAGE, Context.MODE_PRIVATE);
		int which = sp.getInt(Config.REGULAR_REMIND_TIME, 0);
		
		int hourOfDay = 6;
		int minute = 30;
		switch (which) {
		case 0:
			hourOfDay = 6;
			minute = 30;
			break;
		case 1:
			hourOfDay = 9;
			minute = 00;
			break;
		case 2:
			hourOfDay = 12;
			minute = 00;
			break;
		}
		//long firstTime = SystemClock.elapsedRealtime();
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		//c.add(Calendar.MINUTE, timeBefore * -1);
		//c.add(Calendar.DAY_OF_MONTH, 1);
		//c.set(Calendar.HOUR_OF_DAY, 20);
		//c.set(Calendar.MINUTE, 41);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		am.cancel(pendingIntent);
		//提醒时间由当前RTC时间差+SystemClock.elapsedRealtime()组成
		long setTime = SystemClock.elapsedRealtime() + c.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
		Log.i("time", System.currentTimeMillis() + "," + SystemClock.currentThreadTimeMillis() + "," + Calendar.getInstance().getTimeInMillis());
		//am.setRepeating(R, triggerAtMillis, intervalMillis, operation)
		//am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 1000 * 24 * 60 * 60, pendingIntent);
		am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + 60 * 1000, 1000 * 24 * 60 * 60, pendingIntent);
	}
	
	public void cancelDailyRepeatAlarm()
	{
		Intent intent = new Intent(MyApplication.getCurrentApp(), EventAlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(MyApplication.getCurrentApp(),
			ID_START, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager am = 
		    (AlarmManager)MyApplication.getCurrentApp().getSystemService(Activity.ALARM_SERVICE);
		am.cancel(pendingIntent);
	}
	
}
