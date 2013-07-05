package com.airAd.yaqinghui;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;
/**
 * 播放视频
 * @author panyi 
 *
 */
public class PlayVideoActivity extends Activity
{
	public static final String URL= "http://www.nanjing2013.org/upload/flv/yaqing.mp4";
	private VideoView videoView;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.playvideo);

		Uri uri= Uri.parse("http://www.nanjing2013.org/upload/flv/yaqing.mp4");
		videoView= (VideoView) this.findViewById(R.id.video);
		videoView.setMediaController(new MediaController(this));
		videoView.setVideoURI(uri);
		videoView.start();
		videoView.requestFocus();
	}
}//end class
