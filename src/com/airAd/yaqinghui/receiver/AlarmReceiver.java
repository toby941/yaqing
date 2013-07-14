package com.airAd.yaqinghui.receiver;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore.Audio;

import com.airAd.yaqinghui.NotifyDetailActivity;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.business.model.NotificationMessage;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.common.Config;
import com.airAd.yaqinghui.common.Constants;
public class AlarmReceiver extends BroadcastReceiver
{
	private Context mContext;
	private User mUser;
	private NotificationMessage message;
	@Override
	public void onReceive(Context context, Intent intent)
	{
		mContext= context;
		try
		{
			mUser= User.instance(mContext.getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE).getString(
					Config.USER_INFO_KEY,
					""));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			mUser= null;
			return;
		}
		getPushMessage(intent);
	}
	/**
	 * 
	 * @param origin
	 */
	private void getPushMessage(Intent intent)
	{
		if (isInApp())
		{

		}
		else
		{

		}
	}

	private void showPushNotify(long id)
	{
		NotificationManager manager= (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification= new Notification(R.drawable.icon, message.getTitle(), System.currentTimeMillis());
		Intent it= new Intent(mContext, NotifyDetailActivity.class);
		it.putExtra("id", id);
		PendingIntent contentIntent= PendingIntent.getActivity(mContext, 0, it, 0);
		notification.setLatestEventInfo(mContext, message.getTitle(), message.getContent(), contentIntent);
		notification.defaults|= Notification.DEFAULT_SOUND;
		notification.defaults|= Notification.DEFAULT_VIBRATE;
		notification.flags= Notification.FLAG_AUTO_CANCEL;
		notification.sound= Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
		manager.notify(R.drawable.icon, notification);
	}
	private void showPushMsgWithPop(long id)
	{
		Intent intent= new Intent();
		intent.setAction(Constants.PUSH_SERVICE);
		intent.putExtra("title", message.getContent());
		intent.putExtra("id", id);
		mContext.sendBroadcast(intent);
	}
	public boolean isInApp()
	{
		boolean ret= false;
		ActivityManager am= (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list= am.getRunningTasks(100);
		for (RunningTaskInfo info : list)
		{
			if (info.topActivity.getPackageName().equals(Config.PACKAGE)
					&& info.baseActivity.getPackageName().equals(Config.PACKAGE))
			{
				ret= true;
				break;
			}
		}
		return ret;
	}
}//end class
