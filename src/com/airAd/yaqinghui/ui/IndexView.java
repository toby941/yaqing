package com.airAd.yaqinghui.ui;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
/**
 * 
 * @author Panyi
 * 
 */
public class IndexView extends View
{
	private static final int SPACE= 20;
	private static final int RADIUS= 5;
	private static final float SELECT_RADIUS= 6;
	private static final int COLOR_NORMAL= Color.rgb(176, 179, 186), COLOR_HIGHTLIGHT= Color.rgb(231, 76, 60);
	private int num;
	private int highLightIndex;// ������Ԫ ��0��ʼ����
	private int startX;
	private Paint paintNormal, paintHightLight;
	private float selectedRadius;
	public IndexView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init();
	}
	public IndexView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}
	public IndexView(Context context)
	{
		super(context);
		init();
	}
	private void init()
	{
		num= 0;
		highLightIndex= 0;
		paintNormal= new Paint();
		paintNormal.setColor(COLOR_NORMAL);
		paintNormal.setAntiAlias(true);
		paintHightLight= new Paint();
		paintHightLight.setColor(COLOR_HIGHTLIGHT);
		paintHightLight.setAntiAlias(true);
	}
	/**
	 * ���õ���
	 * 
	 * @param number
	 */
	public void setNum(int number)
	{
		if (number < 0)
		{
			return;
		}
		this.num= number;
		this.highLightIndex= 0;
		if (number % 2 == 0)
		{// ż�����
			startX= getWidth() / 2 - ((number - 1) / 2) * SPACE - SPACE / 2;
		}
		else
		{// �������
			startX= getWidth() / 2 - (number / 2) * SPACE;
		}
		selectedRadius= SELECT_RADIUS;
	}
	public void setNum(int number, int screnWidth)
	{
		if (number < 0)
		{
			return;
		}
		this.num= number;
		this.highLightIndex= 0;
		startX= screnWidth / 2 - (number / 2) * SPACE + (number % 2) * (SPACE >> 1) + SPACE + screnWidth;
		startX= 5 * startX - 6;
		paintHightLight.setColor(COLOR_HIGHTLIGHT);
	}
	public void prePoint()
	{
		highLightIndex--;
		if (highLightIndex < 0)
		{
			highLightIndex= 0;
		}
		invalidate();
	}
	public void nextPoint()
	{
		highLightIndex++;
		if (highLightIndex > num - 1)
		{
			highLightIndex= num - 1;
		}
	}
	public void setPoint(int index)
	{
		if (index >= 0 && index < num)
		{
			this.highLightIndex= index;
		}
		selectedRadius= RADIUS;
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (num <= 1 || num > 24)
		{
			return;
		}
		int y= getHeight() >> 1;
		for (int i= 0; i < num; i++)
		{
			if (i != highLightIndex)
			{
				canvas.drawCircle(startX + SPACE * i, y, RADIUS, paintNormal);
			}
			else
			{
				canvas.drawCircle(startX + SPACE * i, y, RADIUS, paintHightLight);
			}
		}// end for
	}
}// end class
