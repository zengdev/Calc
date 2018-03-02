package com.example.calc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.example.calc.Utils.LogUtil;
import com.example.calc.Utils.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

	private static final String TAG = "MainActivity";

	// 控件
	private static TextView calcTextView;		// 计算式显示区域
	private static TextView resultTextView;	// 结果显示区域
//	private static Button addBtn;				// ＋
//	private static Button subBtn;				// －
//	private static Button mulBtn;				// ×
//	private static Button divBtn;				// ÷
//	private static Button pointBtn;				// 小数点

	// 运算操作类型
	private static final int OPERA_TYPE_NONE = 0;
	private static final int OPERA_TYPE_ADD = 1;
	private static final int OPERA_TYPE_SUB = 2;
	private static final int OPERA_TYPE_MUL = 3;
	private static final int OPERA_TYPE_DIV = 4;

	private static int currentOperaType;		// 当前运算操作类型

	// flag
	private static boolean isExistResult;
	private static boolean isOperaSuccess;

	// str
	private static StringBuilder calcStr;		// 计算式
	private static String operaStr;			// 运算操作

	// 操作数对象
	private static OperaNum operaNum1;
	private static OperaNum operaNum2;
	private static OperaNum currentOperaNum;

	private static double result = 0;		// 运算结果

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
			Util.clearStringBuilder(valueStr);
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
		operaNum1 = new OperaNum();
		operaNum2 = new OperaNum();
		calcTextView = findViewById(R.id.calc_textview);
		resultTextView = findViewById(R.id.result_textview);
//		addBtn = findViewById(R.id.btn_add);
//		subBtn = findViewById(R.id.btn_sub);
//		mulBtn = findViewById(R.id.btn_mul);
//		divBtn = findViewById(R.id.btn_div);
//		pointBtn = findViewById(R.id.btn_point);
		clearClick();
	}

	private void initData() {
		currentOperaType = OPERA_TYPE_NONE;
		currentOperaNum = operaNum1;
		isExistResult = false;
		operaNum1.init();
		operaNum2.init();
		Util.clearStringBuilder(calcStr);
		operaStr = "";
	}

	private void clearClick() {
		initData();
		Util.clearTextView(calcTextView, resultTextView);
	}

	private void numClick(String num) {
		String tempNum = num;
		if (isExistResult) {
			clearClick();
		}
		if (0 == currentOperaNum.valueStr.length() && num.equals("00")) {
			tempNum = "0";
		} else	if (currentOperaNum.valueStr.toString().equals("0")) {
			if (num.equals("0") || num.equals("00")) {
				return;
			} else {
				Util.clearStringBuilder(currentOperaNum.valueStr);
				calcStr.deleteCharAt(calcStr.length() - 1);
			}
		}
		currentOperaNum.valueStr.append(tempNum);
		calcStr.append(tempNum);
		calcTextView.setText(calcStr.toString());
	}

	private void pointClick() {
		if (isExistResult || currentOperaNum.havePoint || currentOperaNum.valueStr.length() == 0) {
			return;
		}
		currentOperaNum.valueStr.append(".");
		currentOperaNum.havePoint = true;
		calcStr.append(".");
		calcTextView.setText(calcStr.toString());
	}

	private boolean operaClick(String operaStr) {
		if (isExistResult || calcStr.length() == 0) {
			return false;
		}else if (calcStr.length() > 0) {
			String currentOperaNumLastOneStr = Util.getStringBuilderLastOneStr(currentOperaNum.valueStr);
			if (currentOperaNumLastOneStr.equals(".")) {
				Util.showTipText(getApplicationContext(), "操作数最后一位不能为小数点！");
				return false;
			}
			if (currentOperaNum == operaNum2) {
				if (currentOperaNum.valueStr.length() == 0) {
					calcStr.deleteCharAt(calcStr.length() - 1);
					this.operaStr = operaStr;
					calcStr.append(operaStr);
					calcTextView.setText(calcStr.toString());
					return true;
				}
				return false;
			} else {
				this.operaStr = operaStr;
				calcStr.append(operaStr);
				calcTextView.setText(calcStr.toString());
				currentOperaNum = operaNum2;
				return true;
			}
		}
		return false;
	}

	private void delClick() {
		String calcLastOneStr;
		if (calcStr.length() > 0) {
			calcLastOneStr = Util.getStringBuilderLastOneStr(calcStr);
		} else {
			return;
		}
		if (isExistResult) {
			Util.clearTextView(resultTextView);
			isExistResult = false;
		} else if (calcLastOneStr.equals(operaStr)) {
			operaStr = "";
			currentOperaType = OPERA_TYPE_NONE;
			currentOperaNum = operaNum1;
		} else {
			currentOperaNum.valueStr.deleteCharAt(currentOperaNum.valueStr.length() - 1);
			if (calcLastOneStr.equals(".")) {
				currentOperaNum.havePoint = false;
			}
		}
		calcStr.deleteCharAt(calcStr.length() - 1);
		calcTextView.setText(calcStr.toString());
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
		if (0 == operaNum2.getValue()) {    // 未验证----------------------
			Util.showTipText(getApplicationContext(), "除数不能为0！");
			isOperaSuccess = false;
		} else {
			result = operaNum1.getValue() / operaNum2.getValue();
			isOperaSuccess = true;
		}
	}

	private void calc() {
		if (isExistResult) {
			return;
		}

		String calcLastOneStr = Util.getStringBuilderLastOneStr(calcStr);

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
				clearClick();
				break;
			case R.id.btn_del:
				delClick();
				break;
			case R.id.btn_add:
				if (operaClick(btnText)) {
					currentOperaType = OPERA_TYPE_ADD;
				}
				break;
			case R.id.btn_sub:
				if (operaClick(btnText)) {
					currentOperaType = OPERA_TYPE_SUB;
				}
				break;
			case R.id.btn_mul:
				if (operaClick(btnText)) {
					currentOperaType = OPERA_TYPE_MUL;
				}
				break;
			case R.id.btn_div:
				if (operaClick(btnText)) {
					currentOperaType = OPERA_TYPE_DIV;
				}
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
	}
}
