package com.airAd.yaqinghui;

import java.util.ArrayList;
import java.util.Locale;

import net.sf.json.JSONObject;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.framework.worker.NetWorker;
import com.airAd.yaqinghui.business.CepService;
import com.airAd.yaqinghui.business.api.BasicAPI;
import com.airAd.yaqinghui.business.model.CepItem;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.core.HessianClient;
import com.airAd.yaqinghui.core.ImageFetcherFactory;
import com.airAd.yaqinghui.fragment.CepItemFragment;
import com.airAd.yaqinghui.ui.IndexView;

/**
 * CEP活动
 * 
 * @author Panyi
 */
public class CepActivity extends BaseActivity {
    private ImageButton mBack;
    private ImageButton mShakeBtn;
    private ViewPager mGallery;
    private ImageFetcher mImageFetcher;
    private RequestTask mTask;
    private ArrayList<CepItem> itemList;
    private IndexView mIndexView;
    private ProgressDialog progressDialog;
    
    private User mUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cep);
        init();
    }

    private void init() {
    	mUser = MyApplication.getCurrentApp().getUser();
        mImageFetcher = ImageFetcherFactory.genImageFetcher(this);
        mBack = (ImageButton) findViewById(R.id.main_banner_left_btn);
        mBack.setOnClickListener(new BackClick());
        mGallery = (ViewPager) findViewById(R.id.gallery);
        mIndexView = (IndexView) findViewById(R.id.gallery_index);
        request();
    }

    private void request() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.dialog_title);
        progressDialog.setMessage(getResources().getText(R.string.dialog_msg));
        progressDialog.setCancelable(true);
        progressDialog.show();
        mTask = new RequestTask();
        mTask.execute(0);
    }

    private final class PageAdapter extends FragmentStatePagerAdapter {
        public PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            return CepItemFragment.newInstance(itemList.get(index), mImageFetcher);
        }

        @Override
        public int getCount() {
            return itemList.size();
        }
    }// end inner class

    private final class BackClick implements OnClickListener {
        @Override
        public void onClick(View v) {
            CepActivity.this.finish();
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

    private final class RequestTask extends AsyncTask<Integer, Integer, ArrayList<CepItem>> {
      @Override
		protected ArrayList<CepItem> doInBackground(Integer... params) {
			BasicAPI basicAPI = HessianClient.create();
			JSONObject jsonObj;
			try{
				jsonObj = basicAPI.SelectAllCepActive(mUser.getId(),Locale.getDefault().getLanguage());
			}catch(Exception e){
				return null;
			}
			return new CepService().getCepList(jsonObj);
		}

        @Override
        protected void onPostExecute(ArrayList<CepItem> result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            itemList = result;
            if (itemList == null) {
                Toast.makeText(CepActivity.this, R.string.net_exception, Toast.LENGTH_SHORT).show();
                return;
            }
            mIndexView.setNum(itemList.size(), mIndexView.getMeasuredWidth());
            mGallery.setAdapter(new PageAdapter(getSupportFragmentManager()));
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
        }
    }// end inner class
}// end class
