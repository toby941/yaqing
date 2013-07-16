package com.airAd.yaqinghui.fragment;
import java.text.SimpleDateFormat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airAd.yaqinghui.CepDetailActivity;
import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.business.AlarmService;
import com.airAd.yaqinghui.business.CepService;
import com.airAd.yaqinghui.business.api.vo.param.CepEventScoreParam;
import com.airAd.yaqinghui.business.api.vo.response.CepEventScoreResponse;
import com.airAd.yaqinghui.business.model.Cep;
import com.airAd.yaqinghui.business.model.CepEvent;
import com.airAd.yaqinghui.common.Constants;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.google.zxing.client.android.CaptureActivity;
/**
 * 
 * @author Panyi
 * 
 */
public class CepEventItem extends Fragment
{
	public static final String RED= "cep_type_red";
	public static final String BLUE= "cep_type_blue";
	public static final String GREEN= "cep_type_green";
	public static final int GRAY_COLOR= Color.rgb(209, 206, 197);
	private Cep cep;
	private CepEvent cepEvent;
	public int index;
	private Button signBtn;
	private LocationManagerProxy locationManager;
	private boolean isLocating= false;
	private ProgressDialog mLocateProgressDialog;
	private Button attendBtn, scoreBtn;
	private View scoreView;
	private PopupWindow popWindow;
	private Button submitBtn;
	private RatingBar scoreBar1, scoreBar2, scoreBar3;
	private View parentView;
	private CepService cepSerice;
	private ScoreTask scoreTask;
	private ProgressDialog progressDialog;
	private LayoutInflater mInflater;
	private View medalView;
	private PopupWindow medalPop;
	private ImageView medalTypeImage;
	private Button okBtn;
	private TextView mContent;
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
				locationManager.removeUpdates(this);
				CepDetailActivity cepActivity= (CepDetailActivity) getActivity();
				Intent it= new Intent(getActivity(), CaptureActivity.class);
				getActivity().startActivityForResult(it, UserFragment.SCAN_QRCODE);
				isLocating= false;
				if (mLocateProgressDialog.isShowing())
					mLocateProgressDialog.dismiss();
			}
		}
	};
	public static CepEventItem newInstance(CepEvent data, Cep cep, int index)
	{
		final CepEventItem f= new CepEventItem();
		f.cepEvent= data;
		f.cep= cep;
		f.index= index;
		return f;
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mInflater= (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		cepSerice= new CepService();
		progressDialog= new ProgressDialog(getActivity());
		progressDialog.setTitle(R.string.dialog_title);
		progressDialog.setCancelable(true);
		locationManager= LocationManagerProxy.getInstance(getActivity());
		mLocateProgressDialog= new ProgressDialog(getActivity());
		mLocateProgressDialog.setTitle(R.string.dialog_title);
		mLocateProgressDialog.setMessage(getResources().getText(R.string.is_locating));
		mLocateProgressDialog.setCancelable(true);
		initMedalPop();
	}
	private void initMedalPop()
	{
		medalView= mInflater.inflate(R.layout.getmedal_pop, null);
		medalPop= new PopupWindow(medalView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		medalPop.setBackgroundDrawable(new BitmapDrawable());
		medalPop.setAnimationStyle(R.style.PushPopupAnimation);
		okBtn= (Button) medalView.findViewById(R.id.ok_btn);
		okBtn.setOnClickListener(new OkButtonClick());
		medalTypeImage= (ImageView) medalView.findViewById(R.id.medaltype);
		if (RED.equalsIgnoreCase(cep.getIconType()))
		{
			medalTypeImage.setImageResource(R.drawable.red_medal);
		}
		else if (BLUE.equalsIgnoreCase(cep.getIconType()))
		{
			medalTypeImage.setImageResource(R.drawable.blue_medal);
		}
		else if (GREEN.equalsIgnoreCase(cep.getIconType()))
		{
			medalTypeImage.setImageResource(R.drawable.green_medal);
		}
	}
	private final class OkButtonClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			medalPop.dismiss();
		}
	}//end inner class
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		parentView= getActivity().findViewById(R.id.main);
		final View v= inflater.inflate(R.layout.cep_event, container, false);
		TextView timeText= (TextView) v.findViewById(R.id.cep_event_time);
		mContent= (TextView) v.findViewById(R.id.cep_content);
		long startTimeLong= Long.parseLong(cepEvent.getStartTime());
		long endTimeLong= Long.parseLong(cepEvent.getEndTime());
		SimpleDateFormat sdfEnd= new SimpleDateFormat("HH:mm");
		timeText.setText(cepEvent.getEventTimeRangeDescription());
		attendBtn= (Button) v.findViewById(R.id.attendBtn);
		scoreBtn= (Button) v.findViewById(R.id.scoreBtn);
		TextView locateText= (TextView) v.findViewById(R.id.cep_event_locate);
		locateText.setText(cepEvent.getPlace());
		mContent.setText(cep.getContent());
		setPopWindow(inflater);
		attendBtn.setOnClickListener(new AttendClick());
		if (CepEvent.CEP_EVENT_TYPE_OUT.equals(cepEvent.getCepEventType()))
		{//
			attendBtn.setVisibility(View.GONE);
		}
		signBtn= (Button) v.findViewById(R.id.signin);
		if (cepEvent.canCheckIn())// 可签到
		{
			signBtn.setBackgroundResource(R.drawable.prepost_bg);
			signBtn.setOnClickListener(new ScanClick());
			signBtn.setTextColor(Color.WHITE);
		}
		else
		{
			signBtn.setEnabled(false);
			signBtn.setBackgroundResource(R.drawable.sign_in_bg);
			signBtn.setTextColor(GRAY_COLOR);
		}
		if (cepEvent.canScored())// 可评分
		{
			scoreBtn.setBackgroundResource(R.drawable.prepost_bg);
			scoreBtn.setOnClickListener(new ScoreClick());
			scoreBtn.setTextColor(Color.WHITE);
		}
		else
		{
			scoreBtn.setEnabled(false);
			scoreBtn.setBackgroundResource(R.drawable.sign_in_bg);
			scoreBtn.setTextColor(GRAY_COLOR);
			//			scoreBtn.setBackgroundResource(R.drawable.prepost_bg);
			//			scoreBtn.setOnClickListener(new ScoreClick());
		}
		return v;
	}
	private void setPopWindow(LayoutInflater inflater)
	{
		scoreView= inflater.inflate(R.layout.score_pop, null);
		popWindow= new PopupWindow(scoreView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		popWindow.setFocusable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setAnimationStyle(R.style.PopupAnimation);
		submitBtn= (Button) scoreView.findViewById(R.id.submit);
		scoreBar1= (RatingBar) scoreView.findViewById(R.id.score1);
		scoreBar2= (RatingBar) scoreView.findViewById(R.id.score2);
		scoreBar3= (RatingBar) scoreView.findViewById(R.id.score3);
		submitBtn.setOnClickListener(new SubmitClick());
	}
	private final class SubmitClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			popWindow.dismiss();
			int score1= scoreBar1.getProgress();
			int score2= scoreBar2.getProgress();
			int score3= scoreBar3.getProgress();
			CepService cep= new CepService();
			CepEventScoreParam params= new CepEventScoreParam();
			params.setUserId(MyApplication.getCurrentApp().getUser().getId());
			params.setEventId(cepEvent.getId());
			params.setCepId(cepEvent.getCepId());
			params.setScore(score1 + "," + score2 + "," + score3);
			if (scoreTask != null)
			{
				scoreTask.cancel(true);
			}
			scoreTask= new ScoreTask();
			scoreTask.execute(params);
		}
	}
	/**
	 * 获取勋章
	 */
	private void getMedal()
	{
		medalPop.showAtLocation(parentView, Gravity.CENTER, 0, 0);
	}
	/**
	 * 打分之后勋章操作 
	 * @author Administrator
	 *
	 */
	private final class ScoreTask extends AsyncTask<CepEventScoreParam, Void, CepEventScoreResponse>
	{
		@Override
		protected CepEventScoreResponse doInBackground(CepEventScoreParam... params)
		{
			return (new CepService()).doScoreCepEvent(params[0]);
		}
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			progressDialog.show();
		}
		@Override
		protected void onPostExecute(CepEventScoreResponse result)
		{
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result != null)
			{
				if (Constants.FLAG_SUCC.equals(result.getFlag()))//签到成功
				{
					Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
					//勋章操作
					getMedal();
					CepDetailActivity acy= (CepDetailActivity) getActivity();
					acy.requestDetailData(index);
				}
				else
				{
					Toast.makeText(getActivity(), result.getMsg(), Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				Toast.makeText(getActivity(), R.string.cep_score_failed, Toast.LENGTH_SHORT).show();
			}
		}
	}//end inner class
	private final class AttendClick implements OnClickListener
	{
		@Override
		public void onClick(View view)
		{
			//			System.out.println("报名参加");
			cepSerice.doReservationCepEvent(MyApplication.getCurrentApp().getUser().getId(), cep, cepEvent);
			AlarmService.getInstance().addAlarm(
					Integer.parseInt(cepEvent.getId()),
					cepEvent.getStartTimel(),
					cep.getContent());
			Toast.makeText(getActivity(), R.string.watch_success, Toast.LENGTH_SHORT).show();
		}
	}//end inner class
	private final class ScoreClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			popWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		}
	}//end inner class
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
				//				mLocateProgressDialog.show();
				Intent it= new Intent(getActivity(), CaptureActivity.class);
				getActivity().startActivityForResult(it, UserFragment.SCAN_QRCODE);
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
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		locationManager.removeUpdates(locationListener);
		if (scoreTask != null)
		{
			scoreTask.cancel(true);
		}
		System.gc();
	}
}// end class
