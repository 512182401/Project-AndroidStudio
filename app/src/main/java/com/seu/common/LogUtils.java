package com.seu.common;

import android.util.Log;

/**
 * 对于log的打印输出封装类
 * 
 * @author wdl
 * 
 */
public class LogUtils {
//		public static boolean isDebuger = false;//上线模式
	public static boolean isDebuger = true;//开发模式

	public static void sysout(String msg) {
		if (isDebuger) {
			System.out.println(msg);
		}
	}

	public static void info(String tag, String msg) {
		if (isDebuger) {
			Log.i(tag, msg);
		}
	}
	public static void log(String tag, String msg) {
		if (isDebuger) {
			Log.e(tag, msg);
		}
	}
	public static void w(String tag, String msg) {
		if (isDebuger) {
			Log.w(tag, msg);
		}
	}
}
