package com.airAd.yaqinghui.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static String retWeekName(int day) {
		String weekName = "";
		switch (day) {
		case 1:
			weekName = "Mon";
			break;
		case 2:
			weekName = "Tue";
			break;
		case 3:
			weekName = "Wed";
			break;
		case 4:
			weekName = "Thu";
			break;
		case 5:
			weekName = "Fri";
			break;
		case 6:
			weekName = "Sun";
			break;
		case 7:
			weekName = "Sat";
			break;
		default:
			weekName = "Sat";
		}

		return weekName;
	}

	public static String dateOfDay(int day) {
		if (day < 10) {
			return "0" + day;
		}
		return "" + day;
	}

	public static boolean isBlank(String str) {
		if (null == str || "".equals(str)) {
			return true;
		}
		return false;
	}

	public static boolean IsNumber(String str) {
		String regex = "^[0-9]*$";
		return match(regex, str);
	}

	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
}
