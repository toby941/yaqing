package com.airAd.yaqinghui.fragment;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airAd.yaqinghui.HomeActivity;
import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.MyCepActivity;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.business.NotificationMessageService;
import com.airAd.yaqinghui.business.model.NotificationMessage;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.ui.CustomViewPager;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.google.zxing.client.android.CaptureActivity;
/**
 * 
 * @author Panyi
 * 
 */
public class UserFragment extends Fragment
{
	public static final int MYCEP_ORDER= 1;//预约
	public static final int MYCEP_SIGNIN= 2;//签到
	public static final int MYCEP_COMMENT= 3;//评论
	public static final String MYCEP_TYPE= "my_cep_type";
	public static final int SCAN_QRCODE= 400;
	protected CustomViewPager mGallery;
	private ImageView thumbImage;
	private Button signImage;
	private boolean isLocating= false;
	private View settingBtn;
	private View notifyBtn;
	private User user;
	private NotifyList notifyListFragment;
	private SettingsFragment settingFragment;
	private ProgressDialog mProgressDialog;
	private ImageView myCepOrder, myCepSingn, myCepComment;
	private TextView orderNumText, signNumText, commentNumText;
	public AMapLocationListener locationListener= new AMapLocationListener()
	{
		@Override
		public void onLocationChanged(Location location)
		{
		}
		@Override
		public void onProviderDisabled(String provider)
		{
		}
		@Override
		public void onProviderEnabled(String provider)
		{
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
		}
		@Override
		public void onLocationChanged(AMapLocation location)
		{
			if (location != null)
			{
				Double geoLat= location.getLatitude();
				Double geoLng= location.getLongitude();
				//				System.out.println("location--->" + geoLat + "," + geoLng);
				locationManager.removeUpdates(this);
				Intent it= new Intent(getActivity(), CaptureActivity.class);
				HomeActivity home= (HomeActivity) getActivity();
				home.lat= geoLat + "";
				home.lng= geoLng + "";
				getActivity().startActivityForResult(it, SCAN_QRCODE);
				isLocating= false;
				if (mProgressDialog.isShowing())
					mProgressDialog.dismiss();
			}
		}
	};
	LocationManagerProxy locationManager;
	public static UserFragment newInstance(CustomViewPager gallery)
	{
		final UserFragment f= new UserFragment();
		f.mGallery= gallery;
		return f;
	}
	public ImageView getThumb()
	{
		return thumbImage;
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		user= MyApplication.getCurrentUser();
		locationManager= LocationManagerProxy.getInstance(getActivity());
		mProgressDialog= new ProgressDialog(getActivity());
		mProgressDialog.setTitle(R.string.dialog_title);
		mProgressDialog.setMessage(getResources().getText(R.string.is_locating));
		mProgressDialog.setCancelable(true);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view= inflater.inflate(R.layout.menu_lefts_user, container, false);
		// TODO
		setThumbImage(view);
		orderNumText= (TextView) view.findViewById(R.id.order_num);
		signNumText= (TextView) view.findViewById(R.id.signin_num);
		commentNumText= (TextView) view.findViewById(R.id.comment_num);
		thumbImage= (ImageView) view.findViewById(R.id.headpic);
		thumbImage.setOnClickListener(new ThumbClick());
		signImage= (Button) view.findViewById(R.id.sign);
		signImage.setOnClickListener(new ScanClick());
		settingBtn= view.findViewById(R.id.settingBtn);
		settingBtn.setOnClickListener(new SettingClick());
		notifyBtn= view.findViewById(R.id.notifyBtn);
		notifyBtn.setOnClickListener(new NotifyClick());
		myCepOrder= (ImageView) view.findViewById(R.id.mycep_order);
		myCepSingn= (ImageView) view.findViewById(R.id.mycep_signin);
		myCepComment= (ImageView) view.findViewById(R.id.mycep_comment);
		if (user != null)
		{
			TextView userName= (TextView) view.findViewById(R.id.username);
			userName.setText(user.getName());// 设置用户名
			myCepOrder.setOnClickListener(new GotoMyCep(NotificationMessage.TYPE_CEPEVENT_SIGNUP_HIS));
			myCepSingn.setOnClickListener(new GotoMyCep(NotificationMessage.TYPE_CEPEVENT_CHECKIN_HIS));
			myCepComment.setOnClickListener(new GotoMyCep(NotificationMessage.TYPE_CEPEVENT_SCORE_HIS));
		}
		return view;
	}
	@Override
	public void onResume()
	{
		super.onResume();
		NotificationMessageService service= new NotificationMessageService();
		Map<Integer, Integer> map= service.getHisMapData(MyApplication.getCurrentApp().getUser().getId());
		if (map == null)
		{
			return;
		}
		Integer ordernum= map.get(NotificationMessage.TYPE_CEPEVENT_SIGNUP_HIS);
		Integer signnum= map.get(NotificationMessage.TYPE_CEPEVENT_CHECKIN_HIS);
		Integer commentnum= map.get(NotificationMessage.TYPE_CEPEVENT_SCORE_HIS);
		if (ordernum != null)
		{
			if (ordernum > 0)
			{
				myCepOrder.setImageResource(R.drawable.baoming);
				orderNumText.setVisibility(View.VISIBLE);
				orderNumText.setText(ordernum + "");
			}
			else
			{
				myCepOrder.setImageResource(R.drawable.baoming_none);
				orderNumText.setVisibility(View.INVISIBLE);
			}
		}
		else
		{
			myCepOrder.setImageResource(R.drawable.baoming_none);
			orderNumText.setVisibility(View.INVISIBLE);
		}
		if (signnum != null)
		{
			if (signnum > 0)
			{
				myCepSingn.setImageResource(R.drawable.qiandao);
				signNumText.setVisibility(View.VISIBLE);
				signNumText.setText(signnum + "");
			}
			else
			{
				myCepSingn.setImageResource(R.drawable.qiandao_none);
				signNumText.setVisibility(View.INVISIBLE);
			}
		}
		else
		{
			myCepSingn.setImageResource(R.drawable.qiandao_none);
			signNumText.setVisibility(View.INVISIBLE);
		}
		if (commentnum != null)
		{
			if (commentnum > 0)
			{
				myCepComment.setImageResource(R.drawable.pinlun);
				commentNumText.setVisibility(View.VISIBLE);
				commentNumText.setText(commentnum + "");
			}
			else
			{
				myCepComment.setImageResource(R.drawable.pinlun_none);
				commentNumText.setVisibility(View.INVISIBLE);
			}
		}
		else
		{
			myCepComment.setImageResource(R.drawable.pinlun_none);
			commentNumText.setVisibility(View.INVISIBLE);
		}
	}
	private void setThumbImage(View view)
	{
		ImageView userImage= (ImageView) view.findViewById(R.id.headpic);
		userImage.setImageBitmap(((HomeActivity) getActivity()).getThumbBitmap());
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onPause()
	{
		super.onPause();
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		locationManager.removeUpdates(locationListener);
		System.gc();
	}
	private final class GotoMyCep implements OnClickListener
	{
		int type;
		public GotoMyCep(int type)
		{
			this.type= type;
		}
		@Override
		public void onClick(View v)
		{
			Intent it= new Intent(getActivity(), MyCepActivity.class);
			it.putExtra(MYCEP_TYPE, type);
			getActivity().startActivity(it);
		}
	}//end inner class
	private final class SettingClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			FragmentManager fm= getActivity().getSupportFragmentManager();
			FragmentTransaction ft= fm.beginTransaction();
			ft.replace(R.id.left_menu_container, SettingsFragment.newInstance());
			ft.commit();
		}
	}//end inner class
	private final class NotifyClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			FragmentManager fm= getActivity().getSupportFragmentManager();
			FragmentTransaction ft= fm.beginTransaction();
			ft.replace(R.id.left_menu_container, NotifyList.newInstance());
			ft.commit();
		}
	}//end inner class
	private final class ThumbClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			mGallery.setCurrentItem(1);// 跳转到个人信息页
		}
	}// end inner class
	private final class ScanClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if (isLocating)
			{// 当前正在定位 按钮不响应
				return;
			}
			if (openGPSSettings())
			{// GPS确保打开
				//				locationManager.removeUpdates(locationListener);
				//				locationManager.setGpsEnable(true);
				//				locationManager.requestLocationUpdates(LocationProviderProxy.AMapNetwork, 5000, 10, locationListener);
				//				isLocating= true;
				//				mProgressDialog.show();
				Intent it= new Intent(getActivity(), CaptureActivity.class);
				getActivity().startActivityForResult(it, SCAN_QRCODE);
			}
		}
	}// end inner class
	private boolean openGPSSettings()
	{
		LocationManager alm= (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			return true;
		}
		else
		{
			Toast.makeText(getActivity(), R.string.opengps, Toast.LENGTH_SHORT).show();
			getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			return false;
		}
	}
}// end class
