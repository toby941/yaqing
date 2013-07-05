package com.airAd.yaqinghui.fragment;
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
/**
 * 设置Fragment
 * 
 * @author Panyi
 */
public class SettingsFragment extends Fragment
{
	private ImageButton mBack;
	private ImageView isOpenEventRemind;
	private TextView eventRemindText;
	public static SettingsFragment newInstance()
	{
		SettingsFragment fragment= new SettingsFragment();
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view= inflater.inflate(R.layout.fragment_settings, container, false);
		mBack= (ImageButton) view.findViewById(R.id.back);
		mBack.setOnClickListener(new BackClick());
		eventRemindText= (TextView) view.findViewById(R.id.event_remind_text);
		isOpenEventRemind= (ImageView) view.findViewById(R.id.event_remind_isopen);

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
	}
	private final class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			getActivity().getSupportFragmentManager().beginTransaction().hide(SettingsFragment.this).commit();
		}
	}//end inner class
}// end class
