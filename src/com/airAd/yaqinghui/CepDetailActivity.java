package com.airAd.yaqinghui;
import java.io.IOException;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.yaqinghui.business.CepService;
import com.airAd.yaqinghui.business.api.vo.param.CepEventCheckinParam;
import com.airAd.yaqinghui.business.api.vo.response.CepEventCheckinResponse;
import com.airAd.yaqinghui.business.model.Cep;
import com.airAd.yaqinghui.business.model.CepEvent;
import com.airAd.yaqinghui.common.Config;
import com.airAd.yaqinghui.common.Constants;
import com.airAd.yaqinghui.core.ImageFetcherFactory;
import com.airAd.yaqinghui.fragment.CepEventItem;
import com.airAd.yaqinghui.fragment.ImageDetailFragment;
import com.airAd.yaqinghui.fragment.UserFragment;
import com.airAd.yaqinghui.ui.CepTextView;
import com.google.zxing.client.android.CaptureActivity;
/**
 * 
 * @author Panyi
 * 
 */
public class CepDetailActivity extends BaseActivity
{
	public static final int STARS_NUM= 5;
	private ImageButton mBackBtn;
	private ImageButton mWeiboBtn;
	private ViewPager mGallery;
	private ViewPager mCepEventGallery;
	private ImageFetcher mImageFetcher;
	private TextView mTitleText;
	//	private TextView mContentText;
	private RelativeLayout progress;
	private RequestTask mTask;
	private String cepId;// cep活动ID
	private LayoutInflater mInflater;
	private PopupWindow pop;
	private RelativeLayout mainLayout;
	private boolean isCancel= false;
	private ProgressDialog progressDialog;
	private CepEventAdapter mCepEventAdapter;
	private ImageView lineShadow;
	private View popLayoutView;
	private Cep cep;
	private View eventHeaderView;
	private ImageView mLeftArrow, mRightArrow;
	public static final int EVENT_TIP_NUM= 3;
	private CepTextView[] eventTop= new CepTextView[EVENT_TIP_NUM];
	private CepTextView selectedEventTop;
	private int sub_cur;
	private int sub_start, sub_end;
	private int event_length;
	public final String lat= "46.8897";
	public final String lng= "199.8888";
	private ImageView typeImage;
	private ImageView[] starsImg= new ImageView[STARS_NUM];
	private SigninTask signTask;
	private AssetManager assetManager;
	private ProgressDialog proDialog;
	private int eventIndex= 0;
	private String requestEventId= "-1";
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		init();
		requestDetailData(0);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
		{
			if (requestCode == UserFragment.SCAN_QRCODE)
			{
				String twobarcode= data.getStringExtra(CaptureActivity.FLAG);
				Log.e("yq", twobarcode);
				// System.out.println(twobarcode);
				requestSign(twobarcode, MyApplication.getCurrentApp().getUser().getId(), lng, lat);
			}
		}
	}
	private void requestSign(String twobarcode, String userid, String lng, String lat)
	{
		if (signTask != null)
		{
			signTask.cancel(true);
		}
		signTask= new SigninTask();
		CepEventCheckinParam params= new CepEventCheckinParam();
		params.setCepId(Cep.getIdFromQrcode(twobarcode) + "");
		params.setUserId(userid);
		params.setQrcode(twobarcode);
		params.setLat(lat);
		params.setLng(lng);
		signTask.execute(params);
	}
	private final class SigninTask extends AsyncTask<CepEventCheckinParam, Void, CepEventCheckinResponse>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
		}
		@Override
		protected CepEventCheckinResponse doInBackground(CepEventCheckinParam... param)
		{
			return (new CepService()).doCheckinCepEvent(param[0]);
		}
		@Override
		protected void onPostExecute(CepEventCheckinResponse result)
		{
			super.onPostExecute(result);
			if (result != null)
			{
				if (Constants.FLAG_SUCC.equals(result.getFlag()))//签到成功
				{
					Toast.makeText(CepDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(CepDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
				}
			}
			else
			{
				Toast.makeText(CepDetailActivity.this, R.string.login_failed, Toast.LENGTH_SHORT).show();
			}
		}
	}//end inner class
	/**
	 * 
	 * @param service
	 */
	public void requestDetailData(int gotoIndex)
	{
		eventTop[0].unSelected();
		eventTop[1].unSelected();
		eventTop[2].unSelected();
		eventIndex= gotoIndex;
		if (mTask != null)
		{
			mTask.cancel(true);
		}
		mTask= new RequestTask();
		mTask.execute(cepId);
	}
	private final class RequestTask extends AsyncTask<String, Integer, Cep>
	{
		@Override
		protected Cep doInBackground(String... params)
		{
			return new CepService().getCep(MyApplication.getCurrentUser().getId(), params[0]);
		}
		@Override
		protected void onPostExecute(Cep result)
		{
			super.onPostExecute(result);
			cep= result;
			if (cep == null)
			{
				Toast.makeText(CepDetailActivity.this, R.string.net_exception, Toast.LENGTH_SHORT).show();
				return;
			}
			for (int i= 0; i < cep.getCepEvents().size(); i++)
			{
				CepEvent cepEvent= cep.getCepEvents().get(i);
				String number= cepEvent.getTabId() + "";
				cepEvent.setName(getString(R.string.number) + number);
				if (requestEventId != null && requestEventId.equals(cepEvent.getId()))
				{
					eventIndex= i;
					System.out.println("eventIndex-->" + eventIndex);
				}
			}//end for i
			mGallery.setAdapter(new ImagePagerAdapter(CepDetailActivity.this.getSupportFragmentManager(),
					cep.getPics(), cep));
			//typeImage.setImageResource(Common.getCepTypePic(cep.getIconType()));
			try
			{
				typeImage.setImageBitmap(BitmapFactory.decodeStream(assetManager.open(cep.getIconType() + ".png")));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			setSorce();
			mTitleText.setText(cep.getTitle());
			//			mContentText.setText(cep.getContent());
			mCepEventGallery.setAdapter(new CepEventAdapter(CepDetailActivity.this.getSupportFragmentManager(), cep
					.getCepEvents()));
			setEventComponent();
			if (eventIndex > 0 && eventIndex < cep.getCepEvents().size())
			{
				//				eventTop[0].unSelected();
				//				eventTop[1].unSelected();
				//				eventTop[2].unSelected();
				selectedEventTop.unSelected();
				int len= cep.getCepEvents().size();
				if (eventIndex == 0)
				{
					sub_start= eventIndex;
					selectedEventTop= eventTop[0];
				}
				else if (eventIndex == len - 1)
				{
					sub_start= eventIndex - 2;
					selectedEventTop= eventTop[2];
				}
				else
				{
					sub_start= eventIndex - 1;
					selectedEventTop= eventTop[1];
				}
				mCepEventGallery.setCurrentItem(eventIndex);
				selectedEventTop.selected();
			}
			else
			{
				mCepEventGallery.setCurrentItem(0);
				eventToIndex(0);
			}
			eventIndex= 0;
			progress.setVisibility(View.GONE);
		}
	}// end inner class
	private void setEventComponent()
	{
		if (cep.getCepEvents().size() == 1)// 仅有一场活动
		{
			eventHeaderView.setVisibility(View.GONE);
		}
		else if (cep.getCepEvents().size() == 2)
		{
			mLeftArrow.setVisibility(View.INVISIBLE);
			mRightArrow.setVisibility(View.INVISIBLE);
			lineShadow.setVisibility(View.GONE);
			eventTop[2].setVisibility(View.GONE);
			eventTop[0].setCepEvent(cep.getCepEvents().get(0));
			eventTop[1].setCepEvent(cep.getCepEvents().get(1));
			selectedEventTop= eventTop[0];
			selectedEventTop.selected();
			mCepEventGallery.setOnPageChangeListener(new OnPageChangeListener()
			{
				@Override
				public void onPageSelected(int index)
				{
					if (index == 0)
					{
						eventTop[1].unSelected();
						eventTop[0].selected();
					}
					else if (index == 1)
					{
						eventTop[0].unSelected();
						eventTop[1].selected();
					}
				}
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2)
				{
				}
				@Override
				public void onPageScrollStateChanged(int arg0)
				{
				}
			});
			eventTop[0].setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mCepEventGallery.setCurrentItem(0);
				}
			});
			eventTop[1].setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mCepEventGallery.setCurrentItem(1);
				}
			});
		}
		else if (cep.getCepEvents().size() >= 3)
		{
			sub_start= sub_cur= 0;
			sub_end= sub_start + 3;
			event_length= cep.getCepEvents().size();
			for (int i= sub_start, j= 0; i < sub_end; i++, j++)
			{
				eventTop[j].setCepEvent(cep.getCepEvents().get(i));
			}//end for
			selectedEventTop= eventTop[0];
			selectedEventTop.selected();
			mLeftArrow.setVisibility(View.INVISIBLE);
			if (event_length > 3)
			{
				mRightArrow.setVisibility(View.VISIBLE);
			}
			else
			{
				mRightArrow.setVisibility(View.INVISIBLE);
			}
			mCepEventGallery.setOnPageChangeListener(new OnPageChangeListener()
			{
				@Override
				public void onPageSelected(int index)
				{
					eventToIndex(index);
				}
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2)
				{
				}
				@Override
				public void onPageScrollStateChanged(int arg0)
				{
				}
			});
			eventTop[0].setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mCepEventGallery.setCurrentItem(sub_start);
				}
			});
			eventTop[1].setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mCepEventGallery.setCurrentItem(sub_start + 1);
				}
			});
			eventTop[2].setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mCepEventGallery.setCurrentItem((sub_start + 2));
				}
			});
		}
	}
	private void eventToIndex(int index)
	{
		sub_end= sub_start + 3;
		if (index >= sub_start && index < sub_end)
		{
			if (sub_start == 0 && sub_end != event_length)// 在头部
			{
				if (index == sub_start)
				{
					sub_start= sub_cur= 0;
				}
				else
				{
					sub_cur= 1;
					sub_start= index - 1;
				}
			}
			else if (sub_end == event_length && sub_start != 0)
			{// 在尾部
				if (index == event_length - 1)
				{
					sub_cur= 2;
				}
				else
				{
					sub_cur= 1;
					sub_start= index - 1;
				}
			}
			else if (sub_start == 0 && sub_end == event_length)
			{
				if (index == sub_start)
				{
					sub_cur= 0;
				}
				else if (index == sub_end - 1)
				{
					sub_cur= sub_end - 1;
				}
				else
				{
					sub_cur= 1;
				}
				sub_start= 0;
			}
			else
			{
				sub_cur= 1;
				sub_start= index - 1;
			}
		}
		sub_end= sub_start + 3;
		for (int i= sub_start, j= 0; i < sub_end; i++, j++)
		{
			eventTop[j].setCepEvent(cep.getCepEvents().get(i));
		}//end for
		selectedEventTop.unSelected();
		eventTop[sub_cur].selected();
		selectedEventTop= eventTop[sub_cur];
		if (sub_start > 0)
		{
			mLeftArrow.setVisibility(View.VISIBLE);
		}
		else
		{
			mLeftArrow.setVisibility(View.INVISIBLE);
		}
		if (sub_end < event_length)
		{
			mRightArrow.setVisibility(View.VISIBLE);
		}
		else
		{
			mRightArrow.setVisibility(View.INVISIBLE);
		}
	}
	@Override
	public void onPause()
	{
		super.onPause();
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		mImageFetcher.closeCache();
		if (mTask != null)
		{
			mTask.cancel(true);
		}
		if (signTask != null)
		{
			signTask.cancel(true);
		}
	}
	private void init()
	{
		assetManager= getAssets();
		proDialog= new ProgressDialog(this);
		cepId= getIntent().getStringExtra(Config.CEP_ID);
		requestEventId= getIntent().getStringExtra(Config.CEP_EVENT_ID);
		mInflater= (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageFetcher= ImageFetcherFactory.genImageFetcher(this, R.drawable.ic_launcher);
		mainLayout= (RelativeLayout) findViewById(R.id.main);
		mWeiboBtn= (ImageButton) findViewById(R.id.weibo_btn);
		mWeiboBtn.setOnClickListener(new WeiboClick());
		mBackBtn= (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBackBtn.setOnClickListener(new BackClick());
		mGallery= (ViewPager) findViewById(R.id.gallery);
		mTitleText= (TextView) findViewById(R.id.detail_title);
		//		mContentText= (TextView) findViewById(R.id.cep_content);
		progress= (RelativeLayout) findViewById(R.id.progressLayout);
		eventHeaderView= findViewById(R.id.event_head);
		lineShadow= (ImageView) findViewById(R.id.col_shadow2);
		mCepEventGallery= (ViewPager) findViewById(R.id.cep_event_gallery);
		mLeftArrow= (ImageView) findViewById(R.id.img_arrow_left);
		mRightArrow= (ImageView) findViewById(R.id.img_arrow_right);
		eventTop[0]= (CepTextView) findViewById(R.id.cep_event_top1);
		eventTop[1]= (CepTextView) findViewById(R.id.cep_event_top2);
		eventTop[2]= (CepTextView) findViewById(R.id.cep_event_top3);
		typeImage= (ImageView) findViewById(R.id.cep_type);
		starsImg[0]= (ImageView) findViewById(R.id.starts_1);
		starsImg[1]= (ImageView) findViewById(R.id.starts_2);
		starsImg[2]= (ImageView) findViewById(R.id.starts_3);
		starsImg[3]= (ImageView) findViewById(R.id.starts_4);
		starsImg[4]= (ImageView) findViewById(R.id.starts_5);
	}
	private void setSorce()
	{
		int score= 0;
		try
		{
			score= Integer.parseInt(cep.getScore());
		}
		catch (Exception e)
		{
			score= 0;
		}
		System.out.println(score);
		for (int i= 0; i < score && i < STARS_NUM; i++)
		{
			starsImg[i].setImageResource(R.drawable.cep_stars_light);
		}//end for i
	}
	private class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			CepDetailActivity.this.finish();
		}
	}// end inner class
	/**
	 * @author Panyi
	 */
	private class ImagePagerAdapter extends FragmentStatePagerAdapter
	{
		private final List<String> picList;
		private final Cep itemCep;
		public ImagePagerAdapter(FragmentManager fm, List<String> galleryList, Cep itemCep)
		{
			super(fm);
			this.picList= galleryList;
			this.itemCep= itemCep;
		}
		@Override
		public Fragment getItem(int position)
		{
			return ImageDetailFragment.newInstance(picList.get(position), itemCep);
		}
		@Override
		public int getCount()
		{
			if (picList != null)
			{
				return picList.size();
			}
			else
			{
				return 0;
			}
		}
	}// end inner class
	/**
	 * 
	 * @author Panyi
	 * 
	 */
	private class CepEventAdapter extends FragmentStatePagerAdapter
	{
		private List<CepEvent> listCepEvent;
		public CepEventAdapter(FragmentManager fm, List<CepEvent> listCepEvent)
		{
			super(fm);
			this.listCepEvent= listCepEvent;
		}
		@Override
		public Fragment getItem(int index)
		{
			return CepEventItem.newInstance(listCepEvent.get(index), cep, index);
		}
		@Override
		public int getCount()
		{
			return listCepEvent.size();
		}
	}// end inner class
	private final class WeiboClick implements OnClickListener
	{
		@Override
		public void onClick(View arg0)
		{
			Intent it= new Intent(CepDetailActivity.this, ShareActivity.class);
			CepDetailActivity.this.startActivity(it);
		}
	}//end inner class
}// end class
