package com.airAd.yaqinghui;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.airAd.yaqinghui.business.model.Cep;
/**
 * CEP活动
 * 
 * @author Panyi
 */
public class ShakeActivity extends BaseActivity
{
	private ImageButton mBack;
	private List<Cep> cepList;

	private ImageView mMonkey;
	private LinearLayout item;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shakecep);
		init();
	}
	private void init()
	{
		mBack= (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBack.setOnClickListener(new BackClick());
		//		cepList= (List<Cep>) MyApplication.getCurrentApp().pop();
		mMonkey= (ImageView) findViewById(R.id.monkey);
		item= (LinearLayout) findViewById(R.id.item);
		item.setVisibility(View.INVISIBLE);
		mMonkey.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				shake();
			}
		});
	}

	private void shake()
	{
		//		Animation am= new RotateAnimation(0, 360);
		//		am.setDuration(2000);
		//		am.setRepeatCount(10);
		//		mMonkey.startAnimation(am);
		item.setVisibility(View.GONE);
		int pivot= Animation.RELATIVE_TO_SELF;
		CycleInterpolator interpolator= new CycleInterpolator(2f);
		RotateAnimation animation= new RotateAnimation(0, 25, pivot, 0f, pivot, 0f);
		animation.setDuration(2500);
		animation.setInterpolator(interpolator);
		animation.setAnimationListener(new AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation animation)
			{
			}
			@Override
			public void onAnimationRepeat(Animation animation)
			{
			}
			@Override
			public void onAnimationEnd(Animation animation)
			{
				showItem();
			}
		});
		mMonkey.startAnimation(animation);
	}

	private void showItem()
	{
		//		TranslateAnimation animation= new TranslateAnimation(0, 25, pivot, 0f, pivot, 0f);
		TranslateAnimation downAnimation= new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -2, Animation.RELATIVE_TO_SELF, 0);
		downAnimation.setDuration(2000);
		item.setVisibility(View.VISIBLE);
		item.setAnimation(downAnimation);
	}


	private final class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			ShakeActivity.this.finish();
		}
	}// end inner class
}// end class
