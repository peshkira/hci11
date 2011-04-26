package com.questo.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.questo.android.R;

public class TopBar extends LinearLayout {

	private static final String TAG = "LabeledNumberInput";

	private String labelStr;
	private boolean labelCentered;

	public TopBar(Context context, AttributeSet attr) {
		super(context, attr);
		initializeLayoutBasics(context);
		retrieveLabelString(context, attr);
		retrieveLabelCenteredAttr(context, attr);
	}

	private void initializeLayoutBasics(Context context) {
		setOrientation(HORIZONTAL);
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.topbar, this);
		TextView labelText = (TextView) findViewById(R.id.label);
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Dearest.ttf");
		labelText.setTypeface(tf);
	}

	private void retrieveLabelString(Context context, AttributeSet attr) {
		TextView labelText = (TextView) findViewById(R.id.label);
		final TypedArray a = context.obtainStyledAttributes(attr, R.styleable.TopBar);
		this.labelStr = a.getString(R.styleable.TopBar_label);
		labelText.setText(this.labelStr);
	}

	private void retrieveLabelCenteredAttr(Context context, AttributeSet attr) {
		final TypedArray a = context.obtainStyledAttributes(attr, R.styleable.TopBar);
		labelCentered = a.getBoolean(R.styleable.TopBar_label_centered, false);
		if (labelCentered) {
			TextView labelText = (TextView) findViewById(R.id.label);
			labelText.setGravity(Gravity.CENTER);
		}
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

	public void setTopBarLabel(String label) {
		TextView labelView = (TextView) findViewById(R.id.label);
		labelView.setText(label);
	}
}
