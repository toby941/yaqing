package com.airAd.yaqinghui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.airAd.yaqinghui.util.AMapUtil;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

public class SurroundActivity extends BaseActivity implements
		OnMarkerClickListener {
	private AMap aMap;
	private UiSettings mUiSettings;
	private ImageButton mBack;

	private Marker marker1, marker2, marker3;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surround);
		init();
	}

	private void init() {
		mBack = (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBack.setOnClickListener(new BackClick());
		initMap();
	}

	private void initMap() {
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
		}
		if (AMapUtil.checkReady(this, aMap)) {
			mUiSettings = aMap.getUiSettings();
			mUiSettings.setRotateGesturesEnabled(false);
			mUiSettings.setZoomControlsEnabled(false);
			aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Config.NANJING,
					16));
			
			marker1 = aMap.addMarker(new MarkerOptions().title("CEP").position(
					new LatLng(32.048072, 118.79058)).icon(
					BitmapDescriptorFactory.fromResource (R.drawable.map_point_bg)));
			marker2 = aMap.addMarker(new MarkerOptions().title("CEP").position(
					new LatLng(32.047972, 118.79368)).icon(
					BitmapDescriptorFactory.fromResource (R.drawable.map_point_bg)));
			marker3 = aMap.addMarker(new MarkerOptions().position(
					new LatLng(32.047692, 118.79258)).icon(
					BitmapDescriptorFactory.fromResource (R.drawable.map_self_point)));
			aMap.setOnMarkerClickListener(this);// ��marker��ӵ��������
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		Intent intent = new Intent();
		intent.setClass(SurroundActivity.this, CallDialogActivity.class);
		SurroundActivity.this.startActivity(intent);
		return false;
	}

	private final class BackClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			SurroundActivity.this.finish();
		}
	}// end inner class

}
