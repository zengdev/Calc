package com.example.calc.Utils;

import android.util.Log;

/**
 * Created by iFuck on 2018/2/8.
 */

public class LogUtil {

	private static final String TAG = ": LogUtil";

	public static final int VERBOSE = 1;
	public static final int DEBUG = 2;
	public static final int INFO = 3;
	public static final int WARN = 4;
	public static final int ERROR = 5;
	public static final int NOTHING = 6;

	public static int level = VERBOSE;
//	public static int level = NOTHING;

	public static void v(String tag, String msg) {
		if (level <= VERBOSE) {
			Log.v(tag + TAG, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (level <= DEBUG) {
			Log.d(tag + TAG, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (level <= INFO) {
			Log.i(tag + TAG, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (level <= WARN) {
			Log.w(tag + TAG, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (level <= ERROR) {
			Log.e(tag + TAG, msg);
		}
	}
}
