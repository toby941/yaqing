package com.airAd.yaqinghui;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.yaqinghui.business.CepService;
import com.airAd.yaqinghui.business.model.Cep;
import com.airAd.yaqinghui.business.model.CepEvent;
import com.airAd.yaqinghui.common.Config;
import com.airAd.yaqinghui.core.ImageFetcherFactory;
import com.airAd.yaqinghui.fragment.ImageDetailFragment;

/**
 * @author Panyi
 */
public class CepDetailActivity extends BaseActivity {
    private ImageButton mBackBtn;
    private ViewPager mGallery;
    private ViewPager mCepEventGallery;
    private ImageFetcher mImageFetcher;
    private TextView mTitleText;
    private TextView mContentText;
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
    private Cep cep;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        init();
        requestDetailData();
    }

    /**
     * @param service
     */
    private void requestDetailData() {
        mTask = new RequestTask();
        mTask.execute(cepId);
    }

    private final class RequestTask extends AsyncTask<String, Integer, Cep> {
        @Override
        protected Cep doInBackground(String... params) {
            return new CepService().getCep(params[0], MyApplication.getCurrentUser().getId());
        }

        @Override
        protected void onPostExecute(Cep result) {
            super.onPostExecute(result);
            cep = result;

            cep = new Cep();
            cep.setContent("监听HorizontalScrollView滑动到最左/最右- cigaretwine的专栏- 博客 ...监听HorizontalScrollView滑动到最左/最右- cigaretwine的专栏- 博客 ...监听HorizontalScrollView滑动到最左/最右- cigar"
                    + "etwine的专栏- 博客 ...监听HorizontalScrollView滑动到最左/最右- cigaretwine的专栏- 博客 ...");
            cep.setId("00001");
            cep.setJoinNum(100);
            cep.setPic("http://imgtuku.mingxing.com/upload/attach/2013/05/36797-3YaaYgV.jpg");
            cep.setPlace("菜市口");
            cep.setScore("123");
            cep.setSigupNum(10);
            cep.setTime("20131001");
            cep.setTitle("亚青会活动cep");

            List<CepEvent> cepEvents = new ArrayList<CepEvent>();
            CepEvent e1 = new CepEvent();
            e1.setCepId("00001");
            e1.setEndTime("20131002");
            e1.setFlag("1");
            e1.setId("00002");
            List<String> url = new ArrayList<String>();
            url.add("http://imgtuku.mingxing.com/upload/attach/2013/05/36797-3YaaYgV.jpg");
            e1.setPics(url);
            e1.setPlace("黄河大道");
            e1.setStartTime("201308151410");
            cepEvents.add(e1);
            cepEvents.add(e1);
            cepEvents.add(e1);
            cepEvents.add(e1);
            cep.setCepEvents(cepEvents);

            if (cep == null) {
                Toast.makeText(CepDetailActivity.this, R.string.net_exception, Toast.LENGTH_SHORT).show();
                return;
            }
            List<String> picList = new ArrayList<String>();
            picList.add(cep.getPic());
            mGallery.setAdapter(new ImagePagerAdapter(CepDetailActivity.this.getSupportFragmentManager(), picList));
            mTitleText.setText(cep.getTitle());
            mContentText.setText(cep.getContent());

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
        mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageFetcher = ImageFetcherFactory.genImageFetcher(this, R.drawable.ic_launcher);
        mainLayout = (RelativeLayout) findViewById(R.id.mainlayout);
        mBackBtn = (ImageButton) findViewById(R.id.main_banner_left_btn);
        mBackBtn.setOnClickListener(new BackClick());
        mGallery = (ViewPager) findViewById(R.id.gallery);
        mTitleText = (TextView) findViewById(R.id.detail_title);
        mContentText = (TextView) findViewById(R.id.cep_content);
        progress = (RelativeLayout) findViewById(R.id.progressLayout);
        mainScroll = (ScrollView) findViewById(R.id.main);
        mCepEventGallery = (ViewPager) findViewById(R.id.cep_event_gallery);
    }

    private class BackClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            CepDetailActivity.this.finish();
        }
    }// end inner class

    /**
     * @author Panyi
     */
    private class ImagePagerAdapter extends FragmentStatePagerAdapter {
        private final List<String> picList;

        public ImagePagerAdapter(FragmentManager fm, List<String> galleryList) {
            super(fm);
            this.picList = galleryList;
        }

        @Override
        public Fragment getItem(int position) {
            return ImageDetailFragment.newInstance(picList.get(position),
                    ImageFetcherFactory.genImageFetcher(CepDetailActivity.this));
        }

        @Override
        public int getCount() {
            if (picList != null) {
                return picList.size();
            }
            else {
                return 0;
            }
        }
    }// end inner class

    private void setPopView(String tips) {
        if (pop == null) {
            popLayoutView = mInflater.inflate(R.layout.select_time, null);
            pop = new PopupWindow(popLayoutView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            pop.setBackgroundDrawable(new BitmapDrawable());
            pop.setOutsideTouchable(true);
            pop.setFocusable(true);
            Button closeBtn = (Button) popLayoutView.findViewById(R.id.iknowBtn);
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
