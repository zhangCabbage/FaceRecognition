package com.zhang.util;

import android.content.Context;
import android.widget.Toast;

public class MyShowToast {
	
	/**
	 * 短时间显示Toast弹窗
	 * @param context
	 * @param str
	 */
	public static void showToastShortTime(Context context, String str){
		Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 长时间显示Toast弹窗
	 * @param context
	 * @param str
	 */
	public static void showToastLongTime(Context context, String str){
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}
}
