package com.airAd.yaqinghui;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
/**
 * @author Panyi
 * 
 */
public class ChangePwdActivity extends BaseActivity
{
	private ImageButton mBackBtn;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);
		init();
	}
	private void init()
	{
		mBackBtn= (ImageButton) findViewById(R.id.back_btn);
		mBackBtn.setOnClickListener(new BackClick());
	}

	private final class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			ChangePwdActivity.this.finish();
		}
	}// end inner class
}// end class
