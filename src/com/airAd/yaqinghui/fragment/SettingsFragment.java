package com.airAd.yaqinghui.fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.common.Config;
/**
 * 设置Fragment
 * 
 * @author Panyi
 */
public class SettingsFragment extends Fragment
{
	public static final int DEFAULT_BEFORE_TIME= 30;
	public static final String DEFAULT_REGULAR_TIME= "6:30am";
	private ImageButton mBack;
	private ImageView isOpenEventRemind;
	private TextView eventRemindText;
	private boolean isOpenEventRemind_flag;
	private int eventRemindBeforeValue;
	private TextView dialyText;
	private ImageView dialyImage;
	private boolean isDialyReminderOpen;
	private String dialyRemindString;
	public static SettingsFragment newInstance()
	{
		SettingsFragment fragment= new SettingsFragment();
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view= inflater.inflate(R.layout.fragment_settings, container, false);
		SharedPreferences sp= getActivity().getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE);
		mBack= (ImageButton) view.findViewById(R.id.back);
		mBack.setOnClickListener(new BackClick());
		eventRemindText= (TextView) view.findViewById(R.id.event_remind_text);
		isOpenEventRemind= (ImageView) view.findViewById(R.id.event_remind_isopen);
		isOpenEventRemind_flag= sp.getBoolean(Config.EVENT_REMIND_ISOPEN, true);
		eventRemindBeforeValue= sp.getInt(Config.EVENT_REMIND_BEFORE, DEFAULT_BEFORE_TIME);
		isOpenEventRemind.setImageResource(isOpenEventRemind_flag ? R.drawable.switch_open : R.drawable.switch_close);
		eventRemindText.setText(getActivity().getString(R.string.event_remind_text, eventRemindBeforeValue));
		dialyText= (TextView) view.findViewById(R.id.dialy_remind_text);
		dialyImage= (ImageView) view.findViewById(R.id.dialy_remind_isopen);
		isDialyReminderOpen= sp.getBoolean(Config.REGULAR_REMIND_ISOPEN, true);
		dialyRemindString= sp.getString(Config.REGULAR_REMIND_TIME, DEFAULT_REGULAR_TIME);
		dialyText.setText(getActivity().getString(R.string.dialy_remind_text, dialyRemindString));
		dialyImage.setImageResource(isDialyReminderOpen ? R.drawable.switch_open : R.drawable.switch_close);
		return view;
	}
	@Override
	public void onStop()
	{
		super.onStop();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		isOpenEventRemind.setOnClickListener(new OpenEventRemind());
		dialyImage.setOnClickListener(new OpenDialyRemind());
	}
	private final class OpenEventRemind implements OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			isOpenEventRemind_flag= !isOpenEventRemind_flag;
			SharedPreferences sp= getActivity().getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE);
			Editor ed= sp.edit();
			ed.putBoolean(Config.EVENT_REMIND_ISOPEN, isOpenEventRemind_flag);
			ed.commit();
			isOpenEventRemind.setImageResource(isOpenEventRemind_flag
					? R.drawable.switch_open
					: R.drawable.switch_close);
		}
	}//end inner class
	private final class OpenDialyRemind implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			isDialyReminderOpen= !isDialyReminderOpen;
			SharedPreferences sp= getActivity().getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE);
			Editor ed= sp.edit();
			ed.putBoolean(Config.REGULAR_REMIND_ISOPEN, isDialyReminderOpen);
			ed.commit();
			dialyImage.setImageResource(isDialyReminderOpen ? R.drawable.switch_open : R.drawable.switch_close);
		}
	}//end inner class
	private final class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			getActivity().getSupportFragmentManager().beginTransaction().hide(SettingsFragment.this).commit();
		}
	}//end inner class
}// end class
