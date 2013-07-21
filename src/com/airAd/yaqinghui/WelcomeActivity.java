package com.airAd.yaqinghui;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.common.Config;

public class WelcomeActivity extends BaseActivity {
	public static final int LOAD_DELAY = 2500;
	private SharedPreferences sp;
	private View mLoadImage;

	private View light;
	private ImageView bottom;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		init();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Matrix matrix = bottom.getImageMatrix(); 
			matrix.postTranslate(-0.3f, 0);
			bottom.setImageMatrix(matrix); 
			bottom.setScaleType(ScaleType.MATRIX); 
			bottom.invalidate(); 
		}
	};
	private void init() {
		sp = this.getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE);
		mLoadImage = findViewById(R.id.main);
		bottom = (ImageView) findViewById(R.id.bottom);
		new Thread(new Runnable(){
			int  count=0;
			@Override
			public void run() {
				do{
					handler.sendEmptyMessage(1);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}while(count<=20);
			}
		}).start();
		light = findViewById(R.id.light);
		RotateAnimation rotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate.setRepeatCount(-1);
		rotate.setDuration(5000);
		LinearInterpolator lin = new LinearInterpolator();
		rotate.setInterpolator(lin);
		light.setAnimation(rotate);

		AlphaAnimation alpha = new AlphaAnimation(1.0f, 0);// 欢迎页变淡动画
		alpha.setStartOffset(1000);// 延时1s后播放动画
		alpha.setDuration(LOAD_DELAY);
		alpha.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mLoadImage.setVisibility(View.GONE);
				if (isFirstLogin()) {// 第一次登陆 显示引导 视频页面
					Intent it = new Intent(WelcomeActivity.this,
							TutorialActivity.class);
					it.putExtra("subNum", 3);
					WelcomeActivity.this.startActivity(it);
				} else {
					if (hasLogin()) {
						SharedPreferences sp = getSharedPreferences(
								Config.PACKAGE, Context.MODE_PRIVATE);
						MyApplication.getCurrentApp().setUser(
								User.instance(sp.getString(
										Config.USER_INFO_KEY, "")));
						WelcomeActivity.this.startActivity(new Intent(
								WelcomeActivity.this, HomeActivity.class));
					} else {// 还未登录
						Intent it = new Intent(WelcomeActivity.this,
								TutorialActivity.class);
						it.putExtra("subNum", 1);
						WelcomeActivity.this.startActivity(it);
					}
				}
				WelcomeActivity.this.finish();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
		});
		mLoadImage.setAnimation(alpha);
	}

	protected boolean hasLogin() {
		SharedPreferences sp = getSharedPreferences(Config.PACKAGE,
				Context.MODE_PRIVATE);
		if (StringUtils.isBlank(sp.getString(Config.USER_INFO_KEY, ""))) {
			return false;
		}
		return true;
	}

	/**
	 * 是否是第一次使用
	 * 
	 * @return
	 */
	private boolean isFirstLogin() {
		int times = sp.getInt(Config.USE_TIMES, -1);
		if (times < 0) {
			Editor ed = sp.edit();
			ed.putInt(Config.USE_TIMES, 1);
			ed.commit();
			return true;
		}
		return false;
	}
}// end class
