package com.airAd.yaqinghui.ui;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.airAd.yaqinghui.CepDetailActivity;
import com.airAd.yaqinghui.HomeActivity;
import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.business.AlarmService;
import com.airAd.yaqinghui.business.ScheduleService;
import com.airAd.yaqinghui.business.model.Game;
import com.airAd.yaqinghui.business.model.ScheduleItem;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.common.Common;
import com.airAd.yaqinghui.common.Config;
import com.airAd.yaqinghui.fragment.UserFragment;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.google.zxing.client.android.CaptureActivity;

public class PushClose extends RelativeLayout {
	public static final int UNSELECTED_COLOR = Color.rgb(254, 102, 97);
	public static final int SELECTED_COLOR = Color.rgb(50, 63, 78);
	public static final int TEXT_NORMAL_COLOR = Color.rgb(177, 179, 184);
	public static final int DAYS = 11;
	public boolean isClosed = true;
	private LinearLayout mBottomView;
	private LinearLayout mTopView;
	private Scroller mScroller;
	private CanCloseListView mListView;
	private Context mContext;
	private boolean mIsBeingDragged = false;
	private float mLastMotionX;
	private float mLastMotionY;
	private Map<Integer, Integer> allDays;
	private ScheduleService mScheduleService;
	DateTextView date1;
	private DateTextView[] dates = new DateTextView[DAYS];
	private DateClick dateClick;
	private DateTextView selectedDate;
	private ListView mSheduleList;
	private List<ScheduleItem> mDataList;
	private ScheduleItemAdapter scheduleAdapter;
	private HomeActivity parentActivity;
	private LocationManagerProxy locationManager;
	private boolean isLocating = false;
	private ProgressDialog mProgressDialog;
	private OnDateClickListener onDateClickListener;
	private TextView bannerText;
	private ImageView emptyBox;
	private TextView emptyTitle;
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
				// System.out.println("location2--->" + geoLat + "," + geoLng);
				locationManager.removeUpdates(this);
				parentActivity.lat = geoLat + "";
				parentActivity.lng = geoLng + "";
				Intent it = new Intent(parentActivity, CaptureActivity.class);
				parentActivity.startActivityForResult(it,
						UserFragment.SCAN_QRCODE);
				isLocating = false;
				if (mProgressDialog.isShowing())
					mProgressDialog.dismiss();
			}
		}
	};

	public PushClose(Context context) {
		super(context);
		init(context);
	}

	public PushClose(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void setDateDay(int day) {
		int selected = day - 13;
		if (selected < 0 || selected > DAYS) {
			return;
		}
		if (selectedDate != null) {
			selectedDate.setTextColor(UNSELECTED_COLOR);
			selectedDate.setBackgroundColor(SELECTED_COLOR);
		}
		dates[selected].setTextColor(SELECTED_COLOR);
		dates[selected].setBackgroundColor(UNSELECTED_COLOR);
		selectedDate = dates[selected];
	}

	private void init(Context context) {
		mContext = context;
		if (mContext instanceof HomeActivity) {
			parentActivity = (HomeActivity) mContext;
		}
		locationManager = LocationManagerProxy.getInstance(mContext);
		mScroller = new Scroller(getContext());
		mScheduleService = new ScheduleService();
	}

	public void close() {
		smoothScrollTo(0, 0);
		isClosed = true;
		mListView.isHead = false;
	}

	public void open() {
		int bottomHeight = mBottomView.getChildAt(0).getHeight();
		smoothScrollTo(0, -bottomHeight);
		isClosed = false;
		mListView.isHead = true;
	}

	public void setContent(View top, View bottom) {
		mBottomView = (LinearLayout) findViewById(R.id.bottom);
		mTopView = (LinearLayout) findViewById(R.id.top);
		bannerText = (TextView) top.findViewById(R.id.date_banner);
		emptyBox = (ImageView) top.findViewById(R.id.empty_box);
		emptyTitle = (TextView) top.findViewById(R.id.empty_text);
		mBottomView.addView(bottom);
		mTopView.addView(top);
		mListView = (CanCloseListView) mTopView.findViewById(R.id.date_list);
		mSheduleList = (ListView) top.findViewById(R.id.date_list);
		mSheduleList.setDivider(new BitmapDrawable());
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setTitle(R.string.dialog_title);
		mProgressDialog
				.setMessage(getResources().getText(R.string.is_locating));
		mProgressDialog.setCancelable(true);
		addDateListener();
	}

	private void addDateListener() {
		dateClick = new DateClick();
		dates[0] = (DateTextView) mBottomView.findViewById(R.id.date_day1);
		dates[1] = (DateTextView) mBottomView.findViewById(R.id.date_day2);
		dates[2] = (DateTextView) mBottomView.findViewById(R.id.date_day3);
		dates[3] = (DateTextView) mBottomView.findViewById(R.id.date_day4);
		dates[4] = (DateTextView) mBottomView.findViewById(R.id.date_day5);
		dates[5] = (DateTextView) mBottomView.findViewById(R.id.date_day6);
		dates[6] = (DateTextView) mBottomView.findViewById(R.id.date_day7);
		dates[7] = (DateTextView) mBottomView.findViewById(R.id.date_day8);
		dates[8] = (DateTextView) mBottomView.findViewById(R.id.date_day9);
		dates[9] = (DateTextView) mBottomView.findViewById(R.id.date_day10);
		dates[10] = (DateTextView) mBottomView.findViewById(R.id.date_day11);
		for (int i = 0; i < dates.length; i++) {
			dates[i].setOnClickListener(dateClick);
		}// end for i
	}

	/**
	 * 设置日期栏该天有无事项
	 * 
	 * @param params
	 */
	public void setDateHaveItems(Map<Integer, Integer> params) {
		for (int i = 0; i < dates.length; i++) {
			dates[i].setNoneActivity();
		}// end for
		if (params == null)
			return;
		for (int i = 0; i < dates.length; i++) {
			int index = i + 13;
			if (params.get(index) != null) {
				int num = params.get(index);
				if (num > 0) {
					dates[i].setHasActivity();
				}
			}
		}// end for i
	}

	public void setSheduleListData(int day) {
		Context obj = mContext;
		if (obj instanceof HomeActivity) {
			((HomeActivity) mContext).scheduleDay = day;
		}
		User user = MyApplication.getCurrentApp().getUser();
		mScheduleService = new ScheduleService();
		if (user != null) {
			allDays = mScheduleService.getCalendlarScheduleData(user.getId());
			mDataList = mScheduleService.getScheduleItemsByDate(user.getId(),
					day);
			if (mDataList.size() > 0) {
				ScheduleItem item = new ScheduleItem();
				item.setShowMonkey(1);
				mDataList.add(item);
				emptyBox.setVisibility(View.INVISIBLE);
				emptyTitle.setVisibility(View.INVISIBLE);
			} else {
				emptyBox.setVisibility(View.VISIBLE);
				emptyTitle.setVisibility(View.VISIBLE);
			}
		}
		setDateHaveItems(allDays);
		if (scheduleAdapter == null) {
			scheduleAdapter = new ScheduleItemAdapter();
			mSheduleList.setAdapter(scheduleAdapter);
			return;
		}
		scheduleAdapter.notifyDataSetChanged();
		if (day > 0) {
			bannerText.setText(Common.genBannerText(day));
		}
	}

	public void setSelectedDay(int day) {
		int index = day - DAYS;
		if (index >= 0 && index <= dates.length) {
			dates[index].setTextColor(SELECTED_COLOR);
			dates[index].setBackgroundColor(UNSELECTED_COLOR);
			selectedDate = dates[index];
		}
	}

	private final class ScheduleItemAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private String itemRemind;
		private AssetManager assertManager;
		private View cancelSchdule;

		public ScheduleItemAdapter() {
			mInflater = LayoutInflater.from(mContext);
			itemRemind = mContext.getString(R.string.schedule_item_tips,
					AlarmService.getTimeBefore());
			assertManager = mContext.getAssets();
		}

		@Override
		public int getCount() {
			return mDataList.size();
		}

		@Override
		public Object getItem(int index) {
			return mDataList.get(index);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ScheduleItem data = mDataList.get(position);
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.schedule_item, null);
			}
			ImageView monkey = (ImageView) convertView
					.findViewById(R.id.schdule_bottom);
			View main = convertView.findViewById(R.id.item_main);
			main.setVisibility(View.VISIBLE);
			monkey.setVisibility(View.GONE);
			if (data.getShowMonkey() == 1) {
				main.setVisibility(View.GONE);
				monkey.setVisibility(View.VISIBLE);
				return convertView;
			}
			try {
				View gotos = convertView.findViewById(R.id.gotos);
				View cepZone = convertView.findViewById(R.id.schedule_item_cep);
				TextView title = (TextView) convertView
						.findViewById(R.id.title);
				TextView place = (TextView) convertView
						.findViewById(R.id.place);
				TextView tips = (TextView) convertView.findViewById(R.id.tips);
				TextView timeText = (TextView) convertView
						.findViewById(R.id.schedule_item_time);
				View mainInfo = convertView.findViewById(R.id.maininfo);
				View signBtn = convertView.findViewById(R.id.signin);// 签到按钮
				mainInfo.setOnClickListener(null);
				signBtn.setOnClickListener(null);
				gotos.setVisibility(View.GONE);
				cepZone.setVisibility(View.GONE);
				timeText.setText(Common.timeString(data.getStartTimel() + ""));
				if (AlarmService.isEventAlarmOpen()) {
					tips.setText(itemRemind);
					tips.setVisibility(VISIBLE);
				} else {
					tips.setVisibility(GONE);
				}
				title.setText(data.getTitle());
				place.setText(data.getPlace());
				View banner = convertView.findViewById(R.id.banner);
				cancelSchdule = convertView.findViewById(R.id.cancelSchduleBtn);
				cancelSchdule.setOnClickListener(new CancelSchdule(data));
				ImageView iconImage = (ImageView) convertView
						.findViewById(R.id.icon);
				if (ScheduleItem.TYPE_GAME == data.getItemType()) {
					banner.setBackgroundColor(Color.parseColor(mContext
							.getString(R.color.schedule_game)));
					iconImage.setImageResource(Game.getResourceId(data
							.getIconType()));
				} else if (ScheduleItem.TYPE_CEP_EVENT == data.getItemType()) {
					banner.setBackgroundColor(Color.parseColor(mContext
							.getString(R.color.schedule_cep)));
					gotos.setVisibility(View.VISIBLE);
					cepZone.setVisibility(View.VISIBLE);
					iconImage.setImageBitmap(BitmapFactory
							.decodeStream(assertManager.open(data.getIconType()
									+ ".png")));
					mainInfo.setOnClickListener(new GotoCep(data.getCepId(),
							data.getRefId()));
					signBtn.setOnClickListener(new ScanClick());
				} else if (ScheduleItem.TYPE_TRAINING == data.getItemType()) {
					banner.setBackgroundColor(Color.parseColor(mContext
							.getString(R.color.schedule_training)));
					iconImage.setImageResource(Game.getResourceId(data
							.getIconType()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}// end inner class

	private final class CancelSchdule implements OnClickListener {
		private ScheduleItem item;

		public CancelSchdule(ScheduleItem item) {
			this.item = item;
		}

		@Override
		public void onClick(View view) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle(R.string.delete_schdule);
			builder.setPositiveButton(R.string.confirm,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							mScheduleService.doDelScheduleItem(
									"" + item.getUserId(), item.getCid());
							AlarmService.getInstance().removeAlarm(
									Integer.parseInt(item.getCid() + ""));
							Context obj = mContext;
							if (obj instanceof HomeActivity) {
								setSheduleListData(((HomeActivity) mContext).scheduleDay);
								close();
							}
						}
					});
			builder.setNegativeButton(R.string.cancel, null);
			builder.create().show();
		}
	}// end inner class

	private final class ScanClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (isLocating) {// 当前正在定位 按钮不响应
				return;
			}
			if (openGPSSettings()) {// GPS确保打开
									// locationManager.removeUpdates(locationListener);
									// locationManager.setGpsEnable(true);
									// locationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork,
									// 5000, 10, locationListener);
									// isLocating= true;
									// mProgressDialog.show();
				Intent it = new Intent(parentActivity, CaptureActivity.class);
				parentActivity.startActivityForResult(it,
						UserFragment.SCAN_QRCODE);
			}
		}
	}// end inner class

	private boolean openGPSSettings() {
		LocationManager alm = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			return true;
		} else {
			Toast.makeText(mContext, R.string.opengps, Toast.LENGTH_SHORT)
					.show();
			mContext.startActivity(new Intent(
					Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			return false;
		}
	}

	private final class GotoCep implements OnClickListener {
		private String cepId;
		private String eventId;

		public GotoCep(String cepId, String eventId) {
			this.cepId = cepId;
			this.eventId = eventId;
		}

		@Override
		public void onClick(View v) {
			Intent it = new Intent(mContext, CepDetailActivity.class);
			it.putExtra(Config.CEP_ID, cepId);
			it.putExtra(Config.CEP_EVENT_ID, eventId);
			mContext.startActivity(it);
		}
	}// end innner class

	/**
	 * 点击日期事件响应
	 * 
	 * @author Administrator
	 * 
	 */
	private final class DateClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (isClosed) {
				return;
			}
			if (selectedDate != null) {
				selectedDate.setTextColor(TEXT_NORMAL_COLOR);
				selectedDate.setBackgroundColor(SELECTED_COLOR);
			}
			DateTextView dateText = (DateTextView) v;
			dateText.setTextColor(SELECTED_COLOR);
			dateText.setBackgroundColor(UNSELECTED_COLOR);
			selectedDate = dateText;
			int day = Integer.parseInt(selectedDate.getText().toString());
			close();
			if (onDateClickListener != null) {
				Calendar date = Calendar.getInstance();
				date.set(Calendar.YEAR, 2013);
				date.set(Calendar.MONTH, Calendar.AUGUST);
				date.set(Calendar.DAY_OF_MONTH, day);
				onDateClickListener.onDateClick(date);
			} else {
				setSheduleListData(day);
			}
			// if(dateText.isHaveActivity){
			// dateText.setNoneActivity();
			// }else{
			// dateText.setHasActivity();
			// }
		}
	}// end inner class

	public Calendar getSelectDateInt() {
		Calendar calender = Calendar.getInstance();
		if (selectedDate == null) {
			return calender;
		}
		calender.set(Calendar.DAY_OF_MONTH,
				Integer.parseInt(selectedDate.getText().toString()));
		return calender;
	}

	@Override
	public void computeScroll() {
		if (!mScroller.isFinished()) {
			if (mScroller.computeScrollOffset()) {
				mTopView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
				postInvalidate();
			}
		}
	}

	public void smoothScrollBy(int dx, int dy) {
		mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx,
				dy);
		invalidate();
	}

	public void smoothScrollTo(int fx, int fy) {
		int dx = fx - mScroller.getFinalX();
		int dy = fy - mScroller.getFinalY();
		smoothScrollBy(dx, dy);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			int dy = (int) (mLastMotionY - y);
			if (mScroller.getCurrY() + dy > 0) {
				smoothScrollBy(0, 0);
			} else if (mScroller.getCurrY() + dy < -mBottomView.getChildAt(0)
					.getHeight()) {
				smoothScrollTo(0, -mBottomView.getChildAt(0).getHeight());
			} else {
				smoothScrollBy(0, dy);
			}
			mLastMotionX = x;
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_UP:
			int bottomHeight = mBottomView.getChildAt(0).getHeight();
			if (mTopView.getScrollY() > -bottomHeight / 2) {
				smoothScrollTo(0, 0);
				isClosed = true;
				mListView.isHead = false;
			} else {
				smoothScrollTo(0, -bottomHeight);
				isClosed = false;
				mListView.isHead = true;
			}
			break;
		}
		return true;
	}

	public OnDateClickListener getOnDateClickListener() {
		return onDateClickListener;
	}

	public void setOnDateClickListener(OnDateClickListener onDateClickListener) {
		this.onDateClickListener = onDateClickListener;
	}

	// 切换时间的监听器
	public static interface OnDateClickListener {
		public void onDateClick(Calendar calendar);
	}

	public Calendar getFirstDate() {
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, 2013);
		date.set(Calendar.MONTH, Calendar.AUGUST);
		date.set(Calendar.DAY_OF_MONTH,
				Integer.parseInt(dates[0].getText().toString()));
		return date;
	}
}// end class
