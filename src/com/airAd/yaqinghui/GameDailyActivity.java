package com.airAd.yaqinghui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.airAd.yaqinghui.business.GameService;
import com.airAd.yaqinghui.business.model.GameInfo;
import com.airAd.yaqinghui.common.StringUtil;
import com.airAd.yaqinghui.ui.CanCloseListView;
import com.airAd.yaqinghui.ui.PushClose;

/**
 * @author Panyi
 */
public class GameDailyActivity extends BaseActivity {

	private PushClose mPushClose;
	private CanCloseListView listView;
	private GameService gameService;
	private List<GameInfo> gameInfoList = new ArrayList<GameInfo>();
	private String gameId;
	private DailyAdapter dailyAdapter;
	
	public static final String GAME_ID = "game_id";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_daily);
		gameId = getIntent().getStringExtra(GAME_ID);
		init();
	}

	public void init() {
		gameService = new GameService();
		new GameDailyTask().execute(gameId);
		setPushClose();
		dailyAdapter = new DailyAdapter();
		listView.setAdapter(dailyAdapter);
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

	private class DailyAdapter extends BaseAdapter
	{

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
			if(convertView == null)
			{
				convertView = getLayoutInflater().inflate(R.layout.game_daily_item, null);
			}
			if(convertView.getTag() == null)
			{
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.titleView = (TextView)convertView.findViewById(R.id.game_title);
				viewHolder.locView = (TextView)convertView.findViewById(R.id.game_loc);
				convertView.setTag(viewHolder);
			}
			ViewHolder viewHolder = (ViewHolder)convertView.getTag();
			viewHolder.titleView.setText(gameInfoList.get(position).getTitle());
			viewHolder.locView.setText(gameInfoList.get(position).getPlace());
			return convertView;
		}
	}
	
	private static class ViewHolder
	{
		public TextView titleView;
		public TextView locView;
	}
	
	private class GameDailyTask extends AsyncTask<String, Void, List<GameInfo>> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<GameInfo> doInBackground(String... params) {
			String gameId = params[0];
			Calendar c = Calendar.getInstance();
			c.set(2013, 7, 12);
			return gameService.getGameInfo(gameId, c.getTime());
		}

		@Override
		protected void onPostExecute(List<GameInfo> list) {
			gameInfoList = list;
			dailyAdapter.notifyDataSetChanged();
		}
	}
}
