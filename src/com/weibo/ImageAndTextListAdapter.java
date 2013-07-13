package com.weibo;

import java.util.List;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.core.ImageFetcherFactory;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ImageAndTextListAdapter extends ArrayAdapter<ImageAndText> {

	private ListView listView;
	private AsyncImageLoader asyncImageLoader;
	private ImageFetcher fetcher;

	public ImageAndTextListAdapter(FragmentActivity activity,
			List<ImageAndText> imageAndTexts, ListView listView) {
		super(activity, 0, imageAndTexts);
		this.listView = listView;
		asyncImageLoader = new AsyncImageLoader();
		fetcher = ImageFetcherFactory.genImageFetcher(activity);
		fetcher.setLoadingImage(R.drawable.icon);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		Activity activity = (Activity) getContext();

		// Inflate the views from XML
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			rowView = inflater.inflate(R.layout.weibo_list_item, null);
		}
		ImageAndText imageAndText = getItem(position);

		// Load the image and set it on the ImageView
		String imageUrl = imageAndText.getImageUrl();
		ImageView imageView = (ImageView) rowView.findViewById(R.id.ico);
		fetcher.loadImage(imageUrl, imageView);
		// Set the text on the TextView
		TextView textView = (TextView) rowView.findViewById(R.id.wb_name);

		textView.setText(imageAndText.getText());

		return rowView;
	}

}
