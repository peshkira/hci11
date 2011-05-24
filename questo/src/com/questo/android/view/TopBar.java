package com.questo.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.questo.android.R;
import com.questo.android.helper.DisplayHelper;
import com.questo.android.helper.FontHelper;

public class TopBar extends LinearLayout {

	private static final String TAG = "LabeledNumberInput";
	private static final int IMAGE_PADDING_LEFT_DP = 3;
	private static final int IMAGE_PADDING_DP = 7;
	private static final int BUTTON_WIDTH_AND_HEIGHT_DP = 51;

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
			labelText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
		}
	}

	public void setSpecialFont(boolean specialFont, Context context) {
		if (specialFont) {
			TextView labelText = (TextView) findViewById(R.id.label);
			labelText.setTypeface(FontHelper.getMedievalFont(context));
			labelText.setTextSize(35.0f);
		}
	}

	public Button addButtonLeftMost(final Context ctx, CharSequence label) {
		Button newButton = (Button) LayoutInflater.from(ctx).inflate(R.layout.topbar_button, null);
		newButton.setText(label);
		addButtonToLayout(newButton);
		return newButton;
	}
	
	public Button addImageButtonLeftMost(final Context ctx, int bitmapResource) {
		Button btn = addButtonLeftMost(ctx, "");
		addImageToButton(ctx, bitmapResource, btn);
		return btn;
	}

	public Button addImageToggleButtonLeftMost(final Context ctx, int bitmapResource, boolean checked) {
		Button btn = addToggleButtonLeftMost(ctx, "", checked);
		addImageToButton(ctx, bitmapResource, btn);
		return btn;
	}
	
	private void addImageToButton(Context ctx, int bitmapResource, Button btn) {
		BitmapDrawable bitmapDrawable = DisplayHelper.bitmapWithConstraints(bitmapResource, ctx,
				BUTTON_WIDTH_AND_HEIGHT_DP, IMAGE_PADDING_DP);

		float topOffset = DisplayHelper.dpToPixel(IMAGE_PADDING_DP, ctx);

		btn.setCompoundDrawablesWithIntrinsicBounds(null, bitmapDrawable, null, null);
		btn.setPadding(DisplayHelper.dpToPixel(IMAGE_PADDING_LEFT_DP, ctx), Math.round(topOffset), 0, 0);
	}

	public Button addToggleButtonLeftMost(final Context ctx, CharSequence label, boolean checked) {
		Button newButton = (ToggleButton) LayoutInflater.from(ctx).inflate(R.layout.topbar_togglebutton, null);
		((ToggleButton) newButton).setTextOn(label);
		((ToggleButton) newButton).setTextOff(label);
		((ToggleButton) newButton).setChecked(checked);

		addButtonToLayout(newButton);
		return newButton;
	}

	private void addButtonToLayout(View newButton) {
		LinearLayout buttonsContainer = (LinearLayout) findViewById(R.id.buttons_container);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		buttonsContainer.addView(newButton, 0, lp);
	}

	public void setLabel(String label) {
		TextView labelView = (TextView) findViewById(R.id.label);
		labelView.setText(label);
	}
}
