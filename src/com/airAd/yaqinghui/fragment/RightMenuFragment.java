package com.airAd.yaqinghui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.airAd.yaqinghui.CepActivity;
import com.airAd.yaqinghui.GameScheduleActivity;
import com.airAd.yaqinghui.PlayVideoActivity;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.ui.IconListItem;
public class RightMenuFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view= inflater.inflate(R.layout.menu_rights, null);
		LinearLayout layout= (LinearLayout) view.findViewById(R.id.menu_list);
		IconListItem iconListItemSchedule= new IconListItem(getActivity());
		iconListItemSchedule
				.setIcon(R.drawable.icon_schedule_bg, R.drawable.item_dialy_bg, R.string.menu_left_schedule);
		iconListItemSchedule.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent= new Intent();
				intent.setClass(getActivity(), GameScheduleActivity.class);
				getActivity().startActivity(intent);
			}
		});
		layout.addView(iconListItemSchedule);
		IconListItem cep= new IconListItem(getActivity());
		cep.setIcon(R.drawable.icon_schedule_bg, R.drawable.cep_icon, R.string.cep_menu_title);
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
		layout.addView(cep);
		IconListItem video= new IconListItem(getActivity());
		video.setIcon(R.drawable.icon_schedule_bg, R.drawable.video_icon, R.string.cep_video);
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
		layout.addView(video);
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
