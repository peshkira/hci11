package com.questo.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.questo.android.R;

public class TopBar extends LinearLayout {

	private static final String TAG = "LabeledNumberInput";

	private TextView labelText;
	private EditText input;
	private Button set;
	private String labelStr;

	private Float current;

	public TopBar(Context context, AttributeSet attr) {
		super(context, attr);
		initializeLayoutBasics(context);
		retrieveLabelString(context, attr);
	}

	private void initializeLayoutBasics(Context context) {
		setOrientation(HORIZONTAL);
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.topbar, this);
	}

	private void retrieveLabelString(Context context, AttributeSet attr) {
		this.labelText = (TextView) findViewById(R.id.label);
		final TypedArray a = context.obtainStyledAttributes(attr, R.styleable.TopBar);
		this.labelStr = a.getString(R.styleable.TopBar_label);
		this.labelText.setText(this.labelStr);
	}


	public Button addButtonLeftMost(final Context ctx, String label) {
		LinearLayout buttonsContainer = (LinearLayout) findViewById(R.id.buttons_container);
		Button newButton = (Button) LayoutInflater.from(ctx).inflate(R.layout.topbar_button, null);
		newButton.setText(label);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		buttonsContainer.addView(newButton, 0, lp);
		return newButton;
	}

	public void setInput(Float input) {
		this.current = input;

	}

	public Float getInput(Context ctx) {
		Float result;
		final String strValue = this.input.getText().toString();
		Log.d(TAG, "getInput:strValue=" + strValue);
		if (strValue != null && !strValue.equals("")) {
			try {
				result = Float.parseFloat(strValue);
			} catch (final NumberFormatException e) {
				Toast.makeText(ctx, "Failed to parse number", Toast.LENGTH_LONG).show();
				result = null;
			}
		} else {
			result = new Float(0);
		}
		Log.d(TAG, "getInput:result=" + result);
		return result;
	}

}
