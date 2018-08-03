package com.camelbell.jobrecord.application;

import android.app.Application;
import android.content.Context;

import com.camelbell.jobrecord.bean.Constant;

public class MyApplication extends Application {
	public static String userId ;
	public static Context context;
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();

		//		//如果安装时间超过一个月或者时间超过11.15则直接退出程序
		long currentTime = System.currentTimeMillis()/1000 ;
//		long installTime = PathFromUri.getTimeFromInstallTime(getApplicationContext());
//		boolean time1 = installTime  > 2592000;
		boolean time2 = currentTime > Constant.LimitTime;
//		if(time2){
//			Log.d("vincent","时间到了，无法使用。");
//			System.exit(0);
//		}
 
	}
	public static Context getContext(){
		return context;
	}
}
