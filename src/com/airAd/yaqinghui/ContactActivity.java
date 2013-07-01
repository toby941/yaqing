package com.airAd.yaqinghui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * @author Panyi
 * 
 */
public class ContactActivity extends BaseActivity {
	private Button mLoginBtn;
	private EditText mNameText, mPwdText;
	private ImageButton mBackBtn;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact);
		init();
	}

	private void init() {
		mBackBtn = (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBackBtn.setOnClickListener(new BackClick());
	}

	private class BackClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			ContactActivity.this.finish();
		}
	}// end inner class

}// end class
