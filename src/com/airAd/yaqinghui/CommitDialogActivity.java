package com.airAd.yaqinghui;

import net.sf.json.JSONObject;

import com.airAd.yaqinghui.api.BasicAPI;
import com.airAd.yaqinghui.data.model.CommentMark;
import com.airAd.yaqinghui.factory.HessianClient;
import com.airAd.yaqinghui.net.CommentService;
import com.airAd.yaqinghui.net.MyActiveService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * 
 * @author Panyi
 * 
 */
public class CommitDialogActivity extends BaseActivity {
	private ImageButton mBackBtn;
	private RadioGroup radioBtn;
	private Button mSendBtn;
	private String cepId;
	private CommentTask mTask;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commit);
		init();
	}

	private void init() {
		cepId = getIntent().getStringExtra(Config.CEP_ID);
		mBackBtn = (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBackBtn.setOnClickListener(new BackClick());
		radioBtn = (RadioGroup) findViewById(R.id.radioCommentGroup);
		mSendBtn = (Button) findViewById(R.id.sendCommentBtn);
		mSendBtn.setOnClickListener(new SendClick());
	}

	private final class SendClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			if(radioBtn.getCheckedRadioButtonId()==-1){
				return;
			}
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(CommitDialogActivity.this);
			}
			progressDialog.setTitle(R.string.dialog_title);
			progressDialog.setMessage(getResources().getText(
					R.string.dialog_msg));
			progressDialog.setCancelable(false);
			progressDialog.show();

			String content = ((RadioButton) findViewById(radioBtn
					.getCheckedRadioButtonId())).getText().toString();
			mTask = new CommentTask();
			mTask.execute(content);
		}
	}// end inner class

	private class BackClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			CommitDialogActivity.this.finish();
		}
	}// end inner class

	private final class CommentTask extends
			AsyncTask<String, Void, CommentMark> {
		@Override
		protected CommentMark doInBackground(String... params) {
			BasicAPI basicAPI = HessianClient.create();
			JSONObject jsonObj=null;;
			try{
//				jsonObj = basicAPI.CepActiveComment(cepId,
//						Config.CEP_USER_ID, params[0]);
			}catch(Exception e){
				jsonObj = null;
				e.printStackTrace();
			}
			return new CommentService().getCommentMark(jsonObj);
		}

		@Override
		protected void onPostExecute(CommentMark result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result == null) {
				Toast.makeText(CommitDialogActivity.this,
						R.string.net_exception, Toast.LENGTH_SHORT).show();
				return;
			}
			
			Toast.makeText(CommitDialogActivity.this, result.getText(),
					Toast.LENGTH_SHORT).show();
		}
	}// end class

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mTask != null) {
			mTask.cancel(true);
		}
	}
}// end class
