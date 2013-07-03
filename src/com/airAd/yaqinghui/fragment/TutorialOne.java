package com.airAd.yaqinghui.fragment;

import java.util.Locale;

import com.airAd.yaqinghui.HomeActivity;
import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.WelcomeActivity;
import com.airAd.yaqinghui.common.Config;
import com.airAd.yaqinghui.common.StringUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

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
