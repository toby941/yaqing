package com.airAd.yaqinghui.fragment;

import java.util.ArrayList;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.framework.worker.ImageCache.ImageCacheParams;
import com.airAd.yaqinghui.Config;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.data.model.ActivityItem;
import com.airAd.yaqinghui.factory.ImageFetcherFactory;

import android.content.Context;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * չʾͼƬ��Fragment
 * 
 * @author Panyi
 * 
 */
public class NotifyListFragment extends ListFragment {
	private static final String IMAGE_DATA_EXTRA = "extra_image_data";
	private ArrayList<ActivityItem> itemList;
	private ImageFetcher mImageFetcher;

	public static NotifyListFragment newInstance(
			ArrayList<ActivityItem> itemList, ImageFetcher fetcher) {
		final NotifyListFragment f = new NotifyListFragment();
		//f.mImageFetcher = fetcher;
		f.itemList = itemList;
		return f;
	}

	private NotifyListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageFetcher = ImageFetcherFactory.genImageFetcher(getActivity(),
				R.drawable.ic_launcher);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.fragment_attendlist,
				container, false);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadList();
	}

	private void loadList() {
		if (itemList == null) {
			return;
		}
		setListAdapter(new ItemAdapter());
	}

	@Override
	public void onResume() {
		super.onResume();
		mImageFetcher.setExitTasksEarly(false);
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
		System.gc();
	}

	private class ItemAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public ItemAdapter() {
			mInflater = (LayoutInflater) getActivity().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			return itemList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.attend_list_item, null);
			}
			ActivityItem item = itemList.get(position);
			ImageView statusImg = (ImageView) convertView
					.findViewById(R.id.status_img);
			TextView statusText = (TextView) convertView
					.findViewById(R.id.status_text);
			if (item.getStatus() == ActivityItem.ACTIVITY_STATUS_APPLYING) {
				statusImg.setImageResource(R.drawable.joining_icon);
				statusText.setText(R.string.applying);
			} else if (item.getStatus() == ActivityItem.ACTIVITY_STATUS_HASATTEND) {
				statusImg.setImageResource(R.drawable.joined_icon);
				statusText.setText(R.string.has_attend);
			}
			ImageView newImg = (ImageView) convertView
					.findViewById(R.id.news_img);
			mImageFetcher.loadImage(item.getPicurl(), newImg);
			TextView timeText = (TextView) convertView.findViewById(R.id.time);
			timeText.setText(item.getTime());
			TextView titleText = (TextView) convertView
					.findViewById(R.id.news_title);
			titleText.setText(item.getTitle());
			TextView sTitleText = (TextView) convertView
					.findViewById(R.id.news_stitle);
			sTitleText.setText(item.getTitle());
			TextView newsTime = (TextView) convertView
					.findViewById(R.id.news_time);
			newsTime.setText(item.getTime());
			TextView newsLocation = (TextView) convertView
					.findViewById(R.id.news_location);
			newsLocation.setText(item.getLocation());
			return convertView;
		}
	}
}
