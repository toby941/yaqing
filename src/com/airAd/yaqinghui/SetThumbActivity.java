package com.airAd.yaqinghui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.airAd.yaqinghui.common.Config;
import com.airAd.yaqinghui.common.FileUtils;

/**
 * 头像设置页
 * 
 * @author Panyi
 */
public class SetThumbActivity extends BaseActivity {
	public static final String PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/"
            + Config.FOLDER + "/";// 路径
    public static final String FILENAME = "mythumb.png"; // 照片名称

    public static final int TAKE_PHOTO = 1;// 拍照
    public static final int SELECT_GALLERY = 2;// 相册中选择
    public static final int TAKE_CROP = 3;// 设置剪裁

	private Button mTakePhoto;
	private Button mSetPhoto;
    private ImageView mPreView;// 预览图
	private Bitmap photo;

	private SharedPreferences sp;

	protected File tempFile = new File(
			Environment.getExternalStorageDirectory(), getPhotoFileName());

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setthumb);
		mTakePhoto = (Button) findViewById(R.id.takephoto);
		mSetPhoto = (Button) findViewById(R.id.setphoto);

		mPreView = (ImageView) findViewById(R.id.preview);

		mTakePhoto.setOnClickListener(new TakePhotoClick());
		mSetPhoto.setOnClickListener(new SaveBitmap());
		sp = getSharedPreferences(Config.PACKAGE, Context.MODE_PRIVATE);
	}

    // 相机返回
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PHOTO:
			startPhotoZoom(Uri.fromFile(tempFile), 150);
			break;
		case SELECT_GALLERY:
			break;
		case TAKE_CROP:
			if (data != null) {
				setPicToView(data);
			}
			break;
		}
	}

	public void saveMyBitmap(Bitmap bmp) throws IOException {
		if (!FileUtils.isHasSdcard()) {
			throw new IOException("not found sd card!");
		}
		File folder = new File(PATH);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File f = new File(PATH + FILENAME);
		if (f.exists()) {
			f.delete();
		}
		f.createNewFile();
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    // 将进行剪裁后的图片显示到UI界面上
	private void setPicToView(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (bundle != null) {
			photo = bundle.getParcelable("data");
			mPreView.setImageBitmap(photo);
		}
	}

	private void startPhotoZoom(Uri uri, int size) {
		Intent intent = new Intent("com.android.camera.action.CROP");
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
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	    /**
     * 拍照
     * 
     * @author Administrator
     */
	private final class TakePhotoClick implements OnClickListener {
		@Override
		public void onClick(View v) {
            // 调用系统的拍照功能
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // 指定调用相机拍照后照片的储存路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
			startActivityForResult(intent, TAKE_PHOTO);
		}
	}// end inner class

	private final class SaveBitmap implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (photo == null) {
				return;
			}
			try {
				saveMyBitmap(photo);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (tempFile.exists()) {
				tempFile.delete();
			}
			Editor ed = sp.edit();
			ed.putString(Config.THUMB_PATH, PATH + FILENAME);
			ed.commit();
			
			Intent broadcastIntent = new Intent();
			broadcastIntent.setAction(Config.CHANGE_THUMB_BROADCAST);
			SetThumbActivity.this.sendBroadcast(broadcastIntent);
			
			SetThumbActivity.this.finish();
		}
	}// end inner class
}// end class
