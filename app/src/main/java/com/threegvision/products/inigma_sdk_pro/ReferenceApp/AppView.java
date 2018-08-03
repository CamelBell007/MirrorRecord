/****************************************************************
*
*	File name:	AppView.java
*	
*	This file contain AppView class.
*	
*	Last update: 	June 2, 2008.
*
*	Programmer:	Eran Porat
*
*	Copyright (c) 3GVision LTD.
*
*****************************************************************/

package com.threegvision.products.inigma_sdk_pro.ReferenceApp;

import java.util.Vector;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.*;

public final class AppView extends View
{
	Canvas m_Canvas=null;
	Bitmap m_Bitmap=null;
	Paint m_Paint=null;
	static String m_Text=null;
	
	public AppView(Context context,AttributeSet attrs)
	{
		super(context,attrs);
	}

	protected void onSizeChanged (int w, int h, int oldw, int oldh) 
	{
		super.onSizeChanged(w, h, oldw, oldh);
		try
		{
			m_Bitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
			m_Canvas=new Canvas(m_Bitmap);
			m_Paint=new Paint();
			m_Paint.setTypeface(Typeface.DEFAULT);
			m_Paint.setTextSize(getHeight()/15);
			m_Paint.setStyle(Paint.Style.FILL);
			Draw();
		}
		catch(Throwable t)
		{
		}
	}

	public void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		if (m_Bitmap!=null)
			canvas.drawBitmap(m_Bitmap,0,0,m_Paint);
	}
	
	public void SetText(String text)
	{
		m_Text=new String(text);
		Draw();
	}
	
	public void Draw()
	{
		if (m_Canvas!=null)
		{
			m_Paint.setColor(0xff000000);
			m_Canvas.drawRect(new Rect(0,0,getWidth(),getHeight()),m_Paint);
			if (m_Text!=null)
			{
				m_Paint.setColor(0xff00ff00);
				Vector<String> lines=new Vector<String>();
				String text=new String(m_Text);
				while(text.length()>0)
				{
					String line=new String();
					while(text.length()>0)
					{
						char c=text.charAt(0);
						float w=m_Paint.measureText(line+c);
						if (w>=getWidth()-10)
							break;
						line+=c;
						text=text.substring(1,text.length());
					}
					lines.add(line);
				}
				for (int i=0;i<lines.size();i++)
				{
					String line=(String)lines.get(i);
					float x=(getWidth()-m_Paint.measureText(line))/2;
					float y=(getHeight()-m_Paint.getTextSize()*lines.size())/2+i*m_Paint.getTextSize();
					m_Canvas.drawText(line,x,y,m_Paint);
				}
			}
			invalidate();
		}
	}
}
