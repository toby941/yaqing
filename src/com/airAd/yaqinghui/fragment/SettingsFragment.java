package com.airAd.yaqinghui.fragment;
import net.sf.json.JSONObject;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.airAd.yaqinghui.ChangePwdActivity;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.TutorialActivity;
import com.airAd.yaqinghui.WelcomeActivity;
import com.airAd.yaqinghui.business.AlarmService;
import com.airAd.yaqinghui.common.Config;
import com.airAd.yaqinghui.common.Constants;
import com.airAd.yaqinghui.net.HttpRequest;
import com.airAd.yaqinghui.net.HttpRequest.HttpRequestException;
/**
 * 设置Fragment
 * 
 * @author Panyi
 */
public class SettingsFragment extends Fragment
{
	//public static final int DEFAULT_BEFORE_TIME = 0;
	//public static final int DEFAULT_REGULAR_TIME = "6:30";
	private ImageButton mBack;
	private ImageView isOpenEventRemind;
	private TextView eventRemindText;
	private boolean isOpenEventRemind_flag;
	private int eventRemindBeforeIndex;
	private TextView dialyText;
	private ImageView dialyImage;
	private boolean isDialyReminderOpen;
	private int dialyRemindIndex;
	private View parentView;
	private EditText notifyTimeEditText;
	private View changePassword;
	private View eventRemindView;
	private View dialyRemindView;
	private View popContentView;
	private PopupWindow popWindow;
	// private TextView popTitle;
	private View helpBtn;
	private Button leaveBtn;
	private CheckUpdateTask task;
	private Intent intent= new Intent();
	public static SettingsFragment newInstance()
	{
		SettingsFragment fragment= new SettingsFragment();
		return fragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		intent.setAction(Constants.SCHDULE_LIST_REFRESH);
	}
	@Override
	public void onResume()
	{
		super.onResume();
		Intent intent= new Intent();
		intent.setAction(Constants.SCHDULE_LIST_REFRESH);
		getActivity().sendBroadcast(intent);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view= inflater.inflate(R.layout.fragment_settings, container, false);
		parentView= view;
		// parentView = inflater.inflate(R.layout.add_time, container, false);
		SharedPreferences sp= getActivity().getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE);
		mBack= (ImageButton) view.findViewById(R.id.back);
		mBack.setOnClickListener(new BackClick());
		eventRemindText= (TextView) view.findViewById(R.id.event_remind_text);
		isOpenEventRemind= (ImageView) view.findViewById(R.id.event_remind_isopen);
		isOpenEventRemind_flag= sp.getBoolean(Config.EVENT_REMIND_ISOPEN, false);
		isOpenEventRemind.setImageResource(isOpenEventRemind_flag ? R.drawable.switch_open : R.drawable.switch_close);
		eventRemindBeforeIndex= sp.getInt(Config.EVENT_REMIND_BEFORE, 0);
		eventRemindText
				.setText(getActivity().getResources().getStringArray(R.array.time_before_array)[eventRemindBeforeIndex]);
		dialyText= (TextView) view.findViewById(R.id.dialy_remind_text);
		dialyImage= (ImageView) view.findViewById(R.id.dialy_remind_isopen);
		isDialyReminderOpen= sp.getBoolean(Config.REGULAR_REMIND_ISOPEN, false);
		dialyRemindIndex= sp.getInt(Config.REGULAR_REMIND_TIME, 0);
		dialyText.setText(getActivity().getResources().getStringArray(R.array.time_daily_array)[dialyRemindIndex]);
		dialyImage.setImageResource(isDialyReminderOpen ? R.drawable.switch_open : R.drawable.switch_close);
		changePassword= view.findViewById(R.id.change_password);
		changePassword.setOnClickListener(new ChangePasswordClick());
		eventRemindView= view.findViewById(R.id.event_remind_btn);
		dialyRemindView= view.findViewById(R.id.dialy_remind_btn);
		helpBtn= view.findViewById(R.id.helpBtn);
		helpBtn.setOnClickListener(new HelpClick());
		leaveBtn= (Button) view.findViewById(R.id.login_out);
		leaveBtn.setOnClickListener(new LoginOutClick());
		view.findViewById(R.id.upload_item_view).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				checkUpdate();
			}
		});
		initPopWindow(view, inflater);
		return view;
	}
	private void initPopWindow(View parentView, LayoutInflater inflater)
	{
		eventRemindView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.event_tips).setItems(
						R.array.time_before_array,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								if (which != eventRemindBeforeIndex)
								{
									eventRemindBeforeIndex= which;
									eventRemindText.setText(getActivity().getResources().getStringArray(
											R.array.time_before_array)[eventRemindBeforeIndex]);
									SharedPreferences sp= getActivity().getSharedPreferences(
											Config.PACKAGE,
											Context.MODE_PRIVATE);
									sp.edit().putInt(Config.EVENT_REMIND_BEFORE, eventRemindBeforeIndex).commit();
									// 重设全部闹铃
									AlarmService.getInstance().resetAllAlarm();
									getActivity().sendBroadcast(intent);
								}
							}
						});
				builder.create().show();
			}
		});
		dialyRemindView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
				builder.setTitle(R.string.dialy_remind).setItems(
						R.array.time_daily_array,
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialog, int which)
							{
								if (which != dialyRemindIndex)
								{
									dialyRemindIndex= which;
									dialyText.setText(getActivity().getResources().getStringArray(
											R.array.time_daily_array)[dialyRemindIndex]);
									SharedPreferences sp= getActivity().getSharedPreferences(
											Config.PACKAGE,
											Context.MODE_PRIVATE);
									sp.edit().putInt(Config.REGULAR_REMIND_TIME, dialyRemindIndex).commit();
									AlarmService.getInstance().setDailyRepeatAlarm();
								}
							}
						});
				builder.create().show();
			}
		});
	}
	@Override
	public void onStop()
	{
		super.onStop();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		isOpenEventRemind.setOnClickListener(new OpenEventRemind());
		dialyImage.setOnClickListener(new OpenDialyRemind());
	}
	private final class LoginOutClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			SharedPreferences sp= getActivity().getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE);
			Editor editor= sp.edit();
			editor.putString(Config.USER_INFO_KEY, "");
			editor.commit();
			Toast.makeText(getActivity(), R.string.loginout_success, Toast.LENGTH_SHORT).show();
			Intent it= new Intent(getActivity(), WelcomeActivity.class);
			getActivity().startActivity(it);
			getActivity().finish();
		}
	}// end inner class
	private final class HelpClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Intent it= new Intent(getActivity(), TutorialActivity.class);
			it.putExtra("subNum", 2);
			getActivity().startActivity(it);
		}
	}// end inner class
	private final class ChangePasswordClick implements OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			Intent it= new Intent(getActivity(), ChangePwdActivity.class);
			getActivity().startActivity(it);
		}
	}// end inner class
	private final class OpenEventRemind implements OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			isOpenEventRemind_flag= !isOpenEventRemind_flag;
			SharedPreferences sp= getActivity().getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE);
			Editor ed= sp.edit();
			ed.putBoolean(Config.EVENT_REMIND_ISOPEN, isOpenEventRemind_flag);
			ed.commit();
			isOpenEventRemind.setImageResource(isOpenEventRemind_flag
					? R.drawable.switch_open
					: R.drawable.switch_close);
			if (isOpenEventRemind_flag)
			{
				eventRemindView.setEnabled(true);
			}
			else
			{
				eventRemindView.setEnabled(false);
			}
		}
	}// end inner class
	private final class OpenDialyRemind implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			isDialyReminderOpen= !isDialyReminderOpen;
			SharedPreferences sp= getActivity().getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE);
			Editor ed= sp.edit();
			ed.putBoolean(Config.REGULAR_REMIND_ISOPEN, isDialyReminderOpen);
			ed.commit();
			// 根据选择舍去或者添加 闹铃
			if (isDialyReminderOpen)
			{
				AlarmService.getInstance().setDailyRepeatAlarm();
				dialyRemindView.setEnabled(true);
			}
			else
			{
				AlarmService.getInstance().cancelDailyRepeatAlarm();
				dialyRemindView.setEnabled(false);
			}
			dialyImage.setImageResource(isDialyReminderOpen ? R.drawable.switch_open : R.drawable.switch_close);
		}
	}// end inner class
	private final class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			getActivity().getSupportFragmentManager().beginTransaction().hide(SettingsFragment.this).commit();
		}
	}// end inner class
	private void checkUpdate()
	{
		if (task != null)
		{
			task.cancel(true);
		}
		task= new CheckUpdateTask();
		task.execute();
	}
	private class CheckUpdateTask extends AsyncTask<Void, Void, String>
	{
		ProgressDialog progressDialog;
		@Override
		protected void onPreExecute()
		{
			progressDialog= new ProgressDialog(getActivity());
			progressDialog.setMessage(getActivity().getText(R.string.upload_waiting));
			progressDialog.show();
		}
		@Override
		protected String doInBackground(Void... params)
		{
			try
			{
				String response= HttpRequest.get(Config.UPDATE_URL).body();
				JSONObject json= JSONObject.fromObject(response);
				Log.i("uploadFragment", json.toString());
				int netVersion= json.optInt("version", -1);
				int localVersion= getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionCode;
				if (netVersion > 0 && netVersion > localVersion)
				{
					return json.optString("download_url");
				}
			}
			catch (HttpRequestException e)
			{
				e.printStackTrace();
			}
			catch (NameNotFoundException e)
			{
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(String result)
		{
			if (progressDialog.isShowing() && !getActivity().isFinishing())
			{
				progressDialog.dismiss();
			}
			if (result != null)
			{
				Intent i= new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(result));
				startActivity(i);
			}
			Toast.makeText(getActivity().getApplicationContext(), R.string.update_no_need, Toast.LENGTH_LONG).show();
		}
	}
}// end class
