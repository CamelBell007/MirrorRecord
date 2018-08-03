/****************************************************************
*
*	File name:	AimView.java
*	
*	This file contain AimView class.
*	
*	Last update: 	June 2, 2008.
*
*	Programmer:	Eran Porat
*
*	Copyright (c) 3GVision LTD.
*
*****************************************************************/

package com.threegvision.products.inigma_sdk_pro.ReferenceApp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import com.camelbell.jobrecord.activity.SingleMipcaActivity;

import java.util.Vector;

public final class AimView extends View
{
	static Canvas m_pCanvas=null;
	static Bitmap m_pBitmap=null;
	static Paint m_pPaint=null;
	int m_nViewfinderPercentSize=0;
	Vector<AimVector> m_ViewfinderVectors=null;
	int m_Color=0;
	Rect m_RectView=null;
	Activity activity=null;
	
	class AimVector
	{
		public int m_nX=0,m_nY=0,m_nW=0,m_nH=0;
		public AimVector(int x,int y,int w,int h)
		{
			this.m_nX=x;
			this.m_nY=y;
			this.m_nW=w;
			this.m_nH=h;
		}
		
	}
	public AimView(Activity activity)
	{
		super(activity);
		this.activity=activity;
		SetViewfinder();
	}

	public void SetPreviewRect(Rect previewrect)
	{
		m_RectView=new Rect(previewrect);
	    Runnable r=new Runnable() 
	    {
	        public void run() 
	        {
				try
				{
					layout(m_RectView.left,m_RectView.top,m_RectView.right,m_RectView.bottom); 
				}
				catch (Throwable t) 
				{
				}
	        }
	    };
	    activity.runOnUiThread(r);
	}
	
	protected void onSizeChanged (int w, int h, int oldw, int oldh) 
	{
		super.onSizeChanged(w, h, oldw, oldh);
		try
		{
			if (m_pBitmap==null || m_pBitmap.getWidth()!=w || m_pBitmap.getHeight()!=h)
			{
				m_pBitmap=Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
				m_pCanvas=new Canvas(m_pBitmap);
				m_pPaint=new Paint();
			}
		}
		catch(Throwable t)
		{
		}
	}

	void SetViewfinder()
	{
		try
		{
			if (m_ViewfinderVectors==null)
			{
				m_ViewfinderVectors=new Vector<AimVector>();
				// left line
				m_ViewfinderVectors.add(new AimVector(0,0,3,20));
				m_ViewfinderVectors.add(new AimVector(0,40,3,20));
				m_ViewfinderVectors.add(new AimVector(0,80,3,20));
		
				// Right line
				m_ViewfinderVectors.add(new AimVector(97,0,3,20));
				m_ViewfinderVectors.add(new AimVector(97,40,3,20));
				m_ViewfinderVectors.add(new AimVector(97,80,3,20));
		
				// Top line
				m_ViewfinderVectors.add(new AimVector(0,0,20,3));
				m_ViewfinderVectors.add(new AimVector(40,0,20,3));
				m_ViewfinderVectors.add(new AimVector(80,0,20,3));
		
				// Bottom line
				m_ViewfinderVectors.add(new AimVector(0,97,20,3));
				m_ViewfinderVectors.add(new AimVector(40,97,20,3));
				m_ViewfinderVectors.add(new AimVector(80,97,20,3));
		
				// + lines
				m_ViewfinderVectors.add(new AimVector(0,48,10,3));
				m_ViewfinderVectors.add(new AimVector(90,48,10,3));
				m_ViewfinderVectors.add(new AimVector(48,0,3,10));
				m_ViewfinderVectors.add(new AimVector(48,90,3,10));
			}
			m_nViewfinderPercentSize=70;
			m_Color=(201<<16)|(50<<8)|(50);
		}
		catch(Throwable t)
		{
		}
	}

	public void onDraw(Canvas canvas)
	{
		try
		{
			super.onDraw(canvas);
			if (m_nViewfinderPercentSize>0 && m_ViewfinderVectors!=null && m_ViewfinderVectors.size()>0 &&  m_RectView!=null)
			{
				int mindim=m_RectView.width();
				if (mindim>m_RectView.height())
					mindim=m_RectView.height();
				int radius=mindim*m_nViewfinderPercentSize/100;
				int color=m_Color;
				color |= 0xff000000;
				m_pPaint.setColor(color);
				m_pPaint.setStyle(Paint.Style.FILL);
				for (int i=0;i<m_ViewfinderVectors.size();i++)
				{
					AimVector v=(AimVector)m_ViewfinderVectors.elementAt(i);
					int x=m_RectView.left;
					int y=m_RectView.top;
					int w=m_RectView.right-m_RectView.left;
					int h=m_RectView.bottom-m_RectView.top;
					int X=x+w/2-radius/2+radius*v.m_nX/100;
					int Y=y+h/2-radius/2+radius*v.m_nY/100;
					int W=x+w/2-radius/2+radius*(v.m_nX+v.m_nW)/100;
					int H=y+h/2-radius/2+radius*(v.m_nY+v.m_nH)/100;
					canvas.drawRect(X,Y,W,H,m_pPaint);
				}
				canvas.drawBitmap(m_pBitmap,0,0,m_pPaint);
			}
		}
		catch(Throwable t)
		{
		}
	}

	public boolean onTouchEvent(MotionEvent event) 
	{
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			((SingleMipcaActivity)activity).AutoFocus();
			break;
		}
		return true;
	}

}
