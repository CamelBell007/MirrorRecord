package com.camelbell.jobrecord.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.camelbell.jobrecord.application.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public final class UIUtil {
	private static final String TAG = "UIUtil";

	private UIUtil() {
	}

	/**
	 * dip转px
	 * 
	 * @param context
	 * @param dpValue
	 * @return
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * px转dip
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	
	/**
	 * sp转px
	 * 
	 * @param context
	 * @return
	 */
	 public static int sp2px(Context context, float spValue) { 
         final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
         return (int) (spValue * fontScale + 0.5f); 
     } 

	/**
	 * 根据路径获取byte数组
	 * 
	 * @param filePath
	 * @return
	 */
	public static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// Logger.d(TAG, "get byte[] error");
		}
		return buffer;
	}

	/**
	 * 根据输入流获得byte数组
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] getBytesByInput(InputStream input) {
		byte[] buffer = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = input.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			input.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// Logger.d(TAG, "get byte[] error");
		}
		return buffer;
	}

	/**
	* 显示toast（自定义显示时间）
	* <功能详细描述>
	* @param context
	* @param message
	* @param duration
	* @see [类、类#方法、类#成员]
	*/
	private static void showToastMessage(Context context, String message, int duration) {
		if (context != null && message != null) {
			try {
				Toast.makeText(context, message, duration).show();
			} catch (Throwable e) {
				if (null != e) {
					Log.e(TAG, "show toast error ,e :" + e.getMessage());
				}

			}
		}
	}

	/**
	 * 显示toast（短时间）
	 * <功能详细描述>
	 * @param resId
	 * @see [类、类#方法、类#成员]
	 */
	public static void showShortMessage(int resId) {
		Context context = MyApplication.getContext();
		showShortMessage(context, context.getString(resId));
	}

	/**
	 * 显示Toast
	 * @param message
	 */
	public static void showShortMessage(String message) {
		showToastMessage(MyApplication.getContext(), message, Toast.LENGTH_SHORT);
	}

	/**
	 * 显示toast（短时间）
	 * <功能详细描述>
	 * @param context
	 * @param message
	 * @see [类、类#方法、类#成员]
	 */
	public static void showShortMessage(Context context, String message) {
		showToastMessage(context, message, Toast.LENGTH_SHORT);
	}

	/**
	 * 显示toast（长时间）
	 * <功能详细描述>
	 * @param context
	 * @param message
	 * @see [类、类#方法、类#成员]
	 */
	public static void showLongMessage(Context context, String message) {
		showToastMessage(context, message, Toast.LENGTH_LONG);
	}

	/**
	 * 显示toast（短时间）
	 * <功能详细描述>
	 * @param context
	 * @param resId
	 * @see [类、类#方法、类#成员]
	 */
	public static void showShortMessage(Context context, int resId) {
		if (null != context) {
			showShortMessage(context, context.getString(resId));
		}
	}
}