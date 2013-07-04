package com.airAd.yaqinghui.ui;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import com.airAd.yaqinghui.business.model.CepEvent;
public class CepTextView extends TextView
{
	private boolean isSelected= false;
	private CepEvent cepEvent;
	private Paint paint;
	private int SELECTED_RECT_COLOR= Color.rgb(46, 152, 212);
	private int UNSELECTED_COLOR= Color.rgb(141, 141, 141);
	private Rect rect;
	public CepTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		paint= new Paint();
		paint.setColor(SELECTED_RECT_COLOR);
		rect= new Rect();
	}

	public void setCepEvent(CepEvent cepEvent)
	{
		this.cepEvent= cepEvent;
		this.setText(cepEvent.getName());
	}

	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		if (isSelected)
		{
			int height= getHeight() / 10;
			int width= getWidth();
			int top= 9 * height;
			int left= 0;
			rect.set(left, top, left + width, top + height);
			canvas.drawRect(rect, paint);
		}
	}
	public void selected()
	{
		isSelected= true;
		this.setTextColor(SELECTED_RECT_COLOR);
		invalidate();
	}
	public void unSelected()
	{
		isSelected= false;
		this.setTextColor(UNSELECTED_COLOR);
		invalidate();
	}
}//end class
