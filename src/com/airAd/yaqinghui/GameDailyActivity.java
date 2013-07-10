package com.airAd.yaqinghui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
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
	private ProgressBar progressbar;
	private GameService gameService;
	private List<GameInfo> gameInfoList = new ArrayList<GameInfo>();
	private DailyAdapter dailyAdapter;
	private OnClickListener addScheduleListener;
	private List<String> storedInfoIdList;// 已经持久化的gameInfolist

	private String gameId;
	private String gamePicUrl;
	public static final String GAME_ID = "game_id";
	public static final String GAME_PIC_URL = "game_pic_url";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_daily);
		gameId = getIntent().getStringExtra(GAME_ID);
		gamePicUrl = getIntent().getStringExtra(GAME_PIC_URL);
		init();
	}

	public void init() {
		gameService = new GameService();
		setPushClose();
		dailyAdapter = new DailyAdapter();
		listView.setAdapter(dailyAdapter);
		new GameDailyTask().execute(gameId);
		storedInfoIdList = gameService.queryScheduleIds();
		Log.i("storedInfoIdList", storedInfoIdList.toString());
		addScheduleListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				int pos = (Integer) v.getTag();
				gameService.addtoSchedule(gameInfoList.get(pos), gamePicUrl);
				v.setEnabled(false);
			}
		};
	}

	/**
	 * 设置时间数据
	 */
	private void setPushClose() {
		mPushClose = (PushClose) this.findViewById(R.id.pushClose);
		View bottomView = LayoutInflater.from(this)
				.inflate(R.layout.date, null);
		View topView = LayoutInflater.from(this).inflate(R.layout.dialy, null);
		progressbar = (ProgressBar) topView.findViewById(R.id.progressBar);
		listView = (CanCloseListView) topView.findViewById(R.id.date_list);
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
				viewHolder.addBtn = (Button) convertView
						.findViewById(R.id.game_add_btn);
				convertView.setTag(viewHolder);
			}
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.titleView.setText(gameInfoList.get(position).getTitle());
			viewHolder.locView.setText(gameInfoList.get(position).getPlace());
			viewHolder.dateView.setText(formatTime(gameInfoList.get(position)
					.getTime()));
			if (storedInfoIdList.contains(gameInfoList.get(position).getId())) {
				viewHolder.addBtn.setEnabled(false);
			} else {
				viewHolder.addBtn.setEnabled(true);
				viewHolder.addBtn.setTag(position);
				viewHolder.addBtn.setOnClickListener(addScheduleListener);
			}

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
		public Button addBtn;
	}

	private class GameDailyTask extends AsyncTask<String, Void, List<GameInfo>> {
		@Override
		protected void onPreExecute() {
			progressbar.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}

		@Override
		protected List<GameInfo> doInBackground(String... params) {
			String gameId = params[0];
			Calendar c = Calendar.getInstance();
			c.set(2013, 7, 12);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return gameService.getGameInfo("00001", c.getTime());
		}

		@Override
		protected void onPostExecute(List<GameInfo> list) {
			if (list != null) {
				gameInfoList = list;
			}
			progressbar.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			dailyAdapter.notifyDataSetChanged();
		}
	}
}
