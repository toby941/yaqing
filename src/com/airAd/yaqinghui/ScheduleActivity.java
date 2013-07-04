package com.airAd.yaqinghui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.airAd.yaqinghui.ui.EventView;

/**
 * 赛事日程主界面
 * 
 * @author pengf
 */
public class ScheduleActivity extends BaseActivity {
	private LinearLayout mainLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.race_schedule);
		mainLayout = findViewBy(R.id.mainLayout);
		int myLastPos = addMyProjects();
		addEvents(myLastPos + 1, 10);
	}

	/**
	 * 添加我参与的项目
	 */
	private int addMyProjects() {
		findViewBy(R.id.myProjectTitle).setVisibility(View.VISIBLE);
		return addEvents(1, 2);
	}

	public int addEvents(int startPos, int size) {
		LinearLayout rowLayout = null;
		int row = 0;
		for (int i = 0; i < size; i++) {
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
			EventView eventView = new EventView(this, R.drawable.run,
					R.string.project_trackfield);
			eventView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					openDaily();
				}
			});
			LayoutParams lp = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
			lp.weight = 1;
			eventView.setLayoutParams(lp);
			rowLayout.addView(eventView);
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

	private void openDaily() {
		startActivity(new Intent(this, GameDailyActivity.class));
	}
	/*
	 * private void init() { mBack = (ImageButton)
	 * findViewById(R.id.main_banner_left_btn); mBack.setOnClickListener(new
	 * BackClick()); mGallery = (ViewPager) findViewById(R.id.gallery);
	 * mGallery.setAdapter(new PageAdapter(getSupportFragmentManager()));
	 * mToggleBtn = (ImageButton) findViewById(R.id.toggleBtn);
	 * mToggleBtn.setOnClickListener(new ToggleClick());
	 * mGallery.setOnPageChangeListener(new PageChange()); }
	 * 
	 * private final class PageAdapter extends FragmentStatePagerAdapter {
	 * public PageAdapter(FragmentManager fm) { super(fm); }
	 * 
	 * @Override public Fragment getItem(int index) { switch (index) { case 0 :
	 * return ScheduleListFragment.newInstance(null); case 1 : return
	 * ScheduleKindFragment.newInstance(null); } return null; }
	 * 
	 * @Override public int getCount() { return 2; } }// end inner class
	 * 
	 * private final class PageChange implements OnPageChangeListener {
	 * 
	 * @Override public void onPageScrollStateChanged(int index) { }
	 * 
	 * @Override public void onPageScrolled(int arg0, float arg1, int arg2) { }
	 * 
	 * @Override public void onPageSelected(int index) { switch (index) { case 0
	 * : mToggleBtn.setImageResource(R.drawable.schedule_btn_person); break;
	 * case 1 : mToggleBtn.setImageResource(R.drawable.schedule_btn_kind);
	 * break; }// end switch } }// end inner class
	 * 
	 * private final class ToggleClick implements OnClickListener {
	 * 
	 * @Override public void onClick(View v) { isPersonShow = !isPersonShow; if
	 * (isPersonShow) {// չʾ�����ճ���Ϣ
	 * mToggleBtn.setImageResource(R.drawable.schedule_btn_person);
	 * mGallery.setCurrentItem(0); } else {// չʾ�����ճ���Ϣ
	 * mToggleBtn.setImageResource(R.drawable.schedule_btn_kind);
	 * mGallery.setCurrentItem(1); } } }// end inner class
	 * 
	 * private final class BackClick implements OnClickListener {
	 * 
	 * @Override public void onClick(View v) { ScheduleActivity.this.finish(); }
	 * }// end inner class
	 */
}// end class
