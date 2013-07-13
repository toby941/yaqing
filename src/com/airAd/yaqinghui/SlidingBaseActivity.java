package com.airAd.yaqinghui;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.airAd.yaqinghui.common.Constants;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
public class SlidingBaseActivity extends SlidingFragmentActivity
{
	private PushReceiver mPushReceiver;
	private LayoutInflater mInflater;
	private PopupWindow popWindow;
	private View popContentView;
	private View parentView;
	private View close;
	private TextView titleText;
	private View scanBtn;
	public void onCreate(Bundle savedInstanceState)
	{
		setTheme(R.style.Theme_Sherlock_Light_NoActionBar);
		super.onCreate(savedInstanceState);
		parentView= findViewById(R.id.main);
		mInflater= (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setPopWindow();
		mPushReceiver= new PushReceiver();
	}
	private void setPopWindow()
	{
		popContentView= mInflater.inflate(R.layout.push_msg, null);
		popWindow= new PopupWindow(popContentView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		popWindow.setFocusable(true);
		//		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setAnimationStyle(R.style.PopupAnimation);
		close= popContentView.findViewById(R.id.cancel);
		close.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				popWindow.dismiss();
			}
		});
	}
	protected void onResume()
	{
		super.onResume();
		IntentFilter filter= new IntentFilter(Constants.PUSH_SERVICE);
		registerReceiver(mPushReceiver, filter);
	}
	protected void onPause()
	{
		super.onPause();
		unregisterReceiver(mPushReceiver);
	}
	private final class PushReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			showPop(intent);
		}
	} // end inner class

	protected void showPop(Intent intent)
	{
		String title= intent.getStringExtra("title");
		long id= intent.getLongExtra("id", -1);
		titleText= (TextView) popContentView.findViewById(R.id.content);
		scanBtn= popContentView.findViewById(R.id.scan);
		titleText.setText(title);
		scanBtn.setOnClickListener(new ScanBtn(id));
		popWindow.showAtLocation(findViewById(R.id.main), Gravity.CENTER, 0, 0);
	}

	private final class ScanBtn implements OnClickListener
	{
		long id;
		public ScanBtn(long id)
		{
			this.id= id;
		}
		@Override
		public void onClick(View v)
		{
			popWindow.dismiss();
			Intent it= new Intent(SlidingBaseActivity.this, NotifyDetailActivity.class);
			it.putExtra("id", id);
			SlidingBaseActivity.this.startActivity(it);
		}
	}
	protected View getParentView()
	{
		return findViewById(R.id.main);
	}
}
