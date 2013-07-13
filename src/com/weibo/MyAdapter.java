package com.weibo;

import java.util.List;

import com.airAd.yaqinghui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter implements SectionIndexer{

	private List<ImageAndText> list = null;
	private Context mContext;
	private SectionIndexer mIndexer;
	
	public MyAdapter(Context mContext, List<ImageAndText> list) {
		this.mContext = mContext;
		this.list = list;

	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.weibo_list_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.wb_name);
		//	viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.tvIco = (ImageView) view.findViewById(R.id.ico);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		final ImageAndText mContent = list.get(position);
		if (position == 0) {
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(mContent.getLetter());
		} else {
			String lastCatalog = list.get(position - 1).getLetter();
			if (mContent.getLetter().equals(lastCatalog)) {
				viewHolder.tvLetter.setVisibility(View.GONE);
			} else {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(mContent.getLetter());
			}
		}
	
		viewHolder.tvTitle.setText(this.list.get(position).getText());
		//viewHolder.tvIco.setImageBitmap(this.list.get(position).getImageUrl());
		
		return view;

	}
	


	final static class ViewHolder {
		ImageView tvIco;
		TextView tvTitle;
		TextView tvLetter;
	}


	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSectionForPosition(int position) {
		
		return 0;
	}

	public int getPositionForSection(int section) {
		ImageAndText mContent;
		String l;
		if (section == '!') {
			return 0;
		} else {
			for (int i = 0; i < getCount(); i++) {
				mContent = (ImageAndText) list.get(i);
				l = mContent.getLetter();
				char firstChar = l.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i + 1;
				}

			}
		}
		mContent = null;
		l = null;
		return -1;
	}
}