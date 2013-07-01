/**
 * 
 */
package com.airAd.yaqinghui.util;

import android.content.Context;
import com.amap.api.maps.AMap;

public class AMapUtil {
	public static boolean checkReady(Context context, AMap aMap) {
		if (aMap == null) {
			return false;
		}
		return true;
	}
}
