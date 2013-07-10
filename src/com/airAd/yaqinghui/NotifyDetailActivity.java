package com.airAd.yaqinghui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import com.airAd.yaqinghui.business.model.NoficationMessage;
import com.airAd.yaqinghui.common.Common;

/**
 * 
 * @author Panyi
 * 
 */
public class NotifyDetailActivity extends BaseActivity
{
	private ImageButton mBack;
	private TextView title, time, content;
	private NoficationMessage mNotificationMessage;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.notify_detail);
		init();
	}
	private void init()
	{
		mBack= (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBack.setOnClickListener(new BackClick());
		title= (TextView) findViewById(R.id.title);
		time= (TextView) findViewById(R.id.time);
		content= (TextView) findViewById(R.id.content);
		Object obj= MyApplication.getCurrentApp().pop();
		if (obj instanceof NoficationMessage)
		{
			mNotificationMessage= (NoficationMessage) obj;
			title.setText(mNotificationMessage.getTitle());
			time.setText(Common.timeNotifyString(mNotificationMessage.getAddTimel()));
			content.setText(mNotificationMessage.getContent());
		}
	}
	private final class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			NotifyDetailActivity.this.finish();
		}
	}// end inner class
}// end class
