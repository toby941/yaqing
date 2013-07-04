package com.airAd.yaqinghui.fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.business.model.CepEvent;
/**
 * 
 * @author Panyi
 * 
 */
public class CepEventItem extends Fragment
{
	private CepEvent cepEvent;
	public static CepEventItem newInstance(CepEvent data)
	{
		final CepEventItem f= new CepEventItem();
		f.cepEvent= data;
		return f;
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View v= inflater.inflate(R.layout.cep_event, container, false);
		TextView timeText= (TextView) v.findViewById(R.id.cep_event_time);
		timeText.setText(cepEvent.getStartTime() + "-" + cepEvent.getEndTime());
		TextView locateText= (TextView) v.findViewById(R.id.cep_event_locate);
		locateText.setText(cepEvent.getPlace());
		TextView attendText= (TextView) v.findViewById(R.id.cep_event_attend);
		attendText.setText("10/50");
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
}// end class
