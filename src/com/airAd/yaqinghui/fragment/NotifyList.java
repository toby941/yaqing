package com.airAd.yaqinghui.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.airAd.yaqinghui.R;
/**
 * 设置Fragment
 * 
 * @author Panyi
 */
public class NotifyList extends Fragment
{
	private ImageButton mBack;
	public static NotifyList newInstance()
	{
		NotifyList fragment= new NotifyList();
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view= inflater.inflate(R.layout.fragment_notify_list, container, false);
		mBack= (ImageButton) view.findViewById(R.id.back);
		mBack.setOnClickListener(new BackClick());
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
			getActivity().getSupportFragmentManager().beginTransaction().hide(NotifyList.this).commit();
		}
	}//end inner class
}// end class
