package com.airAd.yaqinghui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

/**
 * 提交评论 对话框
 * 
 * @author Panyi
 * 
 */
public class CallDialogActivity extends Activity {
	private ImageButton mClose;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.call);
		init();
	}

	private void init() {
		mClose = (ImageButton) findViewById(R.id.dialog_commit_close_btn);
		mClose.setOnClickListener(new CloseClick());
	}

	private class CloseClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			CallDialogActivity.this.finish();
		}
	}// end inner class
}// end class
