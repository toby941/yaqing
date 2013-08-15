package com.airAd.yaqinghui;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
/**
 * CEP活动
 * 
 * @author Panyi
 */
public class HelpActivity extends BaseActivity
{
	private ImageButton mBack;
	private Button mMapBtn;
	private Button mTraffic, mReligion, mEatting, mLan;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		init();
	}
	private void init()
	{
		mBack= (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBack.setOnClickListener(new BackClick());
		mMapBtn= (Button) findViewById(R.id.map);
		mMapBtn.setOnClickListener(new MapClick());
		mTraffic= (Button) findViewById(R.id.traffic);
		mReligion= (Button) findViewById(R.id.religion);
		mEatting= (Button) findViewById(R.id.eating);
		mLan= (Button) findViewById(R.id.language);
		mTraffic.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent it= new Intent(HelpActivity.this, HelpServiceActivity.class);
				it.putExtra(HelpServiceActivity.TYPE, HelpServiceActivity.TYPE_TRAFFIC);
				HelpActivity.this.startActivity(it);
			}
		});
		mReligion.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent it= new Intent(HelpActivity.this, HelpServiceActivity.class);
				it.putExtra(HelpServiceActivity.TYPE, HelpServiceActivity.TYPE_RELIGION);
				HelpActivity.this.startActivity(it);
			}
		});
		mEatting.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent it= new Intent(HelpActivity.this, HelpServiceActivity.class);
				it.putExtra(HelpServiceActivity.TYPE, HelpServiceActivity.TYPE_EATTING);
				HelpActivity.this.startActivity(it);
			}
		});
		mLan.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent it= new Intent(HelpActivity.this, HelpServiceActivity.class);
				it.putExtra(HelpServiceActivity.TYPE, HelpServiceActivity.TYPE_LAN);
				HelpActivity.this.startActivity(it);
			}
		});
	}
	private final class MapClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Intent it= new Intent();
			it.setClass(HelpActivity.this, MapScanActivity.class);
			HelpActivity.this.startActivity(it);
		}
	}// end inner class
	private final class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			HelpActivity.this.finish();
		}
	}// end inner class
	@Override
	public void onPause()
	{
		super.onPause();
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}// end class
