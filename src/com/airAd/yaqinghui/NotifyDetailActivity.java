package com.airAd.yaqinghui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.airAd.yaqinghui.business.NotificationMessageService;
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
	private NotificationMessageService notifyService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notify_detail);
		init();
	}

	private void init()
	{
		notifyService= new NotificationMessageService();
		mBack= (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBack.setOnClickListener(new BackClick());
		title= (TextView) findViewById(R.id.title);
		time= (TextView) findViewById(R.id.time);
		content= (TextView) findViewById(R.id.content);
		long id= getIntent().getLongExtra("id", -2);
		if (id < 0)
		{
			Object obj= MyApplication.getCurrentApp().pop();
			if (obj instanceof NoficationMessage)
			{
				mNotificationMessage= (NoficationMessage) obj;
			}
		}
		else
		{
			mNotificationMessage= notifyService.getMessage(id);
		}

		if (mNotificationMessage == null)
		{
			return;
		}
		notifyService.markMessageAsRead(mNotificationMessage.getCid());
		title.setText(mNotificationMessage.getTitle());
		time.setText(Common.timeNotifyString(mNotificationMessage.getAddTimel()));
		content.setText(mNotificationMessage.getContent());

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
