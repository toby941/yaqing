package com.airAd.yaqinghui.fragment;

import java.util.Locale;

import net.sf.json.JSONObject;

import com.airAd.yaqinghui.HomeActivity;
import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.WelcomeActivity;
import com.airAd.yaqinghui.business.CepService;
import com.airAd.yaqinghui.business.api.BasicAPI;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.common.Config;
import com.airAd.yaqinghui.common.StringUtil;
import com.airAd.yaqinghui.core.HessianClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 登陆Fragment
 * 
 * @author Panyi
 * 
 */
public class LoginFragment extends Fragment {
	private EditText mUserText;
	private EditText mPwdText;
	private Button mLogin;
	private ProgressDialog progressDialog;
	private DoLoginTask loginTask;

	private boolean isLogining = false;

	public static LoginFragment newInstance() {
		LoginFragment fragment = new LoginFragment();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_login, container, false);
	}

	@Override
	public void onStop() {
		if (loginTask != null) {
			loginTask.cancel(true);
		}
		super.onStop();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mUserText = (EditText) getActivity().findViewById(R.id.user_text);
		mPwdText = (EditText) getActivity().findViewById(R.id.pwd_text);
		mLogin = (Button) getActivity().findViewById(R.id.login);
		mLogin.setOnClickListener(new LoginClick());

		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage(getActivity().getString(R.string.islogining));
	}

	private final class LoginClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (!isLogining) {
				Info info = new Info();
				info.user = mUserText.getText().toString().trim();
				info.pwd = mPwdText.getText().toString().trim();
				
				info.user ="00000001";
				info.pwd = "1236";
				loginTask = new DoLoginTask();
				loginTask.execute(info);
			}
		}
	}

	private final class Info {
		public String user;
		public String pwd;
	}

	private final class DoLoginTask extends AsyncTask<Info, Void, User> {
		@Override
		protected void onPreExecute() {
			isLogining = true;
			super.onPreExecute();
			progressDialog.show();
		}

		@Override
		protected User doInBackground(Info... params) {
			Info info = params[0];
			BasicAPI basicAPI = HessianClient.create();
			JSONObject jsonObj = null;
			try {
				//("00000001", "1236","CHI");
				jsonObj = basicAPI.UserLogin(info.user, info.pwd, User.getLan());
			} catch (Exception e) {
				 return null;
			}
			return User.instance(jsonObj);
		}

		@Override
		protected void onPostExecute(User result) {
			super.onPostExecute(result);
			isLogining = false;
			progressDialog.dismiss();
			if (result != null) {
				SharedPreferences sp = getActivity().getSharedPreferences(
						Config.PACKAGE, Context.MODE_PRIVATE);
				sp.edit().putString(Config.USER_INFO_KEY, result.getTemp())
						.commit();
				MyApplication.getCurrentApp().setUser(
						User.instance(result.getTemp()));
				Intent intent = new Intent(getActivity(), HomeActivity.class);
				getActivity().startActivity(intent);
				getActivity().finish();
			} else {
				Toast.makeText(getActivity(), R.string.login_failed,
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}// end class
