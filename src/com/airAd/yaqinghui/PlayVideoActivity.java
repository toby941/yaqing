package com.airAd.yaqinghui;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
/**
 * 播放视频
 * @author panyi 
 *
 */
public class PlayVideoActivity extends Activity
{
	public static final String URL= "http://114.80.184.54/youku/6974F778BFA39812EE311F25BB/030010010051D611E3D90C0634800B1BFD35E0-2E07-7E8C-9EE9-49F634268EAA.3gp";
	private VideoView videoView;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playvideo);
		Uri uri= Uri.parse(URL);
		videoView= (VideoView) this.findViewById(R.id.video);
		videoView.setMediaController(new MediaController(this));
		videoView.setVideoURI(uri);
		videoView.start();
		videoView.requestFocus();
	}
}//end class
