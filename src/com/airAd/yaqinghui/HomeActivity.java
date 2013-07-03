package com.airAd.yaqinghui;

import java.io.File;
import java.util.Calendar;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.yaqinghui.business.SigninService;
import com.airAd.yaqinghui.business.api.BasicAPI;
import com.airAd.yaqinghui.business.model.SignInCepActive;
import com.airAd.yaqinghui.common.Config;
import com.airAd.yaqinghui.common.StringUtil;
import com.airAd.yaqinghui.core.HessianClient;
import com.airAd.yaqinghui.core.ImageFetcherFactory;
import com.airAd.yaqinghui.fragment.UserDetailFragment;
import com.airAd.yaqinghui.fragment.UserFragment;
import com.airAd.yaqinghui.ui.CustomViewPager;
import com.airAd.yaqinghui.ui.IconListItem;
import com.airAd.yaqinghui.ui.PushClose;
import com.google.zxing.client.android.CaptureActivity;
import com.slidingmenu.lib.SlidingMenu;

/**
 * @author Panyi
 */
public class HomeActivity extends SlidingBaseActivity {
    private int backPressedCount = 0;
    private ImageFetcher mImageFetcher;
    private ImageButton mLeftBtn, mRightBtn;
    private ProgressDialog progressDialog;
    private SigninTask mSignTask;

    private Bitmap mThumbBitmap;
    private ChangeThumbReceiver mChangeThumbReceiver;

    private UserFragment userFragment;
    private UserDetailFragment userDetailFragment;

    private TextView dateBanner, dateText;
    private PushClose mPushClose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_Sherlock_NoActionBar);
        setContentView(R.layout.main);
        init();
    }

    @Override
    public void onPause() {
        super.onPause();
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
        if (mSignTask != null) {
            mSignTask.cancel(true);
        }

        if (mChangeThumbReceiver != null) {// 取消监听
            this.unregisterReceiver(mChangeThumbReceiver);
        }
    }

    public void init() {
        // startPushService();
        registerBroadcast();
        initSlidingMenu();
        mImageFetcher = ImageFetcherFactory.genImageFetcher(this);
        initComponent();
        setPushClose();
        getThumbBitmap();// 设置头像位图数据
    }

    /**
     * 设置时间数据
     */
    private void setPushClose() {
        mPushClose = (PushClose) this.findViewById(R.id.pushClose);
        View bottomView = LayoutInflater.from(this).inflate(R.layout.date, null);
        View topView = LayoutInflater.from(this).inflate(R.layout.dialy, null);
        mPushClose.setContent(topView, bottomView);
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        TextView bannerText = (TextView) bottomView.findViewById(R.id.home_date_banner);
        TextView dateText = (TextView) topView.findViewById(R.id.date_banner);
        bannerText.setText(StringUtil.dateOfDay(month) + "." + year);
        dateText.setText(day + " " + StringUtil.retWeekName(weekday));
    }

    private void registerBroadcast() {
        mChangeThumbReceiver = new ChangeThumbReceiver();
        this.registerReceiver(mChangeThumbReceiver, new IntentFilter(Config.CHANGE_THUMB_BROADCAST));
    }

    public Bitmap getThumbBitmap() {
        mThumbBitmap = prepareThumbImage();// 设置头像位图数据
        return mThumbBitmap;
    }

    // private void startPushService() {
    // Intent startPush = new Intent();
    // startPush.setClass(this, PushService.class);
    // this.stopService(startPush);//停止服务
    // this.startService(startPush);
    // }

    private void initComponent() {
        mLeftBtn = (ImageButton) findViewById(R.id.main_banner_left_btn);
        mRightBtn = (ImageButton) findViewById(R.id.main_banner_right_btn);
        mLeftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.this.getSlidingMenu().showMenu(true);
            }
        });
        mRightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.this.getSlidingMenu().showSecondaryMenu(true);
            }
        });
    }

    private void initSlidingMenu() {
        SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidth(30);
        sm.setBehindOffset(100);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        sm.setMode(SlidingMenu.LEFT_RIGHT);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setSecondaryShadowDrawable(R.drawable.shadowright);
        setBehindContentView(R.layout.menu_frame);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, new LeftMenuFragment()).commit();
        sm.setSecondaryMenu(R.layout.menu_frame_two);
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame_two, new RightMenuFragment()).commit();
    }

    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        backPressedCount = 0;
        super.onResume();
    }

    public ImageFetcher getImageFetcher() {
        return mImageFetcher;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && !getSlidingMenu().isMenuShowing()) {
            if (backPressedCount++ == 0)
                Toast.makeText(this, R.string.pro_exit_cofirm, Toast.LENGTH_LONG).show();
            else
                finish();
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public class RightMenuFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.menu_rights, null);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.menu_list);

            IconListItem iconListItemSchedule = new IconListItem(HomeActivity.this);
            iconListItemSchedule.setIcon(R.drawable.icon_schedule_bg, R.drawable.item_dialy_bg,
                    R.string.menu_left_schedule);
            iconListItemSchedule.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(HomeActivity.this, ScheduleActivity.class);
                    HomeActivity.this.startActivity(intent);
                }
            });
            layout.addView(iconListItemSchedule);

            IconListItem cep = new IconListItem(HomeActivity.this);
            cep.setIcon(R.drawable.icon_schedule_bg, R.drawable.cep_icon, R.string.cep_menu_title);
            cep.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(HomeActivity.this, CepActivity.class);
                    HomeActivity.this.startActivity(intent);
                }
            });
            layout.addView(cep);

            IconListItem video = new IconListItem(HomeActivity.this);
            video.setIcon(R.drawable.icon_schedule_bg, R.drawable.video_icon, R.string.cep_video);
            video.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            layout.addView(video);

            return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }
    }

    /**
     * 左侧菜单
     * 
     * @author Administrator
     */
    public class LeftMenuFragment extends Fragment {
        private CustomViewPager leftGalllery;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.menu_left, null);
            leftGalllery = (CustomViewPager) view.findViewById(R.id.left_menugallery);
            leftGalllery.setAdapter(new LeftMenuAdapter(getActivity().getSupportFragmentManager()));
            return view;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        private final class LeftMenuAdapter extends FragmentStatePagerAdapter {
            public LeftMenuAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public Fragment getItem(int index) {
                if (index == 0) {
                    if (userFragment == null) {
                        userFragment = UserFragment.newInstance(leftGalllery);
                    }
                    return userFragment;
                }
                else {
                    if (userDetailFragment == null) {
                        userDetailFragment = UserDetailFragment.newInstance(leftGalllery, mImageFetcher);
                    }
                    return userDetailFragment;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

        }// end inner class
    }

    // 二维码扫描返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UserFragment.SCAN_QRCODE) {
                String twobarcode = data.getStringExtra(CaptureActivity.FLAG);
                Log.e("yq", twobarcode);
                // System.out.println(twobarcode);
                requestSign(twobarcode, Config.CEP_USER_ID, Config.LNG, Config.LAT);
            }
        }
    }

    /**
     * 接到广播 改变头像
     * 
     * @author Administrator
     */
    private final class ChangeThumbReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (userFragment != null) {
                userFragment.getThumb().setImageBitmap(prepareThumbImage());
            }
            if (userDetailFragment != null) {
                userDetailFragment.getThumb().setImageBitmap(prepareThumbImage());
            }
        }
    }

    private void requestSign(String twobarcode, String userid, String lng, String lat) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(HomeActivity.this);
            progressDialog.setTitle(R.string.dialog_title);
            progressDialog.setMessage(getResources().getText(R.string.dialog_msg));
            progressDialog.setCanceledOnTouchOutside(true);
            progressDialog.setCancelable(true);
        }
        progressDialog.show();
        mSignTask = new SigninTask();
        mSignTask.execute(twobarcode);
    }

    /**
     * 载入头像位图
     * 
     * @return
     */
    public Bitmap prepareThumbImage() {
        SharedPreferences sp = getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE);
        String thumbPath = sp.getString(Config.THUMB_PATH, "");
        if (StringUtils.isBlank(thumbPath)) {// 载入默认头像
            return BitmapFactory.decodeResource(getResources(), R.drawable.icon);
        }
        else {// 载入自定义头像
            File thumbFile = new File(thumbPath);
            if (!thumbFile.exists()) {// 若文件不存在 载入默认头像
                return BitmapFactory.decodeResource(getResources(), R.drawable.icon);
            }
            try {
                return loadBitmap(thumbPath);
            }
            catch (Exception e) {
                e.printStackTrace();
                return BitmapFactory.decodeResource(getResources(), R.drawable.icon);
            }
        }
    }

    /**
     * 根据指定路径载入压缩后的位图数据
     * 
     * @param path
     * @return
     * @throws Exception
     */
    private Bitmap loadBitmap(String path) throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回bm为空
        // 计算缩放比
        int be = (int) (options.outHeight / (float) 300);
        int ys = options.outHeight % 300;// 求余数
        float fe = ys / (float) 300;
        if (fe >= 0.5)
            be = be + 1;
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        // 重新读入图片，options.inJustDecodeBounds 设为 false
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    private final class SigninTask extends AsyncTask<String, Void, SignInCepActive> {
        @Override
        protected SignInCepActive doInBackground(String... params) {
            BasicAPI basicAPI = HessianClient.create();
            JSONObject jsonObj = null;
            try {
                // jsonObj = basicAPI.SignInCepActive(params[0],
                // Config.CEP_USER_ID, Config.LNG, Config.LAT);
            }
            catch (Exception e) {
                jsonObj = null;
                e.printStackTrace();
            }
            return new SigninService().getSignup(jsonObj);
        }

        @Override
        protected void onPostExecute(SignInCepActive result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result == null) {
                Toast.makeText(HomeActivity.this, R.string.net_exception, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(HomeActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
        }
    }// end inner class
}// end class
