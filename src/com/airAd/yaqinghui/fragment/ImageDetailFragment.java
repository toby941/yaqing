package com.airAd.yaqinghui.fragment;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.framework.worker.ImageWorker;
import com.airAd.yaqinghui.HomeActivity;
import com.airAd.yaqinghui.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * չʾͼƬ��Fragment
 * 
 * @author Panyi
 * 
 */
public class ImageDetailFragment extends Fragment {
	private static final String IMAGE_DATA_EXTRA = "extra_image_data";
	private String mImageUrl;
	private ImageView mImageView;
	public ImageFetcher mImageFetcher;

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();
		final Bundle args = new Bundle();
		args.putString(IMAGE_DATA_EXTRA, imageUrl);
		f.setArguments(args);
		return f;
	}
	
	public static ImageDetailFragment newInstance(String imageUrl,ImageFetcher fetcher) {
		final ImageDetailFragment f = new ImageDetailFragment();
		final Bundle args = new Bundle();
		args.putString(IMAGE_DATA_EXTRA, imageUrl);
		f.setArguments(args);
		f.mImageFetcher=fetcher;
		return f;
	}

	public ImageDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mImageUrl = getArguments() != null ? getArguments().getString(
				IMAGE_DATA_EXTRA) : null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment,
				container, false);
		mImageView = (ImageView) v.findViewById(R.id.imageView);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(mImageFetcher==null){
			if (HomeActivity.class.isInstance(getActivity())) {
				mImageFetcher = ((HomeActivity) getActivity()).getImageFetcher();
				mImageFetcher.loadImage(mImageUrl, mImageView);
			}
		}else{
			mImageFetcher.loadImage(mImageUrl, mImageView);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mImageView != null) {
			ImageWorker.cancelWork(mImageView);
			mImageView.setImageDrawable(null);
		}
	}
}
