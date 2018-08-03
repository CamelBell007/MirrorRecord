package com.camelbell.jobrecord.utils;

import java.io.File;

import android.os.Environment;


/**
 * Log统一管理类
 * 
 * @author way
 * 
 */
public class CamelBellUtils
{

	/**
	 * 判断字符串是否为空
	 * @param msg
	 * @return
	 */
	public static boolean isNotEmpty(String msg)
	{
		if(msg!=null&&!msg.equals("")){
			return true;
		}else{
			return false;
		}

	}
	/**
	 * 获取SD卡的路径
	 * @return
	 */
	public static String getSDPath(){ 
		File sdDir = null; 
		String path = "";
		boolean sdCardExist = Environment.getExternalStorageState()   
				.equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在 
		if(sdCardExist)   
		{                   
			//获取跟目录             
			sdDir = Environment.getExternalStorageDirectory();
		} 
		if(sdDir!=null){
			path = sdDir.toString();
		}
		return path; 

	}
}