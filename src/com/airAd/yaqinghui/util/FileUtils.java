package com.airAd.yaqinghui.util;

import android.os.Environment;

public class FileUtils {
	
	/**
	 * 检查SD卡是否插入
	 * @return
	 */
	public static boolean isHasSdcard() {
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}
}
