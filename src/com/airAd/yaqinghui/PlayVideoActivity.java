package com.airAd.yaqinghui;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;
/**
 * 播放视频
 * @author panyi 
 *
 */
public class PlayVideoActivity extends Activity
{
	public static final String LEADER_URL= "http://www.nanjing2013.org/upload/flv/yaqing.mp4";
	public static final String URL= "http://download.airad.com/yaqing.3gp";

	private VideoView videoView;
	private ProgressBar progressBar;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.playvideo);
		progressBar= (ProgressBar) findViewById(R.id.progressbar);
		DisplayMetrics mDisplayMetrics= new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		String url= URL;
		if (Math.max(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels) > 1000)
		{//这里是领导用的机器 播放特别视频
			url= LEADER_URL;
		}
		else
		{
			url= URL;
		}

		Uri uri= Uri.parse(url);
		videoView= (VideoView) this.findViewById(R.id.video);
		videoView.setMediaController(new MediaController(this));
		videoView.setOnPreparedListener(new OnPreparedListener()
		{
			@Override
			public void onPrepared(MediaPlayer mp)
			{
				progressBar.setVisibility(View.GONE);
			}
		});
		videoView.setVideoURI(uri);
		videoView.start();
		videoView.requestFocus();
		videoView.setOnErrorListener(new OnErrorListener()
		{
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra)
			{
				progressBar.setVisibility(View.GONE);
				return false;
			}
		});
	}
}//end class
