package com.camelbell.jobrecord.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
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
	public static double RATIO_WIDTH = CURRENT_SCREEN_WIDTH / STANDARD_SCREEN_WIDTH;
	/**
	 * 屏幕高度比例
	 */
	public static double RATIO_HEIGHT = CURRENT_SCREEN_HEIGHT / STANDARD_SCREEN_HEIGHT;
	/**
	 * 屏幕密度比例
	 */
	public static double RATIO_DENSITY = CURRENT_DENSITY / STANDARD_DENSITY;

	/**
	 * 初始化当前屏幕的实际宽高值
	 */
	public static void getCurrentWindowMetrics(Context ctx) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

		CURRENT_SCREEN_WIDTH = displayMetrics.widthPixels;
		CURRENT_SCREEN_HEIGHT = displayMetrics.heightPixels;
		CURRENT_DENSITY = displayMetrics.densityDpi;
		RATIO_WIDTH = CURRENT_SCREEN_WIDTH / STANDARD_SCREEN_WIDTH;
		RATIO_HEIGHT = CURRENT_SCREEN_HEIGHT / STANDARD_SCREEN_HEIGHT;
		RATIO_DENSITY = STANDARD_DENSITY / CURRENT_DENSITY;

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
			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
			p.setMargins((int) getCurrentWidthSize(l), (int) getCurrentHeightSize(t), (int) getCurrentWidthSize(r), (int) getCurrentHeightSize(b));
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

	public static String AdaptationString(String values) {
		String returnValue = "";
		if (values.contains("&")) {
			returnValue = values.replace("&", "＆");
		} else {
			returnValue = values;
		}
		return returnValue;
	}

	/**
	 * 设置控件长宽自适应分辨率
	 * 
	 * @param view
	 *            控件
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 */
	public static void setLayoutParams(View view, int width, int height) {
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.width = (int) (MetricsUtil.getCurrentWidthSize(width) * MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);
		layoutParams.height = (int) (MetricsUtil.getCurrentHeightSize(height) * MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);
		view.setLayoutParams(layoutParams);
	}

	/**
	 * 设置控件长宽自适应分辨率
	 * 
	 * @param view
	 *            控件
	 * @param height
	 *            高
	 */

	public static void setLayoutParamsHeight(View view, int height) {
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.height = (int) (MetricsUtil.getCurrentHeightSize(height) * MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);
		view.setLayoutParams(layoutParams);
	}

	/**
	 * 设置控件长宽自适应分辨率
	 * 
	 * @param view
	 *            控件
	 * @param width
	 *            宽
	 * @param height
	 *            高
	 */
	public static void setLayoutParamsWidth(View view, int width) {
		ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
		layoutParams.width = (int) (MetricsUtil.getCurrentWidthSize(width) * MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY);
		view.setLayoutParams(layoutParams);
	}
	public static void setPaddingView(View view, int l, int t, int r, int b) {
		view.setPadding((int) (MetricsUtil.getCurrentWidthSize(l) * MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY), (int) (MetricsUtil.getCurrentWidthSize(t) * MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY), (int) (MetricsUtil.getCurrentWidthSize(r)
				* MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY), (int) (MetricsUtil.getCurrentWidthSize(b) * MetricsUtil.CURRENT_DENSITY / MetricsUtil.STANDARD_DENSITY));
	}
}
