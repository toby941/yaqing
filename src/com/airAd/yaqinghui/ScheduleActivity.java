package com.airAd.yaqinghui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.airAd.yaqinghui.fragment.ScheduleKindFragment;
import com.airAd.yaqinghui.fragment.ScheduleListFragment;

/**
 * �����ճ�
 * 
 * @author Panyi
 */
public class ScheduleActivity extends BaseActivity {
    private ImageButton mBack, mToggleBtn;
    private boolean isPersonShow = true;

    private ViewPager mGallery;
    private ScheduleListFragment mPersonFragment;
    private ScheduleKindFragment mKindFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule);
        init();
    }

    private void init() {
        mBack = (ImageButton) findViewById(R.id.main_banner_left_btn);
        mBack.setOnClickListener(new BackClick());
        mGallery = (ViewPager) findViewById(R.id.gallery);
        mGallery.setAdapter(new PageAdapter(getSupportFragmentManager()));
        mToggleBtn = (ImageButton) findViewById(R.id.toggleBtn);
        mToggleBtn.setOnClickListener(new ToggleClick());
        mGallery.setOnPageChangeListener(new PageChange());
    }

    private final class PageAdapter extends FragmentStatePagerAdapter {
        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            switch (index) {
                case 0 :
                    return ScheduleListFragment.newInstance(null);
                case 1 :
                    return ScheduleKindFragment.newInstance(null);
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }// end inner class

    private final class PageChange implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int index) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int index) {
            switch (index) {
                case 0 :
                    mToggleBtn.setImageResource(R.drawable.schedule_btn_person);
                    break;
                case 1 :
                    mToggleBtn.setImageResource(R.drawable.schedule_btn_kind);
                    break;
            }// end switch
        }
    }// end inner class

    private final class ToggleClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            isPersonShow = !isPersonShow;
            if (isPersonShow) {// չʾ�����ճ���Ϣ
                mToggleBtn.setImageResource(R.drawable.schedule_btn_person);
                mGallery.setCurrentItem(0);
            } else {// չʾ�����ճ���Ϣ
                mToggleBtn.setImageResource(R.drawable.schedule_btn_kind);
                mGallery.setCurrentItem(1);
            }
        }
    }// end inner class

    private final class BackClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            ScheduleActivity.this.finish();
        }
    }// end inner class

}// end class
