package com.camelbell.jobrecord.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

/**
 * 屏幕尺寸适配工具类
 * 
 * @author kevinyin
 * 
 */
public class MetricsUtil extends Activity {
	/**
	 * 基准分辨率的宽
	 */
	public static double STANDARD_SCREEN_WIDTH = 1080;
	/**
	 * 基准分辨率的高
	 */
	public static double STANDARD_SCREEN_HEIGHT = 1920;
	/**
	 * 基准屏幕密度
	 */
	public static final double STANDARD_DENSITY = 160;
	/**
	 * 系统当前的分辨率的宽的初始值
	 */
	public static double CURRENT_SCREEN_WIDTH = 1080;
	/**
	 * 系统当前的分辨率的高的初始值
	 */
	public static double CURRENT_SCREEN_HEIGHT = 1920;
	/**
	 * 当前屏幕密度
	 */
	public static double CURRENT_DENSITY = 160;
	/**
	 * 屏幕宽度比例
	 */
	public static double RATIO_WIDTH = CURRENT_SCREEN_WIDTH
			/ STANDARD_SCREEN_WIDTH;
	/**
	 * 屏幕高度比例
	 */
	public static double RATIO_HEIGHT = CURRENT_SCREEN_HEIGHT
			/ STANDARD_SCREEN_HEIGHT;
	/**
	 * 屏幕密度比例
	 */
	public static double RATIO_DENSITY = CURRENT_DENSITY / STANDARD_DENSITY;
	
	/**
	 * 底部添加的导航高度
	 */
	public static int NAVIGATION_HEIGHT;

	/**
	 * 初始化当前屏幕的实际宽高值
	 */
	public static void getCurrentWindowMetrics(Context ctx) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) ctx).getWindowManager().getDefaultDisplay()
		.getMetrics(displayMetrics);

		CURRENT_SCREEN_WIDTH = displayMetrics.widthPixels;
		CURRENT_SCREEN_HEIGHT = displayMetrics.heightPixels;
		CURRENT_DENSITY = displayMetrics.densityDpi;
		RATIO_WIDTH = CURRENT_SCREEN_WIDTH / STANDARD_SCREEN_WIDTH;
		RATIO_HEIGHT = CURRENT_SCREEN_HEIGHT / STANDARD_SCREEN_HEIGHT;
		RATIO_DENSITY = STANDARD_DENSITY / CURRENT_DENSITY;

		SharedPreferences s = ctx.getSharedPreferences(
				"shopNewUpdate", Context.MODE_MULTI_PROCESS);
		Editor editor = s.edit();
		editor.putLong("CURRENT_DENSITY", (long) CURRENT_DENSITY);
		editor.putLong("STANDARD_DENSITY", (long) STANDARD_DENSITY);
		editor.commit();
	}

	/***
	 * 获取当前适配的字体大小
	 * 
	 * @param 字体大小
	 * @return 适配完成的字体大小 float类型
	 */
	public static float getCurrentTextSize(int size) {
		return (float) (size * MetricsUtil.RATIO_WIDTH * MetricsUtil.RATIO_DENSITY);
	}

	/***
	 * 获取当前适配的宽度大小
	 * 
	 * @param 宽度大小
	 * @return 适配完成的宽度大小 float类型
	 */
	public static float getCurrentWidthSize(int size) {
		return (float) (size * MetricsUtil.RATIO_WIDTH * MetricsUtil.RATIO_DENSITY);
	}

	/***
	 * 获取当前适配的高度大小
	 * 
	 * @param 高度大小
	 * @return 适配完成的高度大小 float类型
	 */
	public static float getCurrentHeightSize(int size) {
		return (float) (size * MetricsUtil.RATIO_HEIGHT * MetricsUtil.RATIO_DENSITY);
	}

	/***
	 * 配置View呈现适配的边距大小
	 * 
	 * @param view
	 *            ,left,top,right,bottom
	 */
	public static void setMargins(View v, int l, int t, int r, int b) {
		if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v
					.getLayoutParams();
			p.setMargins((int) getCurrentWidthSize(l),
					(int) getCurrentHeightSize(t),
					(int) getCurrentWidthSize(r), (int) getCurrentHeightSize(b));
			v.requestLayout();
		}
	}
	/***
	 * 配置View呈现适配的边距大小
	 * 
	 * @param view
	 *            ,left,top,right,bottom
	 */
	public static void setPaddings(View v, int l, int t, int r, int b) {
		v.setPadding((int) getCurrentWidthSize(l), (int) getCurrentHeightSize(t), 
				(int) getCurrentWidthSize(r),  (int) getCurrentHeightSize(b));
	}
	/***
	 * 配置View呈现适配的边距大小
	 * 
	 * @param view
	 *            ,left,top,right,bottom
	 */
	public static void setPadding(View v, int l, int t, int r, int b) {
		if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v
					.getLayoutParams();
			p.setMargins((int) getCurrentWidthSize(l),
					(int) getCurrentHeightSize(t),
					(int) getCurrentWidthSize(r), (int) getCurrentHeightSize(b));
			v.requestLayout();
		}
	}

	/**
	 * 查看字符串中有没有汉字
	 * 
	 * @param s
	 */
	public static boolean isContainChinese(String str) {

		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}


	/***
	 * 更新语言
	 * @param context
	 * @param lan
	 * @return
	 */

	/***
	 * 特殊字符转换，目前&替换成中文的&，解决特殊字符不显示的问题
	 * @param values
	 * @return
	 */
	public static String AdaptationString(String values) {
		String returnValue = "";
		if (values.contains("&")) {
			returnValue = values.replace("&", "＆");
		} else {
			returnValue = values;
		}
		return returnValue;
	}
}
