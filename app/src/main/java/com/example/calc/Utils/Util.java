package com.example.calc.Utils;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by iFuck on 2018/2/6.
 */

public class Util {

	public static void showTipText(Context context, String tipText) {
		Toast.makeText(context, tipText, Toast.LENGTH_SHORT).show();
	}

	public static  void clearTextView(TextView textView) {
		textView.setText("");
	}

	public static  void clearTextView(TextView textView1, TextView textView2) {
		textView1.setText("");
		textView2.setText("");
	}

	public static  void clearStringBuilder(StringBuilder stringBuilder) {
		stringBuilder.setLength(0);
	}

	public static  void clearStringBuilder(StringBuilder stringBuilder1, StringBuilder stringBuilder2) {
		stringBuilder1.setLength(0);
		stringBuilder2.setLength(0);
	}

	public static  String getStringBuilderLastOneStr(StringBuilder stringBuilder) {
		if (stringBuilder.length() > 0) {
			return Character.toString(stringBuilder.charAt(stringBuilder.length() - 1));
		}
		return null;
	}


}
