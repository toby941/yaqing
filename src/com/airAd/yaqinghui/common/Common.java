package com.airAd.yaqinghui.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class Common {
	public static String timeNotifyString(long time)
	{
		SimpleDateFormat sdf= new SimpleDateFormat("MM.dd.yyyy");
		Date dt= new Date(time);
		return sdf.format(dt);
	}
	public static String timeString(String timeStr)
	{
		long time= Long.parseLong(timeStr);
		SimpleDateFormat sdf= new SimpleDateFormat("HH:mm a");
		Date dt = new Date(time);  
		return sdf.format(dt);
	}
	
	public static int genRand(int min, int max) {
		return (new Random()).nextInt(max) % (max - min + 1) + min;
	}

	public static int getFlag(double rotateSpeed) {
		return rotateSpeed >= 0 ? 1 : -1;
	}

	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static float distance(float x1, float y1, float x2, float y2) {
		float x = x2 - x1;
		float y = y2 - y1;
		return (float) Math.sqrt(x * x + y * y);
	}

	public static boolean isHit(int left1, int top1, int w1, int h1, int left2,
			int top2, int w2, int h2) {
		int x1 = left1 + w1 / 2;
		int y1 = top1 + h1 / 2;
		int x2 = left2 + w2 / 2;
		int y2 = top2 + h2 / 2;
		if (Math.abs(x1 - x2) < (w1 / 2 + w2 / 2) - 10
				&& Math.abs(y1 - y2) < (h1 / 2 + h2 / 2) - 10) {
			return true;
		}
		return false;
	}
	
	public static boolean isHit(float left1, float top1, float w1, float h1, float left2,
			float top2, float w2, float h2) {
		float x1 = left1 + w1 / 2;
		float y1 = top1 + h1 / 2;
		float x2 = left2 + w2 / 2;
		float y2 = top2 + h2 / 2;
		if (Math.abs(x1 - x2) < (w1 / 2 + w2 / 2) - 10
				&& Math.abs(y1 - y2) < (h1 / 2 + h2 / 2) - 10) {
			return true;
		}
		return false;
	}

	public static boolean isCircleHit(int x, int y, int radius, int left,
			int top, int width, int height) {
		int x2 = left + width / 2;
		int y2 = top + height / 2;
		return (Math.abs(x - x2) < (radius + width / 2) && Math.abs(y - y2) < (radius + height / 2));
	}

	public static boolean isCircleHit(float x1, float y1, float r1, float x2,
			float y2, float r2) {
		float deltaX = x2 - x1;
		float deltaY = y2 - y1;
		float len = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		return len <= r1 + r2;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param centerX
	 * @param centerY
	 * @param radius
	 * @return
	 */
	public static boolean isInCircle(int x, int y, int centerX, int centerY,
			int radius) {
		int temp_x = x - centerX;
		int temp_y = y - centerY;
		if (temp_x * temp_x + temp_y * temp_y < radius * radius) {
			return true;
		}
		return false;
	}
}
