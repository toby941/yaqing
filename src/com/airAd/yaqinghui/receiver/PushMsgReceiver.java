package com.airAd.yaqinghui.receiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;
public class PushMsgReceiver extends BroadcastReceiver
{
	private Context mContext;
	@Override
	public void onReceive(Context context, Intent intent)
	{
		mContext= context;
		Bundle bundle= intent.getExtras();
		if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
		{// 推送的消息
			//			System.out.println("接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
			String orignJson= bundle.getString(JPushInterface.EXTRA_MESSAGE);
			getPushMessage(orignJson);
		}
	}

	private void getPushMessage(String origin)
	{
		System.out.println("推送消息--->" + origin);
	}

}//end class
