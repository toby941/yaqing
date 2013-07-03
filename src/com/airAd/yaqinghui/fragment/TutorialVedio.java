package com.airAd.yaqinghui.fragment;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Locale;

import com.airAd.yaqinghui.HomeActivity;
import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.PlayVideoActivity;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.WelcomeActivity;
import com.airAd.yaqinghui.common.Config;
import com.airAd.yaqinghui.common.StringUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * 
 * @author Panyi
 * 
 */
public class TutorialVedio extends Fragment {
	private ImageButton playVideo;
	public static TutorialVedio newInstance() {
		TutorialVedio fragment = new TutorialVedio();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_tutorial_pagetwo, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		playVideo = (ImageButton)getActivity().findViewById(R.id.playVideo);
		playVideo.setOnClickListener(new PlayVideoClick());
	}
	
	private final class PlayVideoClick implements OnClickListener{
		@Override
		public void onClick(View v) {
			Intent it = new Intent(getActivity(),PlayVideoActivity.class);
			getActivity().startActivity(it);
		}
		
	}//end inner class
}// end class
