/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.airAd.yaqinghui.business.LocService;
import com.airAd.yaqinghui.business.model.LocMarker;
import com.airAd.yaqinghui.common.AMapUtil;
import com.airAd.yaqinghui.ui.BackBaseActivity;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.AMap.OnMarkerDragListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

/**
 * SurroundingActivity.java
 * 
 * @author pengf
 */
public class SurroundingActivity extends BackBaseActivity implements
		OnMarkerClickListener, OnInfoWindowClickListener,
		OnMapLoadedListener, InfoWindowAdapter {

	private LocService locService;
	private View mWindow;
	private View mContents;
	private AMap aMap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surrouding_map);
		locService = new LocService(); 
		init();
		new LocAsyncTask().execute();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		mWindow = getLayoutInflater()
				.inflate(R.layout.surrounding_info_window, null);
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (AMapUtil.checkReady(this, aMap)) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {

		aMap.getUiSettings().setZoomControlsEnabled(false);// 隐藏缩放按钮

		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		//addMarkersToMap();// 往地图上添加marker
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amap.api.maps.AMap.InfoWindowAdapter#getInfoContents(com.amap.api
	 * .maps.model.Marker)
	 */
	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amap.api.maps.AMap.InfoWindowAdapter#getInfoWindow(com.amap.api.maps
	 * .model.Marker)
	 */
	@Override
	public View getInfoWindow(Marker arg0) {
		return mWindow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amap.api.maps.AMap.OnMapLoadedListener#onMapLoaded()
	 */
	@Override
	public void onMapLoaded() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amap.api.maps.AMap.OnInfoWindowClickListener#onInfoWindowClick(com
	 * .amap.api.maps.model.Marker)
	 */
	@Override
	public void onInfoWindowClick(Marker marker) {
		final String id = marker.getSnippet();
		new Thread(new Runnable(){

			@Override
			public void run() {
				locService.getLocationDetail(id);				
			}}).start();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amap.api.maps.AMap.OnMarkerClickListener#onMarkerClick(com.amap.api
	 * .maps.model.Marker)
	 */
	@Override
	public boolean onMarkerClick(Marker arg0) {
		return false;
	}
	
	private class LocAsyncTask extends AsyncTask<Void, Void, List<LocMarker>>
	{

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected List<LocMarker> doInBackground(Void... params) {
			return locService.getLocations();
		}

		@Override
		protected void onPostExecute(List<LocMarker> result) {
			boolean isMoved = false;
			com.amap.api.maps.model.LatLngBounds.Builder builer = new LatLngBounds.Builder();
			
			for(LocMarker locMarker : result)
			{
				Log.i("locMarker", locMarker.toString());
				LatLng ll = new LatLng(locMarker.getLat(), locMarker.getLon());
				builer.include(ll);
				aMap.addMarker(new MarkerOptions()
				.position(ll)
				.snippet(locMarker.getId())
				.title(locMarker.getName())
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.surrounding_map_mark)));
				if(!isMoved)
				{
					CameraPosition cp = new CameraPosition.Builder()
					.target(ll).build();
					aMap.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
					isMoved = true;
				}
			}
			aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builer.build(), 20));
		}
		
	}
}
