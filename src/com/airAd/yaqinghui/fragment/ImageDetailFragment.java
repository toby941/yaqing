package com.airAd.yaqinghui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.framework.worker.ImageWorker;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.business.model.Cep;

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
	public Cep cep;

	public static ImageDetailFragment newInstance(String imageUrl, Cep cep)
	{
		final ImageDetailFragment f = new ImageDetailFragment();
		final Bundle args = new Bundle();
		f.cep= cep;
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
			mImageView.setImageResource(Cep.getCepSmallPicRes(cep.getId()));
			//			if (HomeActivity.class.isInstance(getActivity())) {
			//				mImageFetcher = ((HomeActivity) getActivity()).getImageFetcher();
			//				mImageFetcher.loadImage(mImageUrl, mImageView);
			//				//				img.setImageResource(Cep.getCepBigPicRes(cep.getId()));
			//				mImageView.setImageResource(Cep.getCepSmallPicRes(cep.getId()));
			//			}
		}else{
			//			mImageFetcher.loadImage(mImageUrl, mImageView);
			mImageView.setImageResource(Cep.getCepSmallPicRes(cep.getId()));
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
