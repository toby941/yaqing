package com.airAd.yaqinghui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.yaqinghui.CepDetailActivity;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.business.model.Cep;
import com.airAd.yaqinghui.common.Config;

/**
 * 
 * @author Panyi
 * 
 */
public class CepItemFragment extends Fragment {
    private Cep cep;
	private ImageFetcher mImageFetcher;
	
	private LinearLayout detailBtn;
	
    public static CepItemFragment newInstance(Cep data, ImageFetcher fetcher) {
		final CepItemFragment f = new CepItemFragment();
		f.mImageFetcher = fetcher;
		f.cep = data;
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
		TextView titleText =  (TextView) v.findViewById(R.id.title);
		ImageView img = (ImageView) v.findViewById(R.id.img);
		View mainLayout  = v.findViewById(R.id.item_main);
		mainLayout.setOnClickListener(new DetailBtn());
		
		titleText.setText(cep.getTitle());
        mImageFetcher.loadImage(cep.getPic(), img);
		
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
			Intent it=new Intent();
			it.setClass(getActivity(), CepDetailActivity.class);
            it.putExtra(Config.CEP_ID, cep.getId());
			getActivity().startActivity(it);
		}
	}//end inner class
}// end class
