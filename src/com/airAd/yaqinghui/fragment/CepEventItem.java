package com.airAd.yaqinghui.fragment;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airAd.yaqinghui.CepDetailActivity;
import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.business.CepService;
import com.airAd.yaqinghui.business.model.Cep;
import com.airAd.yaqinghui.business.model.CepEvent;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.google.zxing.client.android.CaptureActivity;
/**
 * 
 * @author Panyi
 * 
 */
public class CepEventItem extends Fragment
{
	private Cep cep;
	private CepEvent cepEvent;
	private Button signBtn;
	private LocationManagerProxy locationManager;
	private boolean isLocating= false;
	private ProgressDialog mLocateProgressDialog;
	private Button attendBtn, scoreBtn;
	private View scoreView;
	private PopupWindow popWindow;
	private Button submitBtn;
	private RatingBar scoreBar1, scoreBar2, scoreBar3;
	private View parentView;
	private CepService cepSerice;
	public AMapLocationListener locationListener= new AMapLocationListener()
	{
		@Override
		public void onLocationChanged(Location location)
		{
		}
		@Override
		public void onProviderDisabled(String provider)
		{
		}
		@Override
		public void onProviderEnabled(String provider)
		{
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
		}
		@Override
		public void onLocationChanged(AMapLocation location)
		{
			if (location != null)
			{
				Double geoLat= location.getLatitude();
				Double geoLng= location.getLongitude();
				System.out.println("location3--->" + geoLat + "," + geoLng);
				locationManager.removeUpdates(this);
				CepDetailActivity cepActivity= (CepDetailActivity) getActivity();
				cepActivity.lat= geoLat + "";
				cepActivity.lng= geoLng + "";
				Intent it= new Intent(getActivity(), CaptureActivity.class);
				getActivity().startActivityForResult(it, UserFragment.SCAN_QRCODE);
				isLocating= false;
				if (mLocateProgressDialog.isShowing())
					mLocateProgressDialog.dismiss();
			}
		}
	};
	public static CepEventItem newInstance(CepEvent data, Cep cep)
	{
		final CepEventItem f= new CepEventItem();
		f.cepEvent= data;
		f.cep= cep;
		return f;
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		cepSerice= new CepService();
		locationManager= LocationManagerProxy.getInstance(getActivity());
		mLocateProgressDialog= new ProgressDialog(getActivity());
		mLocateProgressDialog.setTitle(R.string.dialog_title);
		mLocateProgressDialog.setMessage(getResources().getText(R.string.is_locating));
		mLocateProgressDialog.setCancelable(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		parentView= getActivity().findViewById(R.id.main);
		final View v= inflater.inflate(R.layout.cep_event, container, false);
		TextView timeText= (TextView) v.findViewById(R.id.cep_event_time);
		long startTimeLong= Long.parseLong(cepEvent.getStartTime());
		SimpleDateFormat sdfStart= new SimpleDateFormat("MM.dd.yyyy HH:mm");
		String startStr= sdfStart.format(new Date(startTimeLong));
		long endTimeLong= Long.parseLong(cepEvent.getEndTime());
		SimpleDateFormat sdfEnd= new SimpleDateFormat("HH:mm");
		String endStr= sdfEnd.format(new Date(endTimeLong));
		timeText.setText(startStr + "-" + endStr);
		attendBtn= (Button) v.findViewById(R.id.attendBtn);
		scoreBtn= (Button) v.findViewById(R.id.scoreBtn);
		TextView locateText= (TextView) v.findViewById(R.id.cep_event_locate);
		locateText.setText(cepEvent.getPlace());
		setPopWindow(inflater);
		if (CepEvent.CEP_EVENT_TYPE_IN.equals(cepEvent.getCepEventType()))
		{//
			attendBtn.setVisibility(View.GONE);
		}
		if (cepEvent.canCheckIn())// 可报名
		{// attendBtn
			attendBtn.setBackgroundResource(R.drawable.prepost_bg);
			attendBtn.setOnClickListener(new AttendClick());
		}
		else
		{
			attendBtn.setBackgroundResource(R.drawable.sign_in_bg);
		}
		signBtn= (Button) v.findViewById(R.id.signin);
		if (cepEvent.canSignUp())// 可签到
		{
			signBtn.setBackgroundResource(R.drawable.prepost_bg);
			signBtn.setOnClickListener(new ScanClick());
		}
		else
		{
			signBtn.setBackgroundResource(R.drawable.sign_in_bg);
		}
		if (cepEvent.canScored())// 可评分
		{
			scoreBtn.setBackgroundResource(R.drawable.prepost_bg);
			scoreBtn.setOnClickListener(new ScoreClick());
		}
		else
		{
			scoreBtn.setBackgroundResource(R.drawable.sign_in_bg);
		}
		return v;
	}
	private void setPopWindow(LayoutInflater inflater)
	{
		scoreView= inflater.inflate(R.layout.score_pop, null);
		popWindow= new PopupWindow(scoreView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		popWindow.setFocusable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setAnimationStyle(R.style.PopupAnimation);
		submitBtn= (Button) scoreView.findViewById(R.id.submit);
		scoreBar1= (RatingBar) scoreView.findViewById(R.id.score1);
		scoreBar2= (RatingBar) scoreView.findViewById(R.id.score2);
		scoreBar3= (RatingBar) scoreView.findViewById(R.id.score3);
		submitBtn.setOnClickListener(new SubmitClick());
	}
	private final class SubmitClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			popWindow.dismiss();
			int score1= scoreBar1.getProgress();
			int score2= scoreBar2.getProgress();
			int score3= scoreBar3.getProgress();
			System.out.println(score1 + "," + score2 + "," + score3);
		}
	}
	private final class AttendClick implements OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			System.out.println("报名参加");
			cepSerice.doReservationCepEvent(MyApplication.getCurrentApp().getUser().getId(), cep, cepEvent);
		}
	}//end inner class
	private final class ScoreClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			popWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		}
	}//end inner class
	private final class ScanClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if (isLocating)
 {// 当前正在定位 按钮不响应
				return;
			}
			if (openGPSSettings())
 {// GPS确保打开
				locationManager.removeUpdates(locationListener);
				locationManager.setGpsEnable(true);
				locationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 5000, 10, locationListener);
				isLocating= true;
				mLocateProgressDialog.show();
			}
		}
	}// end inner class
	private boolean openGPSSettings()
	{
		LocationManager alm= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			return true;
		}
		else
		{
			Toast.makeText(getActivity(), R.string.opengps, Toast.LENGTH_SHORT).show();
			getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			return false;
		}
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
		locationManager.removeUpdates(locationListener);
		System.gc();
	}
}// end class
