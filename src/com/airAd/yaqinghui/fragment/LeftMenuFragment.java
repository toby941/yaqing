package com.airAd.yaqinghui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.core.ImageFetcherFactory;
import com.airAd.yaqinghui.ui.CustomViewPager;
public class LeftMenuFragment extends Fragment
{
	UserFragment userFragment;
	UserDetailFragment userDetailFragment;
	ImageFetcher mImageFetcher;
	private CustomViewPager leftGalllery;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view= inflater.inflate(R.layout.menu_left, null);
		leftGalllery= (CustomViewPager) view.findViewById(R.id.left_menugallery);
		leftGalllery.setAdapter(new LeftMenuAdapter(getActivity().getSupportFragmentManager()));
		return view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mImageFetcher= ImageFetcherFactory.genImageFetcher(getActivity());
	}
	private final class LeftMenuAdapter extends FragmentStatePagerAdapter
	{
		public LeftMenuAdapter(FragmentManager fm)
		{
			super(fm);
		}
		@Override
		public Fragment getItem(int index)
		{
			if (index == 0)
			{
				if (userFragment == null)
				{
					userFragment= UserFragment.newInstance(leftGalllery);
				}
				return userFragment;
			}
			else
			{
				if (userDetailFragment == null)
				{
					userDetailFragment= UserDetailFragment.newInstance(leftGalllery, mImageFetcher);
				}
				return userDetailFragment;
			}
		}
		@Override
		public int getCount()
		{
			return 2;
		}
	}// end inner class
}
