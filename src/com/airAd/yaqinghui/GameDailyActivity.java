package com.airAd.yaqinghui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airAd.yaqinghui.business.AlarmService;
import com.airAd.yaqinghui.business.GameService;
import com.airAd.yaqinghui.business.GameService.GameId;
import com.airAd.yaqinghui.business.model.GameInfo;
import com.airAd.yaqinghui.common.Common;
import com.airAd.yaqinghui.ui.BackBaseActivity;
import com.airAd.yaqinghui.ui.CanCloseListView;
import com.airAd.yaqinghui.ui.PushClose;
import com.airAd.yaqinghui.ui.PushClose.OnDateClickListener;

/**
 * @author Panyi
 */
public class GameDailyActivity extends BackBaseActivity {

	private PushClose mPushClose;
	private CanCloseListView listView;
	private ProgressBar progressbar;
	private GameService gameService;
	private List<GameInfo> gameInfoList = new ArrayList<GameInfo>();
	private DailyAdapter dailyAdapter;
	// private OnClickListener addScheduleListener;
	private OnCheckedChangeListener scheduleChangeListener;
	private List<GameId> storedInfoIdList;// 已经持久化的gameInfolist

	private GameDailyTask task;
	private String gameId;
	private String gamePicUrl;
	private String days;
	private int checkboxWidth, checkboxHeight;
	private TextView bannerText;

	public static final String GAME_ID = "game_id";
	public static final String GAME_PIC_URL = "game_pic_url";
	public static final String GAME_DAYS = "game_days";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_daily);
		gameId = getIntent().getStringExtra(GAME_ID);
		gamePicUrl = getIntent().getStringExtra(GAME_PIC_URL);
		days = getIntent().getStringExtra(GAME_DAYS);
		//days = "13,19,20";
		Drawable checkboxDrawable = getResources().getDrawable(
				R.drawable.game_daily_delete);
		checkboxWidth = checkboxDrawable.getIntrinsicWidth();
		checkboxHeight = checkboxDrawable.getIntrinsicHeight();
		init();
	}

	public void init() {
		gameService = new GameService();
		setPushClose();
		dailyAdapter = new DailyAdapter();
		listView.setAdapter(dailyAdapter);
		// doDailyTask(mPushClose.getFirstDate());
		new InitGameDailyTask().execute();
		storedInfoIdList = gameService.queryScheduleIdAndStarttime();
		Log.i("storedInfoIdList", storedInfoIdList.toString());
		scheduleChangeListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				int pos = (Integer) buttonView.getTag();
				GameInfo gameInfo = gameInfoList.get(pos);
				GameId gameId = GameId.createBy(gameInfo.getId(), gameInfo.getStartTime());
				if (isChecked) {
					storedInfoIdList.add(gameId);
					int cid = gameService.addtoSchedule(gameInfo,
							gamePicUrl);
					AlarmService.getInstance().addAlarm(cid,
							gameInfo.getStartTime(), gameInfo.getTitle());
				} else {
					
					storedInfoIdList.remove(gameId);
					gameService.deleteFromSchedule(gameId);
					AlarmService.getInstance().removeAlarm(
							Integer.parseInt(gameInfo.getId()));
				}

			}
		};
		setDays();
		mPushClose.setOnDateClickListener(new OnDateClickListener() {

			@Override
			public void onDateClick(Calendar calendar) {
				doDailyTask(calendar);
				bannerText.setText(Common.genBannerText(calendar
						.get(Calendar.DAY_OF_MONTH)));
			}
		});
		mPushClose.post(new Runnable()
		{
			@Override
			public void run()
			{
				mPushClose.open();
			}
		});
	}
	
	private void setDays()
	{
		if(days != null && !"".equals(days))
		{
			Map<Integer, Integer> params = new HashMap<Integer, Integer>();
			for(String dayStr : days.split(","))
			{
				int day = 0;
				try {
					day = Integer.parseInt(dayStr);
					params.put(day, 1);
				} catch (NumberFormatException e) {
				}
			}
			mPushClose.setDateHaveItems(params);
		}
		
	}

	/**
	 * 设置时间数据
	 */
	private void setPushClose() {
		mPushClose = (PushClose) this.findViewById(R.id.pushClose);
		View bottomView = LayoutInflater.from(this)
				.inflate(R.layout.date, null);
		View topView = LayoutInflater.from(this).inflate(R.layout.dialy, null);
		bannerText = (TextView) topView.findViewById(R.id.date_banner);
		progressbar = (ProgressBar) topView.findViewById(R.id.progressBar);
		listView = (CanCloseListView) topView.findViewById(R.id.date_list);
		ImageView box = (ImageView) topView.findViewById(R.id.empty_box);
		TextView boxText = (TextView) topView.findViewById(R.id.empty_text);
		box.setVisibility(View.INVISIBLE);
		boxText.setVisibility(View.INVISIBLE);
		mPushClose.setContent(topView, bottomView);
		final Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		// int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		// TextView bannerText = (TextView) bottomView
		// .findViewById(R.id.home_date_banner);
		TextView dateText = (TextView) topView.findViewById(R.id.date_banner);
		dateText.setText(Common.genBannerText(day));
		// mPushClose.setSelectedDay(day);
		mPushClose.setToday();
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
			
			GameInfo gameInfo = gameInfoList.get(position);
			GameId gameId = GameId.createBy(gameInfo.getId(), gameInfo.getStartTime());
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
			if (gameInfo.isGame()) {
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
			RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) viewHolder.addCheckBox
					.getLayoutParams();
			rlp.width = checkboxWidth;
			rlp.height = checkboxHeight;
			viewHolder.addCheckBox.setLayoutParams(rlp);
			viewHolder.addCheckBox.setTag(position);
			viewHolder.addCheckBox.setOnCheckedChangeListener(null);
			if (storedInfoIdList.contains(gameId)) {
				viewHolder.addCheckBox.setChecked(true);
			} else {
				viewHolder.addCheckBox.setChecked(false);
			}
			viewHolder.addCheckBox
					.setOnCheckedChangeListener(scheduleChangeListener);
			return convertView;
		}
	}

	public void doDailyTask(Calendar calendar) {
		gameInfoList.clear();
		dailyAdapter.notifyDataSetChanged();
		if (task != null && !task.isCancelled()) {
			task.cancel(true);
		}
		task = new GameDailyTask();
		task.execute(calendar);
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

	private class GameDailyTask extends
			AsyncTask<Calendar, Void, List<GameInfo>> {
		@Override
		protected void onPreExecute() {
			progressbar.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}

		@Override
		protected List<GameInfo> doInBackground(Calendar... params) {
			Calendar c = params[0];
			return gameService.getGameInfo(gameId, c.getTime());
		}

		@Override
		protected void onPostExecute(List<GameInfo> list) {
			if (list != null) {
				gameInfoList = list;
			}
			if (gameInfoList.isEmpty()) {
				Toast.makeText(GameDailyActivity.this,
						R.string.game_detail_info_empty, Toast.LENGTH_SHORT)
						.show();
			}
			progressbar.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			dailyAdapter.notifyDataSetChanged();
		}
	}

	private static class DailyGameInfo {
		public int day;
		public List<GameInfo> list = new ArrayList<GameInfo>();
	}

	private class InitGameDailyTask extends
			AsyncTask<Void, Void, DailyGameInfo> {

		@Override
		protected void onPreExecute() {
			progressbar.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
		}

		@Override
		protected DailyGameInfo doInBackground(Void... arg0) {
			Calendar lastDay = Calendar.getInstance();
			lastDay.set(2013, Calendar.AUGUST, 24);

			Calendar firstDay = Calendar.getInstance();
			firstDay.set(2013, Calendar.AUGUST, 13);
			// 如果今天在8月13日之前，则设为8月13日
			Calendar today= Calendar.getInstance();
			if (today.before(firstDay)) {
				today = firstDay;
			}
			DailyGameInfo info= new DailyGameInfo();
			List<GameInfo> list = gameService.getGameInfo(gameId,
					today.getTime());
			info.list = list;
			info.day = today.get(Calendar.DAY_OF_MONTH);
			// 从第一天一直遍历到最后一天
			/*DailyGameInfo info= new DailyGameInfo();
			for (Calendar c = today; c.before(lastDay); c.add(
					Calendar.DAY_OF_MONTH, 1)) {
				List<GameInfo> list = gameService.getGameInfo(gameId,
						c.getTime());
				if (list != null && list.size() > 0) {
					if (list.size() > 0) {
						info.day = c.get(Calendar.DAY_OF_MONTH);
						info.list = list;
						return info;
					}
				}
				Log.i("Daily Gameinfo",
						(c.get(Calendar.MONTH) + 1) + ","
								+ c.get(Calendar.DAY_OF_MONTH) + ":"
								+ String.valueOf(list));
			}*/
			return info;
		}

		@Override
		protected void onPostExecute(DailyGameInfo info) {
			
			progressbar.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			if (info != null && info.list != null) {
				gameInfoList = info.list;
				dailyAdapter.notifyDataSetChanged();
				if (!gameInfoList.isEmpty()) {
					mPushClose.setDateDay(info.day);
				} else {
					/*Toast.makeText(GameDailyActivity.this,
							R.string.game_detail_info_empty, Toast.LENGTH_SHORT)
							.show();*/
					final Calendar calendar = Calendar.getInstance();
					int day = calendar.get(Calendar.DAY_OF_MONTH);
					mPushClose.setDateDay(day);
				}
			} else {
				/*Toast.makeText(GameDailyActivity.this,
						R.string.game_detail_info_empty, Toast.LENGTH_SHORT)
						.show();*/
			}
		}

	}

}
