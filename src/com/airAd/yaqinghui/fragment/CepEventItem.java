package com.airAd.yaqinghui.fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airAd.yaqinghui.CepDetailActivity;
import com.airAd.yaqinghui.R;
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
	private CepEvent cepEvent;
	private Button signBtn;
	private LocationManagerProxy locationManager;
	private boolean isLocating= false;
	private ProgressDialog mLocateProgressDialog;
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
		locationManager= LocationManagerProxy.getInstance(getActivity());
		mLocateProgressDialog= new ProgressDialog(getActivity());
		mLocateProgressDialog.setTitle(R.string.dialog_title);
		mLocateProgressDialog.setMessage(getResources().getText(R.string.is_locating));
		mLocateProgressDialog.setCancelable(true);
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
		signBtn= (Button) v.findViewById(R.id.signin);
		signBtn.setOnClickListener(new ScanClick());
		return v;
	}
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
