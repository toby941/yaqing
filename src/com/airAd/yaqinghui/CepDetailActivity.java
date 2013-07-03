package com.airAd.yaqinghui;

import java.util.ArrayList;
import java.util.Locale;
import java.util.zip.Inflater;

import net.sf.json.JSONObject;

import com.airAd.framework.net.RemoteService;
import com.airAd.framework.net.Response;
import com.airAd.framework.worker.ImageFetcher;
import com.airAd.framework.worker.NetWorker;
import com.airAd.framework.worker.NetWorkerHandler;
import com.airAd.framework.worker.ImageCache.ImageCacheParams;
import com.airAd.yaqinghui.api.BasicAPI;
import com.airAd.yaqinghui.data.model.Comment;
import com.airAd.yaqinghui.data.model.NewsDetail;
import com.airAd.yaqinghui.data.model.PrecontractCancelCepActive;
import com.airAd.yaqinghui.data.model.PrecontractSignUp;
import com.airAd.yaqinghui.data.model.User;
import com.airAd.yaqinghui.factory.HessianClient;
import com.airAd.yaqinghui.factory.ImageFetcherFactory;
import com.airAd.yaqinghui.fragment.ImageDetailFragment;
import com.airAd.yaqinghui.net.CepService;
import com.airAd.yaqinghui.net.NewsDetailService;
import com.airAd.yaqinghui.net.PrecontractService;
import com.airAd.yaqinghui.ui.IndexView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Panyi
 * 
 */
public class CepDetailActivity extends BaseActivity {
	private ImageButton mBackBtn;
	private ViewPager mGallery;
	private ImageFetcher mImageFetcher;
	private NewsDetail mDetail;
	private TextView mTitleText;
	private RelativeLayout progress;
	private ScrollView mainScroll;
	private RequestTask mTask;
	private String cepId;// cep活动ID
	private LayoutInflater mInflater;
	private PopupWindow pop;
	private RelativeLayout mainLayout;
	private boolean isCancel = false;
	private ProgressDialog progressDialog;

	private View popLayoutView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_detail);
		init();
		requestDetailData();
	}

	/**
	 * 
	 * @param service
	 */
	private void requestDetailData() {
		mTask = new RequestTask();
		mTask.execute(cepId);
	}

	private final class RequestTask extends
			AsyncTask<String, Integer, NewsDetail> {
		@Override
		protected NewsDetail doInBackground(String... params) {
			BasicAPI basicAPI = HessianClient.create();
			JSONObject jsonObj = null;
			try {
				jsonObj = basicAPI.SelectTheOneCepActive(params[0],
						MyApplication.getCurrentApp().getUser().getId(),
						User.getLan());
			} catch (Exception e) {
				e.printStackTrace();
				jsonObj = null;
			}

			return new NewsDetailService().getItem(jsonObj);
		}

		@Override
		protected void onPostExecute(NewsDetail result) {
			super.onPostExecute(result);
			mDetail = result;
			if (result == null) {
				Toast.makeText(CepDetailActivity.this, R.string.net_exception,
						Toast.LENGTH_SHORT).show();
				return;
			}
			mGallery.setAdapter(new ImagePagerAdapter(CepDetailActivity.this
					.getSupportFragmentManager(), mDetail.getPicList()));
			mTitleText.setText(mDetail.getTitle());
			// 载入评论内容
			progress.setVisibility(View.GONE);
			mainScroll.setVisibility(View.VISIBLE);
		}
	}// end inner class

	@Override
	public void onPause() {
		super.onPause();
		mImageFetcher.setExitTasksEarly(true);
		mImageFetcher.flushCache();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mImageFetcher.closeCache();
		if (mTask != null) {
			mTask.cancel(true);
		}
	}

	private void init() {
		cepId = getIntent().getStringExtra(Config.CEP_ID);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageFetcher = ImageFetcherFactory.genImageFetcher(this,
				R.drawable.ic_launcher);
		mainLayout = (RelativeLayout) findViewById(R.id.mainlayout);
		mBackBtn = (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBackBtn.setOnClickListener(new BackClick());
		mGallery = (ViewPager) findViewById(R.id.gallery);
		mTitleText = (TextView) findViewById(R.id.detail_title);
		progress = (RelativeLayout) findViewById(R.id.progressLayout);
		mainScroll = (ScrollView)findViewById(R.id.main);
	}

	private class BackClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			CepDetailActivity.this.finish();
		}
	}// end inner class

	/**
	 * 
	 * @author Panyi
	 * 
	 */
	private class ImagePagerAdapter extends FragmentStatePagerAdapter {
		private final ArrayList<String> picList;

		public ImagePagerAdapter(FragmentManager fm,
				ArrayList<String> galleryList) {
			super(fm);
			this.picList = galleryList;
		}

		@Override
		public Fragment getItem(int position) {
			return ImageDetailFragment
					.newInstance(picList.get(position), ImageFetcherFactory
							.genImageFetcher(CepDetailActivity.this));
		}

		@Override
		public int getCount() {
			if (picList != null) {
				return picList.size();
			} else {
				return 0;
			}
		}
	}// end inner class

	private void setPopView(String tips) {
		if (pop == null) {
			popLayoutView = mInflater.inflate(R.layout.select_time, null);
			pop = new PopupWindow(popLayoutView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			pop.setBackgroundDrawable(new BitmapDrawable());
			pop.setOutsideTouchable(true);
			pop.setFocusable(true);
			Button closeBtn = (Button) popLayoutView
					.findViewById(R.id.iknowBtn);
			closeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pop.dismiss();
				}
			});
		}
		pop.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
		TextView text = (TextView) popLayoutView.findViewById(R.id.tips);
		text.setText(tips);
	}
}// end class
