package com.example.calc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.calc.Utils.LogUtil;
import com.example.calc.Utils.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

	private static final String TAG = "MainActivity";

	// 控件
	private static TextView calcTextView;		// 计算式显示区域
	private static TextView resultTextView;	// 结果显示区域
	private static Button addBtn;				// ＋
	private static Button subBtn;				// －
	private static Button mulBtn;				// ×
	private static Button divBtn;				// ÷
	private static Button pointBtn;			// 小数点

	// 运算操作类型
	private static final int OPERA_TYPE_NONE = 0;
	private static final int OPERA_TYPE_ADD = 1;
	private static final int OPERA_TYPE_SUB = 2;
	private static final int OPERA_TYPE_MUL = 3;
	private static final int OPERA_TYPE_DIV = 4;

	private static int currentOperaType;	// 当前运算操作类型

	// flag
	private static boolean isExistResult;
	private static boolean isOperaSuccess;

	// str
	private static StringBuilder calcStr;		// 计算式
	private static StringBuilder resultStr;	// 计算结果
	private static String operaStr;			// 运算操作

	// 操作数对象
	private static OperaNum operaNum1;
	private static OperaNum operaNum2;
	private static OperaNum currentOperaNum;

	private static double result;	//运算结果

	class OperaNum {
		public boolean havePoint = false;	// 是否包含小数点
		public double value = 0;				// 操作数double值
		public StringBuilder valueStr;		// 操作数

		public OperaNum() {
			valueStr = new StringBuilder("");
			init();
		}

		public OperaNum(StringBuilder valueStr) {
			this.valueStr = valueStr;
		}

		public double getValue() {
			value = Double.valueOf(valueStr.toString()).doubleValue();
			return value;
		}

		public void init() {
			value = 0;
			clearStringBuilder(valueStr);
			havePoint = false;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		calcStr = new StringBuilder("");
		resultStr = new StringBuilder("");
		operaNum1 = new OperaNum();
		operaNum2 = new OperaNum();
		calcTextView = findViewById(R.id.calc_textview);
		resultTextView = findViewById(R.id.result_textview);
		addBtn = findViewById(R.id.btn_add);
		subBtn = findViewById(R.id.btn_sub);
		mulBtn = findViewById(R.id.btn_mul);
		divBtn = findViewById(R.id.btn_div);
		pointBtn = findViewById(R.id.btn_point);
		clear();
	}

	private void clearTextView(TextView textView) {
		textView.setText("");
	}

	private void clearTextView(TextView textView1, TextView textView2) {
		textView1.setText("");
		textView2.setText("");
	}

	private void clearStringBuilder(StringBuilder stringBuilder) {
		stringBuilder.setLength(0);
	}

	private void clearStringBuilder(StringBuilder stringBuilder1, StringBuilder stringBuilder2) {
		stringBuilder1.setLength(0);
		stringBuilder2.setLength(0);
	}

	private void clearData() {
		pointBtn.setEnabled(true);
		addBtn.setEnabled(true);
		subBtn.setEnabled(true);
		mulBtn.setEnabled(true);
		divBtn.setEnabled(true);
		currentOperaType = OPERA_TYPE_NONE;
		currentOperaNum = operaNum1;
		isExistResult = false;
		isOperaSuccess = false;
		result = 0;
		operaNum1.init();
		operaNum2.init();
		clearStringBuilder(calcStr, resultStr);
		operaStr = "";
	}

	private void clear() {
		clearData();
		clearTextView(calcTextView, resultTextView);
	}

	private String getStringBuilderLastOneStr(StringBuilder stringBuilder) {
		if (stringBuilder.length() > 0) {
			return Character.toString(stringBuilder.charAt(stringBuilder.length() - 1));
		}
		return null;
	}

	private void numClick(String num) {
		String tempNum = num;
		if (isExistResult) {
			clear();
		}
		if (null == currentOperaNum) {
			currentOperaNum = operaNum1;
		}
		if (currentOperaNum.valueStr.toString().equals("0")
				&& (num.equals("0") || num.equals("00"))) {
			return;
		}
		if (0 == currentOperaNum.valueStr.length()
				&& num.equals("00")) {
			tempNum = "0";
		}
		if (currentOperaNum.havePoint) {
			pointBtn.setEnabled(false);
		} else {
			pointBtn.setEnabled(true);
		}
		currentOperaNum.valueStr.append(tempNum);
		calcStr.append(tempNum);
		calcTextView.setText(calcStr.toString());
	}

	private void pointClick() {
		if (isExistResult) {
			return;
		}
		if (currentOperaNum.havePoint || currentOperaNum.valueStr.length() == 0) {
			return;
		}
		currentOperaNum.valueStr.append(".");
		currentOperaNum.havePoint = true;
		pointBtn.setEnabled(false);
		calcStr.append(".");
		calcTextView.setText(calcStr.toString());
	}

	private void operaClick(String operaStr) {
		if (isExistResult) {
			return;
		}
		String operaNum1LastOneStr = getStringBuilderLastOneStr(currentOperaNum.valueStr);
		if (operaNum1LastOneStr.equals(".")) {
			Util.showTipText(getApplicationContext(), "操作数最后一位不能为小数点！");
			return;
		}
		this.operaStr = operaStr;
		calcStr.append(operaStr);
		calcTextView.setText(calcStr.toString());
		currentOperaNum = operaNum2;
		addBtn.setEnabled(false);
		subBtn.setEnabled(false);
		mulBtn.setEnabled(false);
		divBtn.setEnabled(false);
	}

	private void delClick() {
		if (calcStr.length() == 0) {
			return;
		}
		calcStr.deleteCharAt(calcStr.length() - 1);
		calcTextView.setText(calcStr.toString());
		if (isExistResult) {
			clearStringBuilder(resultStr);
			clearTextView(resultTextView);
			isExistResult = false;
			isOperaSuccess = false;
			return;
		}
		String calcLastOneStr = "";
		if (calcStr.length() > 0) {
			calcLastOneStr = Character.toString(calcStr.charAt(calcStr.length() - 1));
		}
		if (calcLastOneStr.equals(operaStr)) {
			operaStr = "";
			currentOperaNum = operaNum1;
			addBtn.setEnabled(true);
			subBtn.setEnabled(true);
			mulBtn.setEnabled(true);
			divBtn.setEnabled(true);
		} else {
			currentOperaNum.valueStr.deleteCharAt(currentOperaNum.valueStr.length() - 1);
			if (calcLastOneStr.equals(".")) {
				currentOperaNum.havePoint = false;
				pointBtn.setEnabled(true);
			}
		}
	}

	private void add() {
		result = operaNum1.getValue() + operaNum2.getValue();
		isOperaSuccess = true;
	}

	private void sub() {
		result = operaNum1.getValue() - operaNum2.getValue();
		isOperaSuccess = true;
	}

	private void mul() {
		result = operaNum1.getValue() * operaNum2.getValue();
		isOperaSuccess = true;
	}

	private void div() {
		if (0 == operaNum2.getValue()) {	// 未验证----------------------
			Util.showTipText(getApplicationContext(), "除数不能为0！");
			isOperaSuccess = false;
			return;
		}
		result = operaNum1.getValue() / operaNum2.getValue();
		isOperaSuccess = true;
	}

	private void calc() {
		if (isExistResult) {
			return;
		}

		String calcLastOneStr = getStringBuilderLastOneStr(calcStr);

		if (calcLastOneStr.equals(".")) {
			Util.showTipText(getApplicationContext(), "操作数最后一位不能为小数点！");
			return;
		}

		if (calcLastOneStr.equals(operaStr)) {
			Util.showTipText(getApplicationContext(), "请输入正确的计算式！");
			return;
		}

		if (currentOperaNum == operaNum1) {
			result = operaNum1.getValue();
			isOperaSuccess = true;
		} else 	if (OPERA_TYPE_NONE == currentOperaType) {
			isOperaSuccess = false;
		} else	if (OPERA_TYPE_ADD == currentOperaType) {
			add();
		} else if (OPERA_TYPE_SUB == currentOperaType) {
			sub();
		} else if (OPERA_TYPE_MUL == currentOperaType) {
			mul();
		} else if (OPERA_TYPE_DIV == currentOperaType) {
			div();
		}

		if (isOperaSuccess) {
			showResult();
			isExistResult = true;
		} else {
			Util.showTipText(getApplicationContext(), "请检查计算式是否有错！");
		}
	}

	private void showResult() {
		calcStr.append("＝");
		calcTextView.setText(calcStr.toString());
		resultTextView.setText(Double.toString(result));
	}

	@Override
	public void onClick(View view) {
		String btnText = ((Button)findViewById(view.getId())).getText().toString();
		switch (view.getId()) {
			case R.id.btn_Clear:
				clear();
				break;
			case R.id.btn_del:
				delClick();
				break;
			case R.id.btn_add:
				currentOperaType = OPERA_TYPE_ADD;
				operaClick(btnText);
				break;
			case R.id.btn_sub:
				currentOperaType = OPERA_TYPE_SUB;
				operaClick(btnText);
				break;
			case R.id.btn_mul:
				currentOperaType = OPERA_TYPE_MUL;
				operaClick(btnText);
				break;
			case R.id.btn_div:
				currentOperaType = OPERA_TYPE_DIV;
				operaClick(btnText);
				break;
			case R.id.btn_equal:
				calc();
				break;
			case R.id.btn_point:
				pointClick();
				break;
			case R.id.btn_00:
			case R.id.btn_0:
			case R.id.btn_1:
			case R.id.btn_2:
			case R.id.btn_3:
			case R.id.btn_4:
			case R.id.btn_5:
			case R.id.btn_6:
			case R.id.btn_7:
			case R.id.btn_8:
			case R.id.btn_9:
				numClick(btnText);
				break;
		}
//		LogUtil.d(TAG, "onClick: =====================================================" +
//				",\n calcText ==-== " + calcStr.toString() +
//				",\n currentOperaNum ==-== " + currentOperaNum.valueStr +
//				",\n operaNum1 ==-== " + operaNum1.valueStr +
//				",\n operaNum2 ==-== " + operaNum2.valueStr +
//				",\n operaStr ==-== " + operaStr +
//				",\n resultStr ==-== " + resultStr  +
//				",\n result ==-== " + result  +
//				",\n currentOperaType ==-== " + currentOperaType +
//				",\n isExistResult ==-== " + Boolean.toString(isExistResult)  +
//				",\n isOperaSuccess ==-== " + Boolean.toString(isOperaSuccess));

	}
}
