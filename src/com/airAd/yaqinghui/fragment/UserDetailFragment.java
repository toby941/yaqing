package com.airAd.yaqinghui.fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.yaqinghui.HomeActivity;
import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.SetThumbActivity;
import com.airAd.yaqinghui.data.model.ActivityItem;
import com.airAd.yaqinghui.data.model.User;
import com.airAd.yaqinghui.factory.ImageFetcherFactory;
import com.airAd.yaqinghui.ui.CustomViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 
 * @author Panyi
 * 
 */
public class UserDetailFragment extends Fragment {
	public static final int SELECTED_THUMBS=401;
	
	protected CustomViewPager mGallery;
	private ImageButton mBack;
	private ImageView thumb;
	
	private User mUser;
	protected ImageFetcher mFetcher;
	
	 /*拍照的照片存储位置*/  
    private static final File PHOTO_DIR =
    		new File(Environment.getExternalStorageDirectory() + "/dcim/Camera");  

	public static UserDetailFragment newInstance(CustomViewPager gallery,ImageFetcher imageFetcher) {
		final UserDetailFragment f = new UserDetailFragment();
		f.mGallery = gallery;
		f.mFetcher = imageFetcher;
		return f;
	}
	
	public ImageView getThumb(){
		return thumb;
	}

	private UserDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mUser = MyApplication.getCurrentApp().getUser();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.menu_lefts_userdetail,
				container, false);
		// TODO
		mBack = (ImageButton) view.findViewById(R.id.back);
		mBack.setOnClickListener(new BackClick());
		thumb = (ImageView) view.findViewById(R.id.snap);
		thumb.setImageBitmap(((HomeActivity)getActivity()).getThumbBitmap());
		thumb.setOnClickListener(new TakePhotoClick());// 拍照按钮
		
		if(mUser!=null){//载入个人信息
			ImageView countryImage =(ImageView)view.findViewById(R.id.detail_country_img);
			mFetcher.loadImage(mUser.getFlag(), countryImage);
			TextView nameText = (TextView)view.findViewById(R.id.detail_name);
			TextView genderText = (TextView)view.findViewById(R.id.detail_gender);
			
			TextView itemText = (TextView)view.findViewById(R.id.detail_attenditem_text);
			TextView itemTextEg = (TextView)view.findViewById(R.id.detail_attenditem_texteg);
			
			nameText.setText(mUser.getName());
			genderText.setText(mUser.getGender());
			itemText.setText(mUser.getTypes()[0]);
		}
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.gc();
	}

	private final class BackClick implements OnClickListener {// 返回第一页面
		@Override
		public void onClick(View v) {
			mGallery.setCurrentItem(0);
		}
	}// end inner class

	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * 拍照
	 * 
	 * @author Panyi
	 * 
	 */
	private final class TakePhotoClick implements OnClickListener {
		@Override
		public void onClick(View v) {
			getActivity().startActivity(new Intent(getActivity(),SetThumbActivity.class));
			
//			PHOTO_DIR.mkdirs();// 创建照片的存储目录  
//            File mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名  
//			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
//			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
//			intent.putExtra("name", mCurrentPhotoFile.getAbsolutePath());
//			getActivity().startActivityForResult(intent, SELECTED_THUMBS);
             
			// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			// intent.addCategory(Intent.CATEGORY_OPENABLE);
			// intent.setType("image/*");
			// getActivity().startActivityForResult(Intent.createChooser(intent,
			// "选择图片"), SELECTED_THUMBS);
		}
	}// end inner class

}// end class
