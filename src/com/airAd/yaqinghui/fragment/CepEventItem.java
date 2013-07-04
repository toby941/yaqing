package com.airAd.yaqinghui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.yaqinghui.CepDetailActivity;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.business.model.CepEvent;
import com.airAd.yaqinghui.common.Config;

/**
 * @author Panyi
 */
public class CepEventItem extends Fragment {
    private CepEvent item;
    private ImageFetcher mImageFetcher;

    public static CepEventItem newInstance(CepEvent data, ImageFetcher fetcher) {
        final CepEventItem f = new CepEventItem();
        f.mImageFetcher = fetcher;
        f.item = data;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.cep_item, container, false);
        // TextView titleText = (TextView) v.findViewById(R.id.title);
        // ImageView img = (ImageView) v.findViewById(R.id.img);
        // View mainLayout = v.findViewById(R.id.item_main);
        // mainLayout.setOnClickListener(new DetailBtn());
        // titleText.setText(item.getTitle());
        // mImageFetcher.loadImage(item.getPicUrl(), img);

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

    private final class DetailBtn implements OnClickListener {
        @Override
        public void onClick(View v) {
            Intent it = new Intent();
            it.setClass(getActivity(), CepDetailActivity.class);
            it.putExtra(Config.CEP_ID, item.getCepId());
            getActivity().startActivity(it);
        }
    }// end inner class
}// end class
