package com.airAd.yaqinghui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.airAd.yaqinghui.PlayVideoActivity;
import com.airAd.yaqinghui.R;

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
		playVideo= (ImageButton) getActivity().findViewById(R.id.playvideo);
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
