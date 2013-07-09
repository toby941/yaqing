package com.airAd.yaqinghui.fragment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.airAd.framework.worker.ImageFetcher;
import com.airAd.yaqinghui.HomeActivity;
import com.airAd.yaqinghui.MyApplication;
import com.airAd.yaqinghui.R;
import com.airAd.yaqinghui.business.model.User;
import com.airAd.yaqinghui.common.Config;
import com.airAd.yaqinghui.common.FileUtils;
import com.airAd.yaqinghui.common.PicProcessUtils;
import com.airAd.yaqinghui.ui.CustomViewPager;
/**
 * 
 * @author Panyi
 * 
 */
public class UserDetailFragment extends Fragment
{
	public static final int SELECTED_THUMBS= 401;
	public static final String PATH= android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
			+ Config.FOLDER + "/";// 路径
	public static final String FILENAME= "mythumb.png"; // 照片名称
	public static final int TAKE_PHOTO= 1;// 拍照
	public static final int SELECT_GALLERY= 2;// 相册中选择
	public static final int TAKE_CROP= 3;// 设置剪裁
	private Button mTakePhoto;
	private Button mSelectPic;
	private ImageView mPreView;// 预览图
	private Bitmap photo;
	protected File tempFile= new File(Environment.getExternalStorageDirectory(), getPhotoFileName());
	protected CustomViewPager mGallery;
	private ImageButton mBack;
	private ImageView thumb;
	private User mUser;
	protected ImageFetcher mFetcher;
	private View parentView;
	private PopupWindow popWindow;//弹出框
	private PopupWindow confirmWindow;//确认头像框
	private View popContentView;
	private View confirmContentView;//确认框内容
	private Button mConfirmSet;
	private Button mCancelSet;
	private AssetManager assertManager;
	/* 拍照的照片存储位置 */
	private static final File PHOTO_DIR= new File(Environment.getExternalStorageDirectory() + "/dcim/Camera");
	public static UserDetailFragment newInstance(CustomViewPager gallery, ImageFetcher imageFetcher)
	{
		final UserDetailFragment f= new UserDetailFragment();
		f.mGallery= gallery;
		f.mFetcher= imageFetcher;
		return f;
	}
	public ImageView getThumb()
	{
		return thumb;
	}
	private UserDetailFragment()
	{
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		mUser= MyApplication.getCurrentUser();
		assertManager= getActivity().getAssets();
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		final View view= inflater.inflate(R.layout.menu_lefts_userdetail, container, false);
		// TODO
		parentView= view.findViewById(R.id.main);
		setPopWindow(view, inflater);
		setConfirmWindow(view, inflater);
		mBack= (ImageButton) view.findViewById(R.id.back);
		mBack.setOnClickListener(new BackClick());
		thumb= (ImageView) view.findViewById(R.id.snap);
		thumb.setImageBitmap(((HomeActivity) getActivity()).getThumbBitmap());
		thumb.setOnClickListener(new SetThumbClick());// 拍照按钮
		if (mUser != null)
		{// 载入个人信息
			ImageView countryImage= (ImageView) view.findViewById(R.id.detail_country_img);
			mFetcher.loadImage(mUser.getFlag(), countryImage);
			TextView nameText= (TextView) view.findViewById(R.id.detail_name);
			TextView genderText= (TextView) view.findViewById(R.id.detail_gender);
			TextView itemText= (TextView) view.findViewById(R.id.detail_attenditem_text);
			TextView itemTextEg= (TextView) view.findViewById(R.id.detail_attenditem_texteg);
			ImageView itemIcon= (ImageView) view.findViewById(R.id.detail_attenditem_icon);
			nameText.setText(mUser.getName());
			genderText.setText(mUser.getGender());
			itemText.setText(mUser.getTypes()[0]);
			try
			{
				itemIcon.setImageBitmap(BitmapFactory.decodeStream(assertManager.open(mUser.getTypes()[0].toLowerCase()
						+ ".png")));
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return view;
	}
	private void setPopWindow(View view, LayoutInflater inflater)
	{
		popContentView= inflater.inflate(R.layout.set_thumb_pop, null);
		popWindow= new PopupWindow(popContentView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		popWindow.setFocusable(true);
		popWindow.setBackgroundDrawable(new BitmapDrawable());
		popWindow.setAnimationStyle(R.style.PopupAnimation);
		mTakePhoto= (Button) popContentView.findViewById(R.id.take_photo_btn);
		mSelectPic= (Button) popContentView.findViewById(R.id.select_albume_btn);
		mTakePhoto.setOnClickListener(new TakePhotoClick());
		mSelectPic.setOnClickListener(new SelectfromGallery());
	}
	/**
	 * 设置确认头像框
	 * @param view
	 * @param inflater
	 */
	private void setConfirmWindow(View view, LayoutInflater inflater)
	{
		confirmContentView= inflater.inflate(R.layout.confirm_thumb_pop, null);
		confirmWindow= new PopupWindow(confirmContentView, ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		confirmWindow.setFocusable(true);
		//		confirmWindow.setBackgroundDrawable(new BitmapDrawable());
		confirmWindow.setAnimationStyle(R.style.PopupAnimation);
		mConfirmSet= (Button) confirmContentView.findViewById(R.id.confirm_thumb_btn);
		mCancelSet= (Button) confirmContentView.findViewById(R.id.cancel_thumb_btn);
		mPreView= (ImageView) confirmContentView.findViewById(R.id.pre_image);
		mConfirmSet.setOnClickListener(new ConfirmThumb());
		mCancelSet.setOnClickListener(new mCancelSet());
	}
	/**
	 * 确认使用当前头像
	 * @author Administrator
	 *
	 */
	private final class ConfirmThumb implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if (photo == null)
			{
				return;
			}
			try
			{
				saveMyBitmap(photo);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			if (tempFile.exists())
			{
				tempFile.delete();
			}
			Editor ed= getActivity().getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE).edit();
			ed.putString(Config.THUMB_PATH, PATH + FILENAME);
			ed.commit();
			Intent broadcastIntent= new Intent();
			broadcastIntent.setAction(Config.CHANGE_THUMB_BROADCAST);
			getActivity().sendBroadcast(broadcastIntent);
			if (confirmWindow.isShowing())
				confirmWindow.dismiss();
		}
	}//end inner class
	/**
	 * 取消头像 则恢复原有头像
	 * @author Administrator
	 *
	 */
	private final class mCancelSet implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			HomeActivity homeActivity= (HomeActivity) getActivity();
			Bitmap originBitmap= homeActivity.prepareThumbImage();
			thumb.setImageBitmap(originBitmap);
			if (confirmWindow.isShowing())
				confirmWindow.dismiss();
		}
	}//end inner class
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onResume()
	{
		super.onResume();
	}
	@Override
	public void onPause()
	{
		super.onPause();
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		System.gc();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		switch (requestCode)
		{
			case TAKE_PHOTO :
				startPhotoZoom(Uri.fromFile(tempFile), 200);
				break;
			case SELECT_GALLERY :
				if (data != null)
					startPhotoZoom(data.getData(), 200);
				break;
			case TAKE_CROP :
				if (data != null)
					setPicToView(data);
				break;
		}
	}
	private final class SetThumbClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			popWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		}
	}//end inner class
	private final class TakePhotoClick implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if (popWindow.isShowing())
				popWindow.dismiss();
			// 调用系统的拍照功能
			Intent intent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 指定调用相机拍照后照片的储存路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
			startActivityForResult(intent, TAKE_PHOTO);
		}
	}// end inner class
	private final class BackClick implements OnClickListener
	{// 返回第一页面
		@Override
		public void onClick(View v)
		{
			mGallery.setCurrentItem(0);
		}
	}// end inner class
	private final class SelectfromGallery implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			if (popWindow.isShowing())
				popWindow.dismiss();
			Intent intent= new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(intent, SELECT_GALLERY);
		}
	}//end inner class
	public void saveMyBitmap(Bitmap bmp) throws IOException
	{
		if (!FileUtils.isHasSdcard())
		{
			throw new IOException("not found sd card!");
		}
		File folder= new File(PATH);
		if (!folder.exists())
		{
			folder.mkdirs();
		}
		File f= new File(PATH + FILENAME);
		if (f.exists())
		{
			f.delete();
		}
		f.createNewFile();
		FileOutputStream fOut= null;
		try
		{
			fOut= new FileOutputStream(f);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try
		{
			fOut.flush();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		try
		{
			fOut.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	// 将进行剪裁后的图片显示到UI界面上
	private void setPicToView(Intent picdata)
	{
		Bundle bundle= picdata.getExtras();
		if (bundle != null)
		{
			photo= PicProcessUtils.toRoundBitmap((Bitmap) bundle.getParcelable("data"));//处理为圆角图片
			//			thumb.setVisibility(View.VISIBLE);
			thumb.setImageBitmap(photo);
			mPreView.setImageBitmap(photo);
			confirmWindow.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
		}
	}
	private void startPhotoZoom(Uri uri, int size)
	{
		Intent intent= new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, TAKE_CROP);
	}
	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName()
	{
		Date date= new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat= new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
}// end class
