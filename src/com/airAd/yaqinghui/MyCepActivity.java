package com.airAd.yaqinghui;
import java.io.IOException;
import java.util.List;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.airAd.yaqinghui.business.NotificationMessageService;
import com.airAd.yaqinghui.business.model.Cep;
import com.airAd.yaqinghui.business.model.NotificationMessage;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.common.ApiUtil;
import com.airAd.yaqinghui.common.Config;
import com.airAd.yaqinghui.fragment.UserFragment;
/**
 * CEP活动
 * 
 * @author Panyi
 */
public class MyCepActivity extends BaseActivity
{
	private User mUser;
	private ImageButton mBack;
	private ListView mListView;
	private ListAdapter listAdapter;
	private List<NotificationMessage> dataList;
	private int type;
	private AssetManager assetManager;
	private ListAdapter adapter;
	private ImageView boxImg;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycep);
		init();
	}
	private void init()
	{
		mUser= MyApplication.getCurrentUser();
		assetManager= getAssets();
		boxImg= (ImageView) findViewBy(R.id.box);
		mBack= (ImageButton) findViewById(R.id.main_banner_left_btn);
		mBack.setOnClickListener(new BackClick());
		type= getIntent().getIntExtra(UserFragment.MYCEP_TYPE, 0);
		mListView= (ListView) findViewBy(R.id.list);
		NotificationMessageService notifyService= new NotificationMessageService();
		dataList= notifyService.getMessagesByType(MyApplication.getCurrentApp().getUser().getId(), type);
		if (dataList != null)
		{
			if (dataList.size() > 0)
			{
				boxImg.setVisibility(View.INVISIBLE);
			}
			mListView.setFocusable(true);
			adapter= new ListAdapter();
			mListView.setAdapter(adapter);
		}
	}
	private final class ListAdapter extends BaseAdapter
	{
		private LayoutInflater mInflater;
		public ListAdapter()
		{
			mInflater= LayoutInflater.from(MyCepActivity.this);
		}
		@Override
		public int getCount()
		{
			return dataList.size();
		}
		@Override
		public Object getItem(int position)
		{
			return dataList.get(position);
		}
		@Override
		public long getItemId(int position)
		{
			return position;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			NotificationMessage data= dataList.get(position);
			if (convertView == null)
			{
				convertView= mInflater.inflate(R.layout.mycep_item, null);
			}
			ImageView iconImge= (ImageView) convertView.findViewById(R.id.icon);
			TextView title= (TextView) convertView.findViewById(R.id.title);
			TextView time= (TextView) convertView.findViewById(R.id.time);
			TextView place= (TextView) convertView.findViewById(R.id.place);
			ImageView typeImage= (ImageView) convertView.findViewById(R.id.typeicon);
			setTypeImg(typeImage);
			TextView status= (TextView) convertView.findViewById(R.id.status);
			TextView dotime= (TextView) convertView.findViewById(R.id.dotime);
			status.setText(data.getTitle());
			dotime.setText(ApiUtil.formatDate(data.getAddTimeStr()));
			title.setText(data.getCepTitle());
			time.setText(ApiUtil.formatDate(data.getCepStartTime()));
			place.setText(data.getCepPlace());
			try
			{
				iconImge.setImageBitmap(BitmapFactory.decodeStream(assetManager.open(Cep.getIconType(data.getCepId())
						+ ".png")));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			View item= convertView.findViewById(R.id.item_main);
			item.setOnClickListener(new ToDetailClick(data));
			return convertView;
		}
	}//end inner class

	private final class ToDetailClick implements OnClickListener
	{
		private NotificationMessage item;
		public ToDetailClick(NotificationMessage item)
		{
			this.item= item;
		}
		@Override
		public void onClick(View v)
		{
			Intent it= new Intent(MyCepActivity.this, CepDetailActivity.class);
			it.putExtra(Config.CEP_ID, item.getCepId());
			it.putExtra(Config.CEP_EVENT_ID, item.getEventId() + "");
			MyCepActivity.this.startActivity(it);
		}
	}//end inner class

	private void setTypeImg(ImageView img)
	{
		if (type == NotificationMessage.TYPE_CEPEVENT_SIGNUP_HIS)
		{
			img.setImageResource(R.drawable.my_cep_order);
		}
		else if (type == NotificationMessage.TYPE_CEPEVENT_CHECKIN_HIS)
		{
			img.setImageResource(R.drawable.my_cep_locate);
		}
		else if (type == NotificationMessage.TYPE_CEPEVENT_SCORE_HIS)
		{
			img.setImageResource(R.drawable.my_cep_comment);
		}
	}
	private final class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			MyCepActivity.this.finish();
		}
	}// end inner class
}// end class
