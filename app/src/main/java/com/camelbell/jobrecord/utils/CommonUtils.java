package com.camelbell.jobrecord.utils;

public class CommonUtils {
	private static long lastClickTime;

	public boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (timeD > 500) {

			lastClickTime = time;

			return true;
		}
		return false;
	}
	/**
	 * 判断当前的字符是否为空
	 * @param string
	 * @return
	 */
	public static boolean stringIsEmpty(String string){
		boolean empty = true;
		if(string != null&&!"".equals(string)){
			empty =  false;
		}
		return empty;
	}

}