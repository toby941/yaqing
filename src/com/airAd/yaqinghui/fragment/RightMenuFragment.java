package com.airAd.yaqinghui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.airAd.yaqinghui.CepActivity;
import com.airAd.yaqinghui.GameScheduleActivity;
import com.airAd.yaqinghui.PlayVideoActivity;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.SurroundingActivity;
public class RightMenuFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view= inflater.inflate(R.layout.menu_rights, null);
		View acy= view.findViewById(R.id.activity);
		acy.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent= new Intent();
				intent.setClass(getActivity(), GameScheduleActivity.class);
				getActivity().startActivity(intent);
			}
		});
		View cep= view.findViewById(R.id.cep);
		cep.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent= new Intent();
				intent.setClass(getActivity(), CepActivity.class);
				getActivity().startActivity(intent);
			}
		});
		View video= view.findViewById(R.id.video);
		video.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent= new Intent();
				intent.setClass(getActivity(), PlayVideoActivity.class);
				getActivity().startActivity(intent);
			}
		});
		View equip= view.findViewById(R.id.equip);
		equip.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent= new Intent();
				intent.setClass(getActivity(), SurroundingActivity.class);
				getActivity().startActivity(intent);
			}
		});
		return view;
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}
}
