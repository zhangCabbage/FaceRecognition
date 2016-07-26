package com.zhang.util;

import java.util.Random;

import android.graphics.Color;

public class MyColor {
	
	private static String[] RGBColor = {"65cb65", "ffcc32", "ff9a32", "ff9997", "5da2fd"} ; 
	/**
	 * 通过十六进制的string类型获得color类的int型数据
	 * @param str
	 * @return
	 */
	public static int getIntColor(String str){
		int red = Integer.valueOf(str.substring(0, 2), 16);
		int green = Integer.valueOf(str.substring(2, 4), 16);
		int blue = Integer.valueOf(str.substring(4, 6), 16);
		return Color.rgb(red, green, blue);
	}
	
	public static int getRandomColor(){
		int index = new Random().nextInt(RGBColor.length);
		return getIntColor( RGBColor[index] );
	}
}
