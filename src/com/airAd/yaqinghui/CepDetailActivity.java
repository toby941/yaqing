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
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import com.airAd.yaqinghui.fragment.CepEventItem;
import com.airAd.yaqinghui.fragment.ImageDetailFragment;
import com.airAd.yaqinghui.ui.CepTextView;
/**
 * 
 * @author Panyi
 * 
 */
public class CepDetailActivity extends BaseActivity
{
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
    private boolean isCancel= false;
    private ProgressDialog progressDialog;
    private CepEventAdapter mCepEventAdapter;
    private View popLayoutView;
    private Cep cep;
    public static final int EVENT_TIP_NUM= 3;
    private CepTextView[] eventTop= new CepTextView[EVENT_TIP_NUM];
    private CepTextView selectedEventTop;
    private int sub_cur;
    private int sub_start, sub_end;
    private int event_length;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);
        init();
        requestDetailData();
    }
    /**
     * 
     * @param service
     */
    private void requestDetailData()
    {
        mTask= new RequestTask();
        mTask.execute(cepId);
    }
    private final class RequestTask extends AsyncTask<String, Integer, Cep>
    {
        @Override
        protected Cep doInBackground(String... params)
        {
            return new CepService().getCep(params[0]);
        }
        @Override
        protected void onPostExecute(Cep result)
        {
            super.onPostExecute(result);
            cep= result;
            cep= new Cep();
            cep.setContent("为天地立心,为生民立命,为往圣继绝学,为天下开万世太平。爱因斯坦说过“窗外的每一片树叶，都使人类的科学显得那么幼稚无力  “不理睬是最大的轻蔑。”“成吉思汗的骑兵，攻击速度与二十世纪的装甲部队相当；北宋的床弩，射程达一千五百米，与二十世纪的狙击步枪差不多；但这些仍不过是古代的骑兵与弓弩而已，不可能与现代力量抗衡。基础理论决定一切，未来史学派清楚地看到了这一点。而你们，却被回光返照的低级技术蒙住了眼睛。你们躺在现代文明的温床中安于享乐，对即将到来的决定人类命运的终极决战完全没有精神上的准备。”  ”");
            cep.setId("00001");
            cep.setPic("http://imgtuku.mingxing.com/upload/attach/2013/05/36797-3YaaYgV.jpg");
            cep.setPlace("菜市口");
            cep.setScore("123");
            cep.setTitle("亚青会活动cep");
            List<CepEvent> cepEvents= new ArrayList<CepEvent>();
            CepEvent e1= new CepEvent();
            e1.setCepId("00001");
            e1.setEndTime("20131002");
            e1.setFlag("1");
            e1.setId("00002");
            e1.setName("第一场");
            e1.setPlace("黄河大道1");
            e1.setStartTime("201308151410");
            CepEvent e2= new CepEvent();
            e2.setCepId("00001");
            e2.setEndTime("20131002");
            e2.setFlag("1");
            e2.setId("00002");
            e2.setName("第二场");
            e2.setPlace("黄河大道2");
            e2.setStartTime("201308151410");
            CepEvent e3= new CepEvent();
            e3.setCepId("00001");
            e3.setEndTime("20131002");
            e3.setFlag("1");
            e3.setId("00002");
            e3.setName("第三场");
            e3.setPlace("黄河大道3");
            e3.setStartTime("201308151410");
            CepEvent e4= new CepEvent();
            e4.setCepId("00001");
            e4.setEndTime("20131002");
            e4.setFlag("1");
            e4.setId("00002");
            e4.setName("第四场");
            e4.setPlace("黄河大道4");
            e4.setStartTime("201308151410");
            CepEvent e5= new CepEvent();
            e5.setCepId("00001");
            e5.setEndTime("20131002");
            e5.setFlag("1");
            e5.setId("00002");
            e5.setName("第五场");
            e5.setPlace("黄河大道5");
            e5.setStartTime("201308151410");
            CepEvent e6= new CepEvent();
            e6.setCepId("00001");
            e6.setEndTime("20131002");
            e6.setFlag("1");
            e6.setId("00002");
            e6.setName("第六场");
            e6.setPlace("黄河大道6");
            e6.setStartTime("201308151410");
            cepEvents.add(e1);
            cepEvents.add(e2);
            cepEvents.add(e3);
            cepEvents.add(e4);
            cepEvents.add(e5);
            cepEvents.add(e6);
            cep.setCepEvents(cepEvents);
            if (cep == null)
            {
                Toast.makeText(CepDetailActivity.this, R.string.net_exception, Toast.LENGTH_SHORT).show();
                return;
            }
            List<String> picList= new ArrayList<String>();
            picList.add(cep.getPic());
            mGallery.setAdapter(new ImagePagerAdapter(CepDetailActivity.this.getSupportFragmentManager(), picList));
            mTitleText.setText(cep.getTitle());
            mContentText.setText(cep.getContent());
            mCepEventGallery.setAdapter(new CepEventAdapter(CepDetailActivity.this.getSupportFragmentManager(), cep
                    .getCepEvents()));
            setEventComponent();
            progress.setVisibility(View.GONE);
            mainScroll.setVisibility(View.VISIBLE);
        }
    }// end inner class
    private void setEventComponent()
    {
        //TODO
        if (cep.getCepEvents().size() == 1)
 {// 仅有一场活动
        }
        else if (cep.getCepEvents().size() == 2)
        {
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
                    //                  eventToIndex(sub_start);
                }
            });
            eventTop[1].setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mCepEventGallery.setCurrentItem(sub_start + 1);
                    //                  eventToIndex(sub_start + 1);
                }
            });
            eventTop[2].setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mCepEventGallery.setCurrentItem((sub_start + 2));
                    //                  eventToIndex(sub_start + 2);
                }
            });
        }
    }
    private void eventToIndex(int index)
    {
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
    }
    private void init()
    {
        cepId= getIntent().getStringExtra(Config.CEP_ID);
        mInflater= (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageFetcher= ImageFetcherFactory.genImageFetcher(this, R.drawable.ic_launcher);
        mainLayout= (RelativeLayout) findViewById(R.id.mainlayout);
        mBackBtn= (ImageButton) findViewById(R.id.main_banner_left_btn);
        mBackBtn.setOnClickListener(new BackClick());
        mGallery= (ViewPager) findViewById(R.id.gallery);
        mTitleText= (TextView) findViewById(R.id.detail_title);
        mContentText= (TextView) findViewById(R.id.cep_content);
        progress= (RelativeLayout) findViewById(R.id.progressLayout);
        mainScroll= (ScrollView) findViewById(R.id.main);
        mCepEventGallery= (ViewPager) findViewById(R.id.cep_event_gallery);
        eventTop[0]= (CepTextView) findViewById(R.id.cep_event_top1);
        eventTop[1]= (CepTextView) findViewById(R.id.cep_event_top2);
        eventTop[2]= (CepTextView) findViewById(R.id.cep_event_top3);
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
        public ImagePagerAdapter(FragmentManager fm, List<String> galleryList)
        {
            super(fm);
            this.picList= galleryList;
        }
        @Override
        public Fragment getItem(int position)
        {
            return ImageDetailFragment.newInstance(
                    picList.get(position),
                    ImageFetcherFactory.genImageFetcher(CepDetailActivity.this));
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
            return CepEventItem.newInstance(listCepEvent.get(index));
        }
        @Override
        public int getCount()
        {
            return listCepEvent.size();
        }
    }// end inner class
    private void setPopView(String tips)
    {
        if (pop == null)
        {
            popLayoutView= mInflater.inflate(R.layout.select_time, null);
            pop= new PopupWindow(popLayoutView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            pop.setBackgroundDrawable(new BitmapDrawable());
            pop.setOutsideTouchable(true);
            pop.setFocusable(true);
            Button closeBtn= (Button) popLayoutView.findViewById(R.id.iknowBtn);
            closeBtn.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    pop.dismiss();
                }
            });
        }
        pop.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        TextView text= (TextView) popLayoutView.findViewById(R.id.tips);
        text.setText(tips);
    }
}// end class
