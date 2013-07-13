package com.airAd.yaqinghui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.airAd.yaqinghui.R;

/**
 *单纯展示页
 * @author Panyi
 * 
 */
public class TutorialOne extends Fragment {
	public static TutorialOne newInstance() {
		TutorialOne fragment = new TutorialOne();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tutorial_pageone, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}// end class
