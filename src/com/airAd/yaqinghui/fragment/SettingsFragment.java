package com.airAd.yaqinghui.fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.airAd.yaqinghui.ChangePwdActivity;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.TutorialActivity;
import com.airAd.yaqinghui.WelcomeActivity;
import com.airAd.yaqinghui.common.Config;
/**
 * 设置Fragment
 * 
 * @author Panyi
 */
public class SettingsFragment extends Fragment
{
	public static final int DEFAULT_BEFORE_TIME= 30;
	public static final String DEFAULT_REGULAR_TIME= "6:30am";
	private ImageButton mBack;
	private ImageView isOpenEventRemind;
	private TextView eventRemindText;
	private boolean isOpenEventRemind_flag;
	private int eventRemindBeforeValue;
	private TextView dialyText;
	private ImageView dialyImage;
	private boolean isDialyReminderOpen;
	private String dialyRemindString;
	private View parentView;
	private View changePassword;
	private View eventRemindView;
	private View dialyRemindView;
	private View popContentView;
	private PopupWindow popWindow;
	private TextView popTitle;
	private View helpBtn;
	private Button leaveBtn;
	public static SettingsFragment newInstance()
	{
		SettingsFragment fragment= new SettingsFragment();
		return fragment;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view= inflater.inflate(R.layout.fragment_settings, container, false);
		parentView= view;
		SharedPreferences sp= getActivity().getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE);
		mBack= (ImageButton) view.findViewById(R.id.back);
		mBack.setOnClickListener(new BackClick());
		eventRemindText= (TextView) view.findViewById(R.id.event_remind_text);
		isOpenEventRemind= (ImageView) view.findViewById(R.id.event_remind_isopen);
		isOpenEventRemind_flag= sp.getBoolean(Config.EVENT_REMIND_ISOPEN, true);
		eventRemindBeforeValue= sp.getInt(Config.EVENT_REMIND_BEFORE, DEFAULT_BEFORE_TIME);
		isOpenEventRemind.setImageResource(isOpenEventRemind_flag ? R.drawable.switch_open : R.drawable.switch_close);
		eventRemindText.setText(getActivity().getString(R.string.event_remind_text, eventRemindBeforeValue));
		dialyText= (TextView) view.findViewById(R.id.dialy_remind_text);
		dialyImage= (ImageView) view.findViewById(R.id.dialy_remind_isopen);
		isDialyReminderOpen= sp.getBoolean(Config.REGULAR_REMIND_ISOPEN, true);
		dialyRemindString= sp.getString(Config.REGULAR_REMIND_TIME, DEFAULT_REGULAR_TIME);
		dialyText.setText(getActivity().getString(R.string.dialy_remind_text, dialyRemindString));
		dialyImage.setImageResource(isDialyReminderOpen ? R.drawable.switch_open : R.drawable.switch_close);
		changePassword= view.findViewById(R.id.change_password);
		changePassword.setOnClickListener(new ChangePasswordClick());
		eventRemindView= view.findViewById(R.id.event_remind_btn);
		dialyRemindView= view.findViewById(R.id.dialy_remind_btn);
		helpBtn= view.findViewById(R.id.helpBtn);
		helpBtn.setOnClickListener(new HelpClick());
		leaveBtn= (Button) view.findViewById(R.id.login_out);
		leaveBtn.setOnClickListener(new LoginOutClick());
		initPopWindow(view, inflater);
		return view;
	}
	private void initPopWindow(View parentView, LayoutInflater inflater)
	{
		popContentView= inflater.inflate(R.layout.select_pretime_pop, null);
		popWindow= new PopupWindow(popContentView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popWindow.setFocusable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setAnimationStyle(R.style.PopupAnimation);
		popTitle= (TextView) popContentView.findViewById(R.id.set_event_flag);
		String title= isOpenEventRemind_flag
				? getString(R.string.open_event_remind)
				: getString(R.string.close_event_remind);
		popTitle.setText(title);
		eventRemindView.setOnClickListener(new OpenTimeDeltaPop());
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
	}//end inner class
	private final class HelpClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Intent it= new Intent(getActivity(), TutorialActivity.class);
			it.putExtra("subNum", 2);
			getActivity().startActivity(it);
		}
	}//end inner class
	/**
	 * 打开时间段选择工具
	 * @author Administrator
	 *
	 */
	private final class OpenTimeDeltaPop implements OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			popWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		}
	}//end inner class
	private final class ChangePasswordClick implements OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			Intent it= new Intent(getActivity(), ChangePwdActivity.class);
			getActivity().startActivity(it);
		}
	}//end inner class
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
			String title= isOpenEventRemind_flag
					? getString(R.string.open_event_remind)
					: getString(R.string.close_event_remind);
			popTitle.setText(title);
		}
	}//end inner class
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
			dialyImage.setImageResource(isDialyReminderOpen ? R.drawable.switch_open : R.drawable.switch_close);
		}
	}//end inner class
	private final class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			getActivity().getSupportFragmentManager().beginTransaction().hide(SettingsFragment.this).commit();
		}
	}//end inner class
}// end class
