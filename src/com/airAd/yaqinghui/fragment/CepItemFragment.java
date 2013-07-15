package com.airAd.yaqinghui.fragment;
import java.io.IOException;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
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
public class CepItemFragment extends Fragment
{
	public static final int STARS_NUM= 5;
	private AssetManager assertManager;
	private Cep cep;
	private ImageFetcher mImageFetcher;
	private LinearLayout detailBtn;
	private ImageView[] starsImg= new ImageView[STARS_NUM];
	public static CepItemFragment newInstance(Cep data, ImageFetcher fetcher)
	{
		final CepItemFragment f= new CepItemFragment();
		f.mImageFetcher= fetcher;
		f.cep= data;
		return f;
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		assertManager= getActivity().getAssets();
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View v= inflater.inflate(R.layout.cep_item, container, false);
		TextView titleText= (TextView) v.findViewById(R.id.title);
		setSorce(v);
		ImageView img= (ImageView) v.findViewById(R.id.img);
		ImageView typeImage= (ImageView) v.findViewById(R.id.cep_type);
		View mainLayout= v.findViewById(R.id.item_main);
		//		typeImage.setImageResource(Common.getCepTypePic());
		try
		{
			typeImage.setImageBitmap(BitmapFactory.decodeStream(assertManager.open(cep.getIconType() + ".png")));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		mainLayout.setOnClickListener(new DetailBtn());
		titleText.setText(cep.getTitle());
		//mImageFetcher.loadImage(cep.getPic(), img);
		img.setImageResource(Cep.getCepBigPicRes(cep.getId()));
		return v;
	}

	private void setSorce(View view)
	{
		int score= 0;
		starsImg[0]= (ImageView) view.findViewById(R.id.starts_1);
		starsImg[1]= (ImageView) view.findViewById(R.id.starts_2);
		starsImg[2]= (ImageView) view.findViewById(R.id.starts_3);
		starsImg[3]= (ImageView) view.findViewById(R.id.starts_4);
		starsImg[4]= (ImageView) view.findViewById(R.id.starts_5);
		try
		{
			score= Integer.parseInt(cep.getScore());
		}
		catch (Exception e)
		{
			score= 0;
		}
		for (int i= 0; i < score && i < STARS_NUM; i++)
		{
			starsImg[i].setImageResource(R.drawable.cep_stars_light);
		}//end for i
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}
	private final class DetailBtn implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			Intent it= new Intent();
			it.setClass(getActivity(), CepDetailActivity.class);
			it.putExtra(Config.CEP_ID, cep.getId());
			getActivity().startActivity(it);
		}
	}//end inner class
}// end class
