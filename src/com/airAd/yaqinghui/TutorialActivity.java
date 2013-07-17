package com.airAd.yaqinghui;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.airAd.yaqinghui.fragment.LoginFragment;
import com.airAd.yaqinghui.fragment.TutorialOne;
import com.airAd.yaqinghui.fragment.TutorialVedio;
import com.airAd.yaqinghui.ui.IndexView;
/**
 * 教学页面
 * 
 * @author Panyi
 * 
 */
public class TutorialActivity extends BaseActivity
{
	private ViewPager mGallery;
	private IndexView mIndexView;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tutorial);
		mGallery= (ViewPager) findViewById(R.id.gallery);
		mIndexView= (IndexView) findViewById(R.id.gallery_index);
		final int subNum= getIntent().getIntExtra("subNum", 1);// 确定显示何种页面
		if (subNum == 3)
		{// 显示引导页面
			mGallery.setAdapter(new TutorialAdapter(this.getSupportFragmentManager()));
		}
		else if (subNum == 2)
		{
			mGallery.setAdapter(new HelpAdapter(this.getSupportFragmentManager()));
		}
		else
		{// 仅仅显示登陆功能页面
			mGallery.setAdapter(new LoginAdapter(this.getSupportFragmentManager()));
		}
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState)
	{
		super.onPostCreate(savedInstanceState);
	}
	private final class HelpAdapter extends FragmentStatePagerAdapter
	{
		public HelpAdapter(FragmentManager fm)
		{
			super(fm);
		}
		@Override
		public Fragment getItem(int index)
		{
			Fragment fm= null;
			switch (index)
			{
				case 0 :
					fm= TutorialOne.newInstance();
					break;
				case 1 :
					fm= TutorialVedio.newInstance();
					break;
			}
			return fm;
		}
		@Override
		public int getCount()
		{
			return 2;
		}
	}// end inner class
	private final class LoginAdapter extends FragmentStatePagerAdapter
	{
		public LoginAdapter(FragmentManager fm)
		{
			super(fm);
		}
		@Override
		public Fragment getItem(int index)
		{
			return LoginFragment.newInstance();
		}
		@Override
		public int getCount()
		{
			return 1;
		}
	}// end inner class
	/**
	 * 三页面
	 * 
	 * @author Administrator
	 * 
	 */
	private final class TutorialAdapter extends FragmentStatePagerAdapter
	{
		public TutorialAdapter(FragmentManager fm)
		{
			super(fm);
		}
		@Override
		public Fragment getItem(int index)
		{
			Fragment fm= null;
			switch (index)
			{
				case 0 :
					fm= TutorialOne.newInstance();
					break;
				case 1 :
					fm= TutorialVedio.newInstance();
					break;
				case 2 :
					fm= LoginFragment.newInstance();
					break;
			}
			return fm;
		}
		@Override
		public int getCount()
		{
			return 3;
		}
	}// end inner class
}// end class
