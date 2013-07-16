/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.business.AlarmService;

/**
 * AlarmReceiver.java
 * 
 * @author liyuhang
 */
public class AlarmReceiver extends BroadcastReceiver {

	public static final int ALARM_ID = 10001;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 * android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		String content = intent.getDataString();
		content = intent.getStringExtra(AlarmService.ALARM_CONTENT);
		
		NotificationCompat.Builder mBuilder =
	        new NotificationCompat.Builder(context)
	        .setSmallIcon(R.drawable.game_daily_loc)
	        .setContentTitle(context.getResources().getString(R.string.alarm_title))
	        .setContentText(content);
		
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

		stackBuilder.addNextIntent(startMain);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(ALARM_ID, mBuilder.build());
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
			/*// 常规模式，值为2，分两种情况：1_响铃但不 震动，2_响铃+震动
			Uri ringTone = null;
			// 获取软件的设置
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(MyApplication.getCurrentApp());
			if (!sp.contains(SystemUtil.KEY_RING_TONE)) {
				// 如果没有生成配置文件，那么既 有铃声又有震动
				notification.defaults |= Notification.DEFAULT_VIBRATE;
				notification.defaults |= Notification.DEFAULT_SOUND;
			} else {
				String ringFile = sp.getString(SystemUtil.KEY_RING_TONE, null);
				if (ringFile == null) {
					// 无值，为空，不播放铃声
					ringTone = null;
				} else if (!TextUtils.isEmpty(ringFile)) {
					// 有铃声：1，默 认2自定义，都返回一个 uri
					ringTone = Uri.parse(ringFile);
				}
				notification.sound = ringTone;
				boolean vibrate = sp.getBoolean(
						SystemUtil.KEY_NEW_MAIL_VIBRATE, true);
				if (vibrate == false) {
					// 如果软件设置不震动，那么就不震动了
					notification.vibrate = null;
				} else {
					// 否则就是需要震动，这时候要看系统是怎么设置的：不震动=0;震动=1；仅在静音模式下震动=2；
					if (volMgr
							.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_OFF) {
						// 不震动
						notification.vibrate = null;
					} else if (volMgr
							.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_ONLY_SILENT) {
						// 只在静音时震动
						notification.vibrate = null;
					} else {
						// 震动
						notification.defaults |= Notification.DEFAULT_VIBRATE;
					}
				}
			}*/
			notification.flags |= Notification.FLAG_SHOW_LIGHTS;
			// 都给开灯
			break;
		default:
			break;
		}

	}
}
