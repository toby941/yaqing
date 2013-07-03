package com.airAd.yaqinghui.fragment;

import com.airAd.yaqinghui.HomeActivity;
import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.ui.CustomViewPager;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.google.zxing.client.android.CaptureActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Panyi
 * 
 */
public class UserFragment extends Fragment {
	public static final int SCAN_QRCODE = 400;

	protected CustomViewPager mGallery;
	private ImageView thumbImage;
	private Button signImage;
	private boolean isLocating = false;
	private User user;

	public AMapLocationListener locationListener = new AMapLocationListener() {
		@Override
		public void onLocationChanged(Location location) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onLocationChanged(AMapLocation location) {
			if (location != null) {
				Double geoLat = location.getLatitude();
				Double geoLng = location.getLongitude();
				System.out.println("location--->" + geoLat + "," + geoLng);
				locationManager.removeUpdates(this);
				Intent it = new Intent(getActivity(), CaptureActivity.class);
				getActivity().startActivityForResult(it, SCAN_QRCODE);
				isLocating = false;
			}
		}
	};

	LocationManagerProxy locationManager;

	public static UserFragment newInstance(CustomViewPager gallery) {
		final UserFragment f = new UserFragment();
		f.mGallery = gallery;
		return f;
	}

	public ImageView getThumb() {
		return thumbImage;
	}

	private UserFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		user = MyApplication.getCurrentApp().getUser();
		locationManager = LocationManagerProxy.getInstance(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.menu_lefts_user, container,
				false);

		// TODO
		setThumbImage(view);

		thumbImage = (ImageView) view.findViewById(R.id.headpic);
		thumbImage.setOnClickListener(new ThumbClick());
		signImage = (Button) view.findViewById(R.id.sign);
		signImage.setOnClickListener(new ScanClick());
		
		if(user!=null){
			TextView userName = (TextView)view.findViewById(R.id.username);
			userName.setText(user.getName());//设置用户名
		}
		return view;
	}

	private void setThumbImage(View view) {
		ImageView userImage = (ImageView) view.findViewById(R.id.headpic);
		userImage.setImageBitmap(((HomeActivity) getActivity())
				.getThumbBitmap());
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		locationManager.removeUpdates(locationListener);
		System.gc();
	}

	private final class ThumbClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			mGallery.setCurrentItem(1);// 跳转到个人信息页
		}
	}// end inner class

	private final class ScanClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			if(isLocating){//当前正在定位  按钮不响应
				return;
			}
			
			if (openGPSSettings()) {// GPS确保打开
				locationManager.removeUpdates(locationListener);
				locationManager.setGpsEnable(true);
				locationManager.requestLocationUpdates(
						LocationProviderProxy.AMapNetwork, 5000, 10,
						locationListener);
				isLocating = true;
			}
		}
	}// end inner class

	private boolean openGPSSettings() {
		LocationManager alm = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true;
		} else {
			Toast.makeText(getActivity(), R.string.opengps, Toast.LENGTH_SHORT)
					.show();
			getActivity().startActivity(
					new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			return false;
		}
	}
}// end class
