package com.airAd.yaqinghui;

import java.util.ArrayList;
import java.util.zip.Inflater;

import net.sf.json.JSONObject;

import com.airAd.framework.net.RemoteService;
import com.airAd.framework.net.Response;
import com.airAd.framework.worker.ImageFetcher;
import com.airAd.framework.worker.NetWorker;
import com.airAd.framework.worker.NetWorkerHandler;
import com.airAd.framework.worker.ImageCache.ImageCacheParams;
import com.airAd.yaqinghui.data.model.Comment;
import com.airAd.yaqinghui.data.model.NewsDetail;
import com.airAd.yaqinghui.data.model.PrecontractCancelCepActive;
import com.airAd.yaqinghui.data.model.PrecontractSignUp;
import com.airAd.yaqinghui.factory.HessianClient;
import com.airAd.yaqinghui.factory.ImageFetcherFactory;
import com.airAd.yaqinghui.fragment.ImageDetailFragment;
import com.airAd.yaqinghui.net.CepService;
import com.airAd.yaqinghui.net.NewsDetailService;
import com.airAd.yaqinghui.net.PrecontractService;
import com.airAd.yaqinghui.ui.IndexView;
import com.yogapppackage.BasicAPI;

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
public class NewsDetailActivity extends BaseActivity {
	private ImageButton mBackBtn;
	private ViewPager mGallery;
	private ImageFetcher mImageFetcher;
	private NewsDetail mDetail;
	private TextView mTimeText, mLocalText;
	private TextView mAbstractText, mTitleText;
	private IndexView mIndexView;
	private RelativeLayout progress;
	private LinearLayout mCommentLayout;
	private ScrollView mainScroll;
	private RequestTask mTask;
	private int commentY, preY = 0;
	private RelativeLayout mCommit;
	private Button mOrderBtn;
	private String cepId;// cep活动ID
	private LayoutInflater mInflater;
	private PopupWindow pop;
	private RelativeLayout mainLayout;
	private boolean isCancel = false;
	private ProgressDialog progressDialog;
	private PrecontractSignRequest signRequest;
	private PrecontractCancelRequest cancelRequest;
	private View popLayoutView;

	private CancelClick cancelClick;
	private OrderClick orderClick;

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
			JSONObject jsonObj=null;
			try{
				jsonObj = basicAPI.SelectTheOneCepActive(params[0],
						Config.CEP_USER_ID);
			}catch(Exception e){
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
				Toast.makeText(NewsDetailActivity.this, R.string.net_exception,
						Toast.LENGTH_SHORT).show();
				return;
			}
			mGallery.setAdapter(new ImagePagerAdapter(NewsDetailActivity.this
					.getSupportFragmentManager(), mDetail.getPicList()));
			mIndexView.setNum(mDetail.getPicList().size());
			mTimeText.setText(mDetail.getDate());
			mLocalText.setText(mDetail.getLocation());
			mTitleText.setText(mDetail.getTitle());
			mAbstractText.setText(mDetail.getContent());
			mGallery.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int index) {
					mIndexView.setPoint(index);
				}

				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}

				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});

			// 载入评论内容
			ArrayList<Comment> commentList = mDetail.getCommentList();
			if (commentList == null) {
				return;
			}
			for (int i = 0; i < commentList.size(); i++) {
				final Comment comment = commentList.get(i);
				View addView = mInflater.inflate(R.layout.comment_item, null);
				ImageView img = (ImageView) addView
						.findViewById(R.id.commentimg);
				TextView name = (TextView) addView
						.findViewById(R.id.commentname);
				TextView content = (TextView) addView
						.findViewById(R.id.commentcontent);
				TextView time = (TextView) addView
						.findViewById(R.id.commenttime);
				mImageFetcher.loadImage(comment.getPicurl(), img);
				name.setText(comment.getName());
				content.setText(comment.getContent());
				time.setText(comment.getTime() + "");

				mCommentLayout.addView(addView);
			}// end for i

			progress.setVisibility(View.GONE);
			mainScroll.setVisibility(View.VISIBLE);
			listenScrollView();
		}
	}// end inner class

	private void listenScrollView() {
		commentY = mCommentLayout.getTop();
		mainScroll.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					if (mainScroll.getScrollY() >= commentY) {
						mCommit.setVisibility(View.VISIBLE);
					} else {
						mCommit.setVisibility(View.GONE);
					}
				}
				return false;
			}
		});
	}

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
		if (signRequest != null) {
			signRequest.cancel(true);
		}
		if (cancelRequest != null) {
			cancelRequest.cancel(true);
		}
	}

	private void init() {
		cepId = getIntent().getStringExtra(Config.CEP_ID);
		mInflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageFetcher = ImageFetcherFactory.genImageFetcher(this,
				R.drawable.ic_launcher);
		mainLayout = (RelativeLayout) findViewById(R.id.mainlayout);
		mOrderBtn = (Button) findViewById(R.id.orderBtn);
		mBackBtn = (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBackBtn.setOnClickListener(new BackClick());
		mGallery = (ViewPager) findViewById(R.id.gallery);
		mTimeText = (TextView) findViewById(R.id.news_detail_time_text);
		mLocalText = (TextView) findViewById(R.id.news_detail_location_text);
		mTitleText = (TextView) findViewById(R.id.detail_title);
		mAbstractText = (TextView) findViewById(R.id.detail_content_abstract);
		mIndexView = (IndexView) findViewById(R.id.gallery_index);
		mainScroll = (ScrollView) findViewById(R.id.main);
		progress = (RelativeLayout) findViewById(R.id.progressLayout);
		mCommentLayout = (LinearLayout) findViewById(R.id.commentLayout);
		mCommit = (RelativeLayout) findViewById(R.id.commitBtn);
		mCommit.setOnClickListener(new OpenCommit());
		orderClick = new OrderClick();
		cancelClick = new CancelClick();
		mOrderBtn.setOnClickListener(orderClick);
	}

	private class OrderClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(NewsDetailActivity.this);
				progressDialog.setTitle(R.string.dialog_title);
				progressDialog.setMessage(getResources().getText(
						R.string.dialog_msg));
				progressDialog.setCancelable(true);
			}
			progressDialog.show();
			signRequest = new PrecontractSignRequest();
			signRequest.execute("");
		}
	}// end class

	private class CancelClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(NewsDetailActivity.this);
				progressDialog.setTitle(R.string.dialog_title);
				progressDialog.setMessage(getResources().getText(
						R.string.dialog_msg));
				progressDialog.setCancelable(true);
			}
			progressDialog.show();
			cancelRequest = new PrecontractCancelRequest();
			cancelRequest.execute("");
		}
	}// end class

	private class OpenCommit implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.putExtra(Config.CEP_ID, cepId);
			intent.setClass(NewsDetailActivity.this, CommitDialogActivity.class);
			NewsDetailActivity.this.startActivity(intent);
		}
	}// end class

	private class BackClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			NewsDetailActivity.this.finish();
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
			return ImageDetailFragment.newInstance(picList.get(position),
					ImageFetcherFactory
							.genImageFetcher(NewsDetailActivity.this));
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

	private final class PrecontractCancelRequest extends
			AsyncTask<String, Void, PrecontractCancelCepActive> {
		@Override
		protected PrecontractCancelCepActive doInBackground(String... params) {
			BasicAPI basicAPI = HessianClient.create();
			JSONObject jsonObj = basicAPI.PrecontractCancelCepActive(cepId,
					Config.CEP_USER_ID);
			return new PrecontractService().getCancel(jsonObj);
		}

		@Override
		protected void onPostExecute(PrecontractCancelCepActive result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result == null) {
				Toast.makeText(NewsDetailActivity.this, R.string.net_exception,
						Toast.LENGTH_SHORT).show();
				return;
			}
			setPopView(result.getText());
			// 取消成功 更改按钮响应事件为可预约'
			if (PrecontractCancelCepActive.CANCEL_SUCCESS
					.equalsIgnoreCase(result.getMark())) {
				mOrderBtn.setText(getString(R.string.order_attend));
				mOrderBtn.setOnClickListener(orderClick);
			}
		}
	}// end inner class

	private final class PrecontractSignRequest extends
			AsyncTask<String, Void, PrecontractSignUp> {
		@Override
		protected PrecontractSignUp doInBackground(String... params) {
			BasicAPI basicAPI = HessianClient.create();
			JSONObject jsonObj = basicAPI.PrecontractSignUpCepActive(cepId,
					Config.CEP_USER_ID);
			return new PrecontractService().getSignup(jsonObj);
		}

		@Override
		protected void onPostExecute(PrecontractSignUp result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result == null) {
				Toast.makeText(NewsDetailActivity.this, R.string.net_exception,
						Toast.LENGTH_SHORT).show();
				return;
			}
			setPopView(result.getText());
			// 预约成功 修改按钮事件为取消
			if (PrecontractSignUp.PRECONTRACT_SUCCESS.equalsIgnoreCase(result
					.getMark())) {
				MyApplication.getCurrentApp().push(new Object());

				mOrderBtn.setText(getString(R.string.order_cancel));
				mOrderBtn.setOnClickListener(cancelClick);
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
