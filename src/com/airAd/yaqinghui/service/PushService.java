package com.airAd.yaqinghui.service;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import net.sf.json.JSONObject;

import com.airAd.yaqinghui.Config;
import com.airAd.yaqinghui.Constants;
import com.airAd.yaqinghui.HomeActivity;
import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.data.model.CepActive;
import com.airAd.yaqinghui.data.model.CepItem;
import com.airAd.yaqinghui.data.model.ConfirmCepActive;
import com.airAd.yaqinghui.factory.HessianClient;
import com.airAd.yaqinghui.net.CepService;
import com.airAd.yaqinghui.net.ConfirmCepActiveResolve;
import com.airAd.yaqinghui.net.MyActiveService;
import com.yogapppackage.BasicAPI;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;

public class PushService extends Service {
	public static final int SERVICE_STATUS_NORMAL = 1;
	public static final int SERVICE_STATUS_REPEAT = 2;
	public static final int INTERVAL = 10 * 60 * 1000;// 间隔时间10min
	public static final int CHECK_DELTA = 20 * 1000;//
	public static final String STATUS = "status";
	public static final int NOTIFICATIONID = R.drawable.icon;
	private boolean alarmSet;
	private ActivityManager activityManager;

	private Timer timer;
	private MyApplication app;
	private SharedPreferences sp;
	private NotificationManager nManager;// 通知管理

	@Override
	public void onCreate() {
		super.onCreate();
		app = MyApplication.getCurrentApp();
		timer = new Timer();
		nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			return -1;
		}
		int status = intent.getIntExtra(STATUS, SERVICE_STATUS_NORMAL);
		switch (status) {
		case SERVICE_STATUS_NORMAL:
			setPushTask();
			break;
		case SERVICE_STATUS_REPEAT:
			setUpAlarm();
			break;
		}// end switch
		return 1;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void setUpAlarm() {
		if (!alarmSet) {
			AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(this, PushService.class);
			intent.putExtra(STATUS, SERVICE_STATUS_REPEAT);
			PendingIntent sender = PendingIntent.getService(this, 0, intent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 0, INTERVAL,
					sender);
			alarmSet = true;
		}
	}

	private void setPushTask() {
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				CepActive cepActive = getPushInfo();
				if (cepActive != null) {
					if (isInApp()) {// 在应用之内
						Intent it = new Intent();
						it.setAction(Constants.PUSH_SERVICE);
						it.putExtra("status", cepActive.getCeptitle());
						PushService.this.sendBroadcast(it);
					} else {// 在应用外面
						showNotification(cepActive.getCeptitle(),
								getString(R.string.app_name));
					}
				}// end if
			}
		}, 0, CHECK_DELTA);
	}

	private void showNotification(String text, String title) {
		Notification notification = new Notification(NOTIFICATIONID, null,
				System.currentTimeMillis());
		Intent intent = new Intent();
		intent.putExtra(Constants.HOME_FLAG, Constants.HOME_FLAG_GOTO);
		intent.setClass(this, HomeActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(this, title, text, contentIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		nManager.notify(NOTIFICATIONID, notification);
	}

	private CepActive getPushInfo() {// 获取到推送消息
		BasicAPI basicAPI = HessianClient.create();
		JSONObject jsonObj = null;
		try {
			jsonObj = basicAPI.SelectConfirmCepActive(Config.CEP_USER_ID);
			ArrayList<CepActive> cepList = new MyActiveService()
					.getCepActiveList(jsonObj);
			for (CepActive cepItem : cepList) {
				JSONObject obj = basicAPI.ConfirmCepActive(cepItem.getCepid(),
						Config.CEP_USER_ID);
				ConfirmCepActive confirmCepActive = (new ConfirmCepActiveResolve())
						.getConfirmCepActive(obj);
				sp = this.getSharedPreferences(Config.PACKAGE,
						Context.MODE_PRIVATE);
				if (!ConfirmCepActive.FLAG_CHECKING
						.equalsIgnoreCase(confirmCepActive.getConfirmmark())) {
					String hasRecordCepData = sp.getString(Config.HAS_SHOW_CEP,
							"");
					if (!hasRecordCepData.contains(cepItem.getCepid())) {// 没有记录
						CepActive result = new CepActive();
						result.setCepid(cepItem.getCepid());
						result.setCeptitle(confirmCepActive.getConfirmtext());
						Editor ed = sp.edit();
						String newKey = hasRecordCepData + ","
								+ cepItem.getCepid();
						ed.putString(Config.HAS_SHOW_CEP, newKey);
						ed.commit();
						return result;
					}
				}
			}// end for
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 判断当前是否在App内部
	 * 
	 * @return
	 */
	private boolean isInApp() {
		ComponentName cn = activityManager.getRunningTasks(1).get(0).topActivity;
		if (cn == null) {
			return false;
		}
		if (cn.getClassName().contains(Constants.PACKAGE)) {
			return true;
		}
		return false;
	}

}// end class
