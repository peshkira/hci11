package com.questo.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.questo.android.R;
import com.questo.android.helper.FontHelper;

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
			labelText.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		}
	}

	public void setSpecialFont(boolean specialFont, Context context) {
		if (specialFont) {
			TextView labelText = (TextView) findViewById(R.id.label);
			labelText.setTypeface(FontHelper.getMedievalFont(context));
			labelText.setTextSize(35.0f);
		}
	}

	public Button addButtonLeftMost(final Context ctx, String label, boolean checked) {
		LinearLayout buttonsContainer = (LinearLayout) findViewById(R.id.buttons_container);
		ToggleButton newButton = (ToggleButton) LayoutInflater.from(ctx).inflate(R.layout.topbar_button, null);
		newButton.setTextOn(label);
		newButton.setTextOff(label);
		newButton.setChecked(checked);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		buttonsContainer.addView(newButton, 0, lp);
		return newButton;
	}

	public void setLabel(String label) {
		TextView labelView = (TextView) findViewById(R.id.label);
		labelView.setText(label);
	}
}
