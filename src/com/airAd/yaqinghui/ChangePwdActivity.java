package com.airAd.yaqinghui;
import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.airAd.yaqinghui.business.AccountService;
import com.airAd.yaqinghui.business.api.vo.response.ChangePasswordResponse;
import com.airAd.yaqinghui.common.Config;
import com.airAd.yaqinghui.common.Constants;
/**
 * @author Panyi
 * 
 */
public class ChangePwdActivity extends BaseActivity
{
	private ImageButton mBackBtn;
	private ChangePassTask task;
	private EditText oldPassword;
	private EditText newPassword;
	private EditText reNewPassword;
	private String orginPwd;
	private String newPwd;
	private String confirmPassword;
	private AccountService accountService;
	private ProgressBar progress;
	private Button changeBtn;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.change_password);
		init();
	}
	private void init()
	{
		accountService= new AccountService();
		progress= new ProgressBar(this);
		mBackBtn= (ImageButton) findViewById(R.id.back_btn);
		mBackBtn.setOnClickListener(new BackClick());
		oldPassword= (EditText) findViewById(R.id.origin_password);
		newPassword= (EditText) findViewById(R.id.new_password);
		reNewPassword= (EditText) findViewById(R.id.new_confirm_password);
		changeBtn= (Button) findViewById(R.id.changeBtn);
		changeBtn.setOnClickListener(new ChangeClick());
	}
	private final class ChangeClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			doChangePwd();
		}
	}//end inner
	private void doChangePwd()
	{
		orginPwd= oldPassword.getText().toString().trim();
		confirmPassword= newPassword.getText().toString().trim();
		newPwd= reNewPassword.getText().toString().trim();
		if (StringUtils.isBlank(confirmPassword) || StringUtils.isBlank(newPwd))
		{
			Toast.makeText(this, R.string.change_error, Toast.LENGTH_SHORT).show();
			return;
		}
		if (task != null)
		{
			task.cancel(true);
		}
		task= new ChangePassTask();
		task.execute(0);
	}
	private final class ChangePassTask extends AsyncTask<Integer, Void, ChangePasswordResponse>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}
		@Override
		protected ChangePasswordResponse doInBackground(Integer... params)
		{
			return accountService.doChangePassword(
					MyApplication.getCurrentApp().getUser().getId(),
					orginPwd,
					newPwd,
					confirmPassword);
		}
		@Override
		protected void onPostExecute(ChangePasswordResponse result)
		{
			super.onPostExecute(result);
			if (result != null)
			{
				if (Constants.FLAG_SUCC.equals(result.getFlag()))
				{
					SharedPreferences sp= getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE);
					Editor editor= sp.edit();
					editor.putString(Config.USER_INFO_KEY, "");
					editor.commit();
					Toast.makeText(ChangePwdActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
					ChangePwdActivity.this.finish();
				}
				else
				{
					Toast.makeText(ChangePwdActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				Toast.makeText(ChangePwdActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
			}
		}
	}
	private final class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			ChangePwdActivity.this.finish();
		}
	}// end inner class
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (task != null)
		{
			task.cancel(true);
		}
	}
}// end class
