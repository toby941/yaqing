package com.airAd.yaqinghui;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.common.Config;

public class WelcomeActivity extends BaseActivity {
	public static final int LOAD_DELAY = 2500;
	private SharedPreferences sp;
	private ImageView mLoadImage;
	
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

	private void init() {
		sp = this.getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE);
		mLoadImage = (ImageView) findViewById(R.id.loadImage);
        AlphaAnimation alpha = new AlphaAnimation(1.0f, 0);// 欢迎页变淡动画
        alpha.setStartOffset(1000);// 延时1s后播放动画
		alpha.setDuration(LOAD_DELAY);
		alpha.setFillAfter(true);
		alpha.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
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
                    }
                    else {// 还未登录
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
