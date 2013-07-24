/*
 * Copyright 2013 Mitian Technology, Co., Ltd. All rights reserved.
 */
package com.airAd.yaqinghui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airAd.yaqinghui.business.GameService;
import com.airAd.yaqinghui.business.LocService;
import com.airAd.yaqinghui.business.model.GameInfo;
import com.airAd.yaqinghui.business.model.LocMarker;
import com.airAd.yaqinghui.common.AMapUtil;
import com.airAd.yaqinghui.ui.BackBaseActivity;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
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
		OnInfoWindowClickListener, InfoWindowAdapter, OnMapClickListener,
		OnMarkerClickListener {

	private LocService locService;
	private GameService gameService;
	private List<LocMarker> markers;
	private List<GameInfo> gameInfoList = new ArrayList<GameInfo>();

	private AMap aMap;

	private View mInfoWindow;
	private View mContents;
	private View mParentView;

	private PopupWindow popWindow;
	private TextView titleView;
	private ListView listView;
	private DailyAdapter adapter;
	private ProgressBar progressBar;
	private Marker mCurrentMarker;
	private LocAsyncTask task;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surrouding_map);
		locService = new LocService();
		gameService = new GameService();
		init();
		if (task != null) {
			task.cancel(true);
		}
		task = new LocAsyncTask();
		task.execute();
		// new LocAsyncTask().execute();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (task != null) {
			task.cancel(true);
		}
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		mParentView = findViewById(R.id.main);
		mInfoWindow = getLayoutInflater().inflate(
				R.layout.surrounding_info_window, null);
		if (aMap == null) {
			aMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			if (AMapUtil.checkReady(this, aMap)) {
				setUpMap();
			}
		}
		setPopWindow();

	}

	private void setUpMap() {

		aMap.getUiSettings().setZoomControlsEnabled(false);// 隐藏缩放按钮
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
		aMap.setOnMapClickListener(this);
		aMap.setOnMarkerClickListener(this);
		// addMarkersToMap();// 往地图上添加marker
	}

	private void setPopWindow() {
		View popContentView = LayoutInflater.from(this).inflate(
				R.layout.surrounding_pop_window, null);
		progressBar = (ProgressBar) popContentView
				.findViewById(R.id.progressBar);
		popWindow = new PopupWindow(popContentView,
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popWindow.setFocusable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setAnimationStyle(R.style.PopupAnimation);
		listView = (ListView) popContentView.findViewById(R.id.listView);
		titleView = (TextView) popContentView.findViewById(R.id.titleView);
		adapter = new DailyAdapter();
		listView.setAdapter(adapter);
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
	public View getInfoWindow(Marker marker) {
		final String id = marker.getSnippet();
		mCurrentMarker = marker;
		LocMarker locMarker = findMarker(id);
		if (locMarker != null) {
			TextView textView = (TextView) mInfoWindow.findViewById(R.id.title);
			textView.setText(locMarker.getName());
		}
		return mInfoWindow;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amap.api.maps.AMap.OnMapClickListener#onMapClick(com.amap.api.maps
	 * .model.LatLng)
	 */
	@Override
	public void onMapClick(LatLng arg0) {
		if (mCurrentMarker != null) {
			mCurrentMarker.hideInfoWindow();
		}
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
		LocMarker locMarker = findMarker(id);
		new ListAsyncTask().execute(locMarker.getId());
		titleView.setText(locMarker.getName());
		popWindow.showAtLocation(mParentView, Gravity.BOTTOM, 0, 0);
		marker.hideInfoWindow();
	}

	private LocMarker findMarker(String id) {
		for (LocMarker marker : markers) {
			if (marker.getId().equals(id)) {
				return marker;
			}
		}
		return null;
	}

	private class LocAsyncTask extends AsyncTask<Void, Void, List<LocMarker>> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected List<LocMarker> doInBackground(Void... params) {
			return locService.getLocations();
		}

		@Override
		protected void onPostExecute(List<LocMarker> result) {
			if (result != null) {
				com.amap.api.maps.model.LatLngBounds.Builder builer = new LatLngBounds.Builder();
				markers = result;
				for (LocMarker locMarker : result) {
					LatLng ll = new LatLng(locMarker.getLat(),
							locMarker.getLon());
					builer.include(ll);
					aMap.addMarker(new MarkerOptions()
							.position(ll)
							.snippet(locMarker.getId())
							.title(locMarker.getName())
							.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.surrounding_map_mark)));
				}
				aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
						builer.build(), 20));
			}

		}

	}

	private class ListAsyncTask extends AsyncTask<String, Void, List<GameInfo>> {

		@Override
		protected void onPreExecute() {
			gameInfoList.clear();
			adapter.notifyDataSetChanged();
			listView.setVisibility(View.GONE);
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected List<GameInfo> doInBackground(String... params) {
			String placeId = params[0];
			Calendar c = Calendar.getInstance();
			return gameService.getGameInfoDetailByPlace(placeId, c.getTime());
		}

		@Override
		protected void onPostExecute(List<GameInfo> result) {
			if (result != null) {
				gameInfoList = result;
				adapter.notifyDataSetChanged();
			}
			listView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
		}
	}

	private class DailyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return gameInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.game_daily_item, null);
			}
			if (convertView.getTag() == null) {
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.titleView = (TextView) convertView
						.findViewById(R.id.game_title);
				viewHolder.locView = (TextView) convertView
						.findViewById(R.id.game_loc);
				viewHolder.dateView = (TextView) convertView
						.findViewById(R.id.date);
				viewHolder.addCheckBox = (CheckBox) convertView
						.findViewById(R.id.game_add_btn);
				viewHolder.itemView = convertView.findViewById(R.id.daily_item);
				convertView.setTag(viewHolder);
			}
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.addCheckBox.setVisibility(View.GONE);
			if (gameInfoList.get(position).isGame()) {
				viewHolder.itemView
						.setBackgroundResource(R.drawable.game_daily_match_bg);
			} else {
				viewHolder.itemView
						.setBackgroundResource(R.drawable.game_daily_tran_bg);
			}
			viewHolder.titleView.setText(gameInfoList.get(position).getTitle());
			viewHolder.locView.setText(gameInfoList.get(position).getPlace());
			viewHolder.dateView.setText(formatTime(gameInfoList.get(position)
					.getTime()));
			return convertView;
		}
	}

	public String formatTime(String time) {
		return time.substring(time.indexOf(" ") + 1);
	}

	private static class ViewHolder {
		public TextView titleView;
		public TextView locView;
		public TextView dateView;
		public CheckBox addCheckBox;
		public View itemView;
	}

}
