package com.airAd.yaqinghui;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.TextView;

import com.airAd.yaqinghui.business.model.Cep;
import com.airAd.yaqinghui.common.Common;
import com.airAd.yaqinghui.common.Config;
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
	private ShakeHandler handler;
	private Sensor acceleromererSensor;
	private SensorEventListener acceleromererListener;
	private SensorManager sm;
	private int count= -1;
	private long lastUpdate;
	private long curTime;
	private float last_x= 0.0f;
	private float last_y= 0.0f;
	private float last_z= 0.0f;
	private static final int SHAKE_THRESHOLD= 600;
	private boolean isRunning= true;
	private ImageView typeImage;
	private TextView title, content;
	private Cep cep;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shakecep);
		init();
	}
	private void init()
	{
		handler= new ShakeHandler();
		mBack= (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBack.setOnClickListener(new BackClick());
		cepList= (List<Cep>) MyApplication.getCurrentApp().pop();
		typeImage= (ImageView) findViewById(R.id.icon);
		title= (TextView) findViewById(R.id.title);
		content= (TextView) findViewById(R.id.content);

		mMonkey= (ImageView) findViewById(R.id.monkey);
		item= (LinearLayout) findViewById(R.id.item);
		item.setVisibility(View.INVISIBLE);
		//		mMonkey.setOnClickListener(new OnClickListener()
		//		{
		//			@Override
		//			public void onClick(View v)
		//			{
		//				shake();
		//			}
		//		});
		sm= (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		acceleromererSensor= sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		acceleromererListener= new SensorEventListener()
		{
			public void onAccuracyChanged(Sensor arg0, int arg1)
			{
			}
			public void onSensorChanged(SensorEvent event)
			{
				curTime= System.currentTimeMillis();
				// 每300毫秒检测一次
				if ((curTime - lastUpdate) > 300)
				{
					long diffTime= (curTime - lastUpdate);
					lastUpdate= curTime;
					float x= event.values[SensorManager.DATA_X];
					float y= event.values[SensorManager.DATA_Y];
					float z= event.values[SensorManager.DATA_Z];
					float speed= Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;
					//					System.out.println(speed);
					if (speed > SHAKE_THRESHOLD)
					{
						shakeDo(curTime);
					}
					last_x= x;
					last_y= y;
					last_z= z;
				}
			}
		};
		sm.registerListener(acceleromererListener, acceleromererSensor, SensorManager.SENSOR_DELAY_NORMAL);
		new ShakeThread().start();
	}
	private void shakeDo(long time)
	{
		count= 30;
	}
	private void shake()
	{
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
		int randomIndex= Common.genRand(0, cepList.size());
		Cep cepItem= cepList.get(randomIndex);
		cep= cepItem;
		typeImage.setImageResource(Common.getCepTypePic(cepItem.getIconType()));
		title.setText(cepItem.getTitle());
		content.setText(cepItem.getTitle());
		TranslateAnimation downAnimation= new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -2, Animation.RELATIVE_TO_SELF, 0);
		downAnimation.setDuration(2000);
		item.setVisibility(View.VISIBLE);
		item.setAnimation(downAnimation);
		item.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent it= new Intent(ShakeActivity.this, CepDetailActivity.class);
				it.putExtra(Config.CEP_ID, cep.getId());
				ShakeActivity.this.startActivity(it);
				ShakeActivity.this.finish();
			}
		});
	}

	@Override
	public void onDestroy()
	{
		isRunning= false;
		super.onDestroy();
	}
	private final class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			ShakeActivity.this.finish();
		}
	}// end inner class
	private final class ShakeThread extends Thread
	{
		@Override
		public void run()
		{
			while (isRunning)
			{
				try
				{
					count--;
					if (count == 20)
					{
						handler.sendEmptyMessage(1);
					}
					else if (count < 0)
					{
						count= 0;
					}
					Thread.sleep(20);
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}//end while
		}
	}
	private final class ShakeHandler extends Handler
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			shake();
		}
	}//end inner class
}// end class
