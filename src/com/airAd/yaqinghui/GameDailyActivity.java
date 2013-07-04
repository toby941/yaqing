package com.airAd.yaqinghui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.airAd.yaqinghui.business.ScheduleService;
import com.airAd.yaqinghui.business.model.ScheduleItem;
import com.airAd.yaqinghui.common.StringUtil;
import com.airAd.yaqinghui.ui.CanCloseListView;
import com.airAd.yaqinghui.ui.PushClose;

/**
 * @author Panyi
 */
public class GameDailyActivity extends BaseActivity {

	private PushClose mPushClose;
	private CanCloseListView listView;
	private ScheduleService scheduleService;
	private List<ScheduleItem> itemList = new ArrayList<ScheduleItem>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_daily);
		init();
	}

	public void init() {
		scheduleService = new ScheduleService();
		setPushClose();
	}

	/**
	 * 设置时间数据
	 */
	private void setPushClose() {
		mPushClose = (PushClose) this.findViewById(R.id.pushClose);
		View bottomView = LayoutInflater.from(this)
				.inflate(R.layout.date, null);
		View topView = LayoutInflater.from(this).inflate(R.layout.dialy, null);
		listView = (CanCloseListView)topView.findViewById(R.id.date_list);
		mPushClose.setContent(topView, bottomView);
		final Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		TextView bannerText = (TextView) bottomView
				.findViewById(R.id.home_date_banner);
		TextView dateText = (TextView) topView.findViewById(R.id.date_banner);
		bannerText.setText(StringUtil.dateOfDay(month) + "." + year);
		dateText.setText(day + " " + StringUtil.retWeekName(weekday));
	}

	private class ListAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			return itemList.size();
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
			return null;
		}
		
	}
	
}
