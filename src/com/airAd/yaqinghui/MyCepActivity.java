package com.airAd.yaqinghui;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.fragment.UserFragment;
/**
 * CEP活动
 * 
 * @author Panyi
 */
public class MyCepActivity extends BaseActivity
{
	private User mUser;
	private ImageButton mBack;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycep);
		init();
	}
	private void init()
	{
		mUser= MyApplication.getCurrentUser();
		mBack= (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBack.setOnClickListener(new BackClick());
		int type= getIntent().getIntExtra(UserFragment.MYCEP_TYPE, 0);
		switch (type)
		{
			case UserFragment.MYCEP_ORDER :
				break;
			case UserFragment.MYCEP_SIGNIN :
				break;
			case UserFragment.MYCEP_COMMENT :
				break;
		}//end switch
	}


	private final class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			MyCepActivity.this.finish();
		}
	}// end inner class
}// end class
