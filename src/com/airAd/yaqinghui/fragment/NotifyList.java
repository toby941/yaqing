package com.airAd.yaqinghui.fragment;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.NotifyDetailActivity;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.business.NotificationMessageService;
import com.airAd.yaqinghui.business.model.NotificationMessage;
import com.airAd.yaqinghui.common.Common;
/**
 * 设置Fragment
 * 
 * @author Panyi
 */
public class NotifyList extends Fragment
{
	private ImageButton mBack;
	private ListView listView;
	private NotificationMessageService notifyService;
	private ItemSelectListener selectListener;
	private List<NotificationMessage> dataList;

	public static NotifyList newInstance()
	{
		NotifyList fragment= new NotifyList();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		notifyService= new NotificationMessageService();
		View view= inflater.inflate(R.layout.fragment_notify_list, container, false);
		mBack= (ImageButton) view.findViewById(R.id.back);
		mBack.setOnClickListener(new BackClick());
		listView= (ListView) view.findViewById(R.id.notify_list);

		return view;
	}

	@Override
	public void onResume()
	{
		super.onResume();
		dataList= notifyService.getNoficationMessages();
		selectListener= new ItemSelectListener();
		listView.setAdapter(new NotifyAdapter());
		listView.setOnItemClickListener(selectListener);
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
	}
	private final class BackClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			getActivity().getSupportFragmentManager().beginTransaction().hide(NotifyList.this).commit();
		}
	}//end inner class
	private final class NotifyAdapter extends BaseAdapter
	{
		private LayoutInflater mInflater;
		public NotifyAdapter()
		{
			mInflater= LayoutInflater.from(getActivity());
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
				convertView= mInflater.inflate(R.layout.notify_list_item, null);
			}
			TextView date= (TextView) convertView.findViewById(R.id.date);
			TextView title= (TextView) convertView.findViewById(R.id.title);
			TextView content= (TextView) convertView.findViewById(R.id.content);
			ImageView isReadImage= (ImageView) convertView.findViewById(R.id.isread);
			date.setText(Common.timeNotifyString(data.getAddTimel()));
			title.setText(data.getTitle());
			content.setText(data.getContent());
			isReadImage.setImageResource(data.getReadFlag() == NotificationMessage.READ
					? R.drawable.msg_read
					: R.drawable.msg_not_read);
			return convertView;
		}
	}//end inner class
	private final class ItemSelectListener implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3)
		{
			//			System.out.println(arg2);
			MyApplication.getCurrentApp().push(dataList.get(index));
			Intent it= new Intent(getActivity(), NotifyDetailActivity.class);
			it.putExtra("id", dataList.get(index).getCid());
			getActivity().startActivity(it);
		}
	}//end class
}// end class
