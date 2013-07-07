package com.airAd.yaqinghui;

import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.yaqinghui.business.GameService;
import com.airAd.yaqinghui.business.model.Game;
import com.airAd.yaqinghui.core.ImageFetcherFactory;
import com.airAd.yaqinghui.ui.EventView;

/**
 * 赛事日程主界面
 * 
 * @author pengf
 */
public class GameScheduleActivity extends BaseActivity {
	private LinearLayout mainLayout;
	private GameService gameService;
	private ImageFetcher mImageFetcher;
	private List<Game> myGameList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.race_schedule);
		mainLayout = findViewBy(R.id.mainLayout);
		//int myLastPos = addMyProjects();
		//addEvents(myLastPos + 1, 10);
		gameService = new GameService();
		mImageFetcher = ImageFetcherFactory.genImageFetcher(this);
		new GameTask().execute();
	}

	/**
	 * 添加我参与的项目
	 */
	private int addMyProjects() {
		findViewBy(R.id.myProjectTitle).setVisibility(View.VISIBLE);
		return addEvents(1);
	}

	public int addEvents(int startPos) {
		LinearLayout rowLayout = null;
		int row = 0;
		for (int i = 0; i < myGameList.size(); i++) {
			if (i % 4 == 0) {
				if (rowLayout != null) {
					mainLayout.addView(rowLayout, startPos + row - 1);
				}
				rowLayout = new LinearLayout(this);
				rowLayout.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				rowLayout.setOrientation(LinearLayout.HORIZONTAL);
				row++;
			}
			EventView eventView = new EventView(this, myGameList.get(i).getPic(),
					myGameList.get(i).getName());
			final String gameId = myGameList.get(i).getId();
			eventView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					openDaily(gameId);
				}
			});
			LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
			lp.weight = 1;
			eventView.setLayoutParams(lp);
			rowLayout.addView(eventView);
			eventView.load(mImageFetcher);
		}
		if (rowLayout != null && rowLayout.getChildCount() < 4) {
			View spaceView = new View(this);
			LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
			lp.weight = 4 - rowLayout.getChildCount();
			spaceView.setLayoutParams(lp);
			rowLayout.addView(spaceView);
			Log.i("schedule", lp.weight + "");
		}
		mainLayout.addView(rowLayout, startPos + row - 1);
		return startPos + row;
	}

	private void openDaily(String id) {
		Intent intent  = new Intent(this, GameDailyActivity.class);
		intent.putExtra(GameDailyActivity.GAME_ID, id);
		startActivity(intent);
	}

	private class GameTask extends AsyncTask<Void, Void, List<Game>> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected List<Game> doInBackground(Void... params) {
			return gameService.getGame();
		}

		@Override
		protected void onPostExecute(List<Game> result) {
			myGameList = result;
			for (Game game : result) {
				Log.i("schedule", game.toString());
			}
			addMyProjects();
		}
	}
}// end class
