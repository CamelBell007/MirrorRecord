package com.camelbell.jobrecord.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * 
 * @author wangwenbin 2012-12-19
 */
public class ActivityStackManager {

	private static Stack<Activity> activityStack;
	private static ActivityStackManager instance;

	private ActivityStackManager() {
	};

	/**
	 * 单个实例
	 */
	public static ActivityStackManager getActivityManager() {
		if (instance == null) {
			instance = new ActivityStackManager();
		}
		return instance;
	}

	/**
	 * add Activity to activityStack
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * finish当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * finish 指定的Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			// activity.finish();
			// activity = null;
		}
	}

	/**
	 * finish 指定类名的Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * finish all Activity
	 */
	public static void finishAllActivity() {
		if(activityStack!=null){
			for (Activity activity : activityStack) {
				activity.finish();
			}
			activityStack.clear();
		}
	}

	/**
	 * 退出应用程序
	 */
	public void AppExit() {
		try {
			finishAllActivity();
			System.exit(0);

		} catch (Exception e) {
		}
	}
	
	/**
	 * 切换跳转到 登录页面
	 * @param activity
	 */
//	public static void startLoginWithClear(Activity activity){
//		finishAllActivity();
//		AndroidUtils.jumpToLogin(activity);
//		//activity.startActivity(new Intent(activity, LoginActivity.class));
//	}
	
	/**
	 * 切换跳转到 主界面
	 * @param activity
	 */
//	public static void startMainWithClear(Activity activity){
//		finishAllActivity();
//		activity.startActivity(new Intent(activity, HexagonActivity.class));
//	}
}
