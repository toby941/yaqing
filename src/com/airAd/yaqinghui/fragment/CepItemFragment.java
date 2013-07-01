package com.airAd.yaqinghui.fragment;

import java.util.ArrayList;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.yaqinghui.Config;
import com.airAd.yaqinghui.NewsDetailActivity;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.data.model.CepItem;
import com.airAd.yaqinghui.factory.ImageFetcherFactory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * CEPÃ¿Ïî
 * 
 * @author Panyi
 * 
 */
public class CepItemFragment extends Fragment {
	private CepItem item;
	private ImageFetcher mImageFetcher;
	
	private LinearLayout detailBtn;
	
	public static CepItemFragment newInstance(CepItem data, ImageFetcher fetcher) {
		final CepItemFragment f = new CepItemFragment();
		f.mImageFetcher = fetcher;
		f.item = data;
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.cep_item, container, false);
		TextView attendText = (TextView) v.findViewById(R.id.cep_text_attend);
		TextView commitText = (TextView) v.findViewById(R.id.cep_text_commit);
		TextView titleText =  (TextView) v.findViewById(R.id.title);
		TextView contentText =  (TextView) v.findViewById(R.id.content);
		ImageView img = (ImageView) v.findViewById(R.id.img);
		detailBtn = (LinearLayout)v.findViewById(R.id.newsDetail);
		detailBtn.setOnClickListener(new DetailBtn());
		
		titleText.setText(item.getTitle());
		contentText.setText(item.getTips());
		attendText.setText(item.getAttendNum());
		commitText.setText(item.getCommentNum());
		mImageFetcher.loadImage(item.getPicUrl(), img);
		
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private final class DetailBtn implements OnClickListener{
		@Override
		public void onClick(View v) {
			//NewsDetailActivity
			Intent it=new Intent();
			it.setClass(getActivity(), NewsDetailActivity.class);
			it.putExtra(Config.CEP_ID, item.getCepId());
			getActivity().startActivity(it);
		}
	}//end inner class
}// end class
