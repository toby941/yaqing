/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.alarm;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.WelcomeActivity;
import com.airAd.yaqinghui.business.AlarmService;
import com.airAd.yaqinghui.common.Config;

/**
 * AlarmReceiver.java
 * 
 * @author liyuhang
 */
public class EventAlarmReceiver extends BroadcastReceiver {

	public static final int ALARM_ID = 10001;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {

		String content = intent.getStringExtra(AlarmService.ALARM_CONTENT);
		int id = intent.getIntExtra(AlarmService.ALARM_ID, ALARM_ID);
		boolean isRepeat = intent.getBooleanExtra(AlarmService.DAILY_REPEAT,
				false);
		Log.i("EventAlarmReceiver", content);

		SharedPreferences sp = context.getSharedPreferences(Config.PACKAGE,
				Context.MODE_PRIVATE);
		boolean show = false;
		if (isRepeat) {
			show = sp.getBoolean(Config.REGULAR_REMIND_ISOPEN, true);
		} else {
			show = sp.getBoolean(Config.EVENT_REMIND_ISOPEN, true);
		}
		// 如果设置为提醒
		if (show) {
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					context)
					.setSmallIcon(R.drawable.game_daily_loc)
					.setContentTitle(
							context.getResources().getString(
									R.string.alarm_title))
					.setContentText(content);

			Intent startMain = new Intent(context, WelcomeActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);

			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			stackBuilder.addNextIntent(startMain);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
					0, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			Notification notification = mBuilder.build();
			setAlarmParams(notification);
			mNotificationManager.notify(id, notification);
		}
	}

	private void setAlarmParams(Notification notification) {
		// AudioManager provides access to volume and ringer mode control.
		AudioManager volMgr = (AudioManager) MyApplication.getCurrentApp()
				.getSystemService(Context.AUDIO_SERVICE);
		switch (volMgr.getRingerMode()) {
		// 获取系统设置的铃声模式
		case AudioManager.RINGER_MODE_SILENT:
			// 静音模 式，值为0，这时候不震动，不响铃
			notification.sound = null;
			notification.vibrate = null;
			break;
		case AudioManager.RINGER_MODE_VIBRATE:
			// 震动模式，值为1，这时候震动，不响铃
			notification.sound = null;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			break;
		case AudioManager.RINGER_MODE_NORMAL:
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			// 都给开灯
			break;
		default:
			break;
		}

	}
}
