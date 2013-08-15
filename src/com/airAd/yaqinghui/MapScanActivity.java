package com.airAd.yaqinghui;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase.DisplayType;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

/**
 * 
 * @author Panyi
 */
public class MapScanActivity extends BaseActivity {
	private ImageButton mBack;
	private ImageViewTouch mImage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_scan);
		init();
	}

	private void init() {
		mBack = (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBack.setOnClickListener(new BackClick());
		mImage= (ImageViewTouch) findViewById(R.id.map);
		mImage.setDisplayType(DisplayType.FIT_IF_BIGGER);
		mImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.yaqing_pic));
	}

	private final class BackClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			MapScanActivity.this.finish();
		}
	}// end inner class

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.gc();
	}
}// end class
